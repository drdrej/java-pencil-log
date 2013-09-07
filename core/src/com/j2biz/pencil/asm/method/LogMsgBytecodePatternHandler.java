/* 
 * "Pencil - Log message compiler" is (c) 2004 Andreas Siebert (j2biz community)
 *
 * Author: Andreas Siebert.
 *  
 * This file is part of "Pencil - Log message compiler".
 *
 * "Pencil - Log message compiler" is free software; 
 * you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version.
 *
 * "Pencil - Log message compiler" is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Pencil - Logger message compiler"; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package com.j2biz.pencil.asm.method;

import java.util.List;

import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.LdcWrapper;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.ClassParseException;
import com.j2biz.pencil.ex.NotAllowedInstructionBeforeLogCall;
import com.j2biz.utils.StringUtils;


public class LogMsgBytecodePatternHandler {

	private int					idxOfLastTemplate;

	private final String		enclosingMethodName;

	private final List<Node>	instructions;

	private String				msgTemp;

	public LogMsgBytecodePatternHandler( final String enclosingMethodName,
			final List<Node> instructions ) {
		assert (enclosingMethodName != null);
		assert (instructions != null);

		this.enclosingMethodName = enclosingMethodName;
		this.instructions = instructions;
	}

	
	private LabelWrapper analyse( final List<Node> instructions, final String name ) {
		final Node firstPart = getPartOne(instructions);

		if ( !(firstPart instanceof LabelWrapper) ) 
			throw new NotAllowedInstructionBeforeLogCall("found wrong instruction before log-call:", firstPart);
		

		final LabelWrapper rval = (LabelWrapper) firstPart;

		final Node secondPart = getPartTwo(instructions);

		if ( !(secondPart instanceof LdcWrapper && ((LdcWrapper) secondPart)
				.isStringValue()) )
//			throw new ClassParseException(
//					"ByteCode is not parseable by pencil. every log-call accept only strings. dynamic values or static but non-string values are not allowed."
//							+ "\n found BYTE-CODE COMMAND: "
//							+ getClassNameOfInstruction(secondPart)
//							+ " log-method-parameter: "
//							+ secondPart
//							+ "; in method-name =" + name);
			new NotAllowedInstructionBeforeLogCall("log-call accept only string-values. no variables or concatenated strings. found instruction:", secondPart);

		if ( hasThirdPart(instructions) ) {
			final Node thirdPart = getThirdPart(instructions);

			if ( !(thirdPart instanceof LabelWrapper) )
				throw new ClassParseException(
						"ByteCode is not parseable by pencil. log-methods in pencil supports only one value as parameter. contcatenating values are not allowed."
								+ "\n found BYTE-CODE COMMAND: "
								+ getClassNameOfInstruction(thirdPart)
								+ " log-method-parameter: "
								+ thirdPart
								+ "; in method-name =" + name);
		}

		return rval;
	}
//
//	private LabelWrapper findPreviousLabel( ) {
//		for ( final Iterator i = this.instructions.iterator(); i.hasNext(); ) {
//			final Object instruction = i.next();
//
//			if ( instruction instanceof LabelWrapper )
//				return (LabelWrapper) instruction;
//		}
//
//		throw new ClassParseException(
//				"no previous label => no linenumbers. => no debug infos.");
//	}

	private boolean hasThirdPart( final List instructions ) {
		return instructions.size() >= this.idxOfLastTemplate + 2;
	}

	private Node getThirdPart( List instructions ) {
		return (Node) instructions.get(this.idxOfLastTemplate + 1);
	}

	private Node getPartTwo( final List instructions ) {
		return (Node) instructions.get(this.idxOfLastTemplate);
	}

	private Node getPartOne( final List instructions ) {
		Node rval = (Node) instructions.get(this.idxOfLastTemplate - 1);

		return rval;
	}

	private void remove( final List<Node> instructions ) {
		while ( instructions.size() > this.idxOfLastTemplate )
			instructions.remove(instructions.size() - 1);
	}

	/**
	 * @param instr
	 * @return
	 */
	private String getClassNameOfInstruction( Object instr ) {
		return StringUtils.getClassName(instr);
	}

	public void setLastTemplateIdx( int idxOfLastTemplate ) {
		assert (idxOfLastTemplate > 0);

		this.idxOfLastTemplate = idxOfLastTemplate;
	}

	/**
	 * die Reihenfolge der Aufrufe:
	 *      1. setIdx()
	 *      2. jetzt erst remove. weil sont das falsche remove verwendet wird. sprich zustandswechsel.
	 *         evtl. State-Pattern anwenden.
	 *           
	 * @return LabelWrapper as marker for the last line
	 */
	public LabelWrapper removeLastByteCodesToLastLabel( ) {
		assert (this.instructions != null);
		assert (this.enclosingMethodName != null);
		assert (this.idxOfLastTemplate > 0) : "index of last template is not specified. please call setter for this value before execute.";
//		assert (this.idxOfLastTemplate >= this.instructions.size() - 3) : "setIdx may be wrong called. or bytecode is wrong. index of last template == " + this.idxOfLastTemplate + ", number of instructions == " + this.instructions.size();
//      ersetze durch eine sate-prüfung.
		
		analyse(this.instructions, this.enclosingMethodName);
		remove(this.instructions);

		final LabelWrapper wrapper = (LabelWrapper) this.instructions
				.get(instructions.size() - 1);

		return wrapper;
	}

	public boolean isLogMethodByteCodePatternRecognized(
		final int opcode,
		final String callerOwner ) {
		return opcode == Constants.INVOKESTATIC && callerOwner.endsWith("/LOG")
				&& "com/j2biz/log/LOG".equals(callerOwner);
	}

	public String getMessageTemplate( ) {
		return this.msgTemp;
	}

	public void markPossibleTemplateString( ) {
		assert (this.instructions != null);
		assert (this.instructions.size() > 0);

		this.idxOfLastTemplate = this.instructions.size() - 1;
		this.msgTemp = this.instructions.get(this.idxOfLastTemplate).toString();
	}

}
