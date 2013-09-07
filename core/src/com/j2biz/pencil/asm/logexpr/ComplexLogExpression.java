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
package com.j2biz.pencil.asm.logexpr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.j2biz.pencil.antlr.decorator.LogEntryASTDecorator;
import com.j2biz.pencil.antlr.decorator.LogMsgPartASTDecorator;
import com.j2biz.pencil.asm.AsmUtils;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class ComplexLogExpression extends AbstractLogExpression {

	private final LogEntryASTDecorator logEntryNode;
	
	private List<CompileTimeWarningException>	exceptions = new ArrayList();
	
	public ComplexLogExpression(final LogMessageNode logEntry,
			final MethodInfoNode callerMethod) {
		super(logEntry, callerMethod);
		
		this.logEntryNode = new LogEntryASTDecorator(logEntry);
	}

	public Node createReplace(LogTransformer transformer) {
		final InstructionGroup rval = new InstructionGroup();

		addStartLabel(rval);
		// store string in the variable. (with if-locks)
		addCodeBlockBeforeCall(rval);
		
		addLoadLoggerFieldInstruction(rval);
		addStringBufferConstructorCode(rval);
		// hier die Variableladen und in den StringBuffer reinhaengen.
		rval.addInstruction(createMsgPartCode(transformer));
		addStringBufferToStringCall(rval);
		rval.addInstruction(logEntry.createDebugCall());
		
		addEndLabel(rval);

		return rval;
	}

	private void addCodeBlockBeforeCall(InstructionGroup rval) {
		for (LogMsgPartASTDecorator logMsgPart : logEntryNode) {
			final Node logMsgPartCode = logMsgPart.createCodeBlockBeforeDebugCall(this.callerMethod,
					this.logEntry);
			
			rval.addInstruction(logMsgPartCode);
			
			if( logMsgPart.hasWarning() )
				addWarning( logMsgPart.getWarning() );
		}
	}

	private void addWarning( CompileTimeWarningException warning ) {
		this.exceptions .add( warning );
	}
	
	public boolean hasWarnings() {
		return !this.exceptions.isEmpty();
	}
	
	public List<CompileTimeWarningException> getWarnings() {
		return Collections.unmodifiableList( this.exceptions );
	}
	
	

	private void addLoadLoggerFieldInstruction(InstructionGroup rval) {
		rval.addInstruction(callerMethod.getOwner().getLoggerFieldRef(
				callerMethod));
	}

	private void addEndLabel(InstructionGroup rval) {
		rval.addInstruction(new LabelWrapper(this.logEntry.getScopeEnd()));
	}

	private void addStartLabel(InstructionGroup rval) {
		rval.addInstruction(new LabelWrapper(this.logEntry.getScopeStart()));
	}

	private void addStringBufferToStringCall(
			final InstructionGroup newInstructionGroup) {
		final Node toStringCall = AsmUtils.StringBuffer.createToStringCall();
		newInstructionGroup.addInstruction(toStringCall);
	}

	private void addStringBufferConstructorCode(
			final InstructionGroup newInstructionGroup) {
		final Node constructorCode = AsmUtils.StringBuffer
				.createConstructorCode(logEntry);
		newInstructionGroup.addInstruction(constructorCode);
	}

	private Node createMsgPartCode(final LogTransformer transformer) {
		final InstructionGroup rval = new InstructionGroup();

		for (LogMsgPartASTDecorator logMsgPart : logEntryNode) {
			final Node logMsgPartCode = logMsgPart.createSetStringCode(callerMethod,
					logEntry);
			rval.addInstruction(logMsgPartCode);
		}

		return rval;
	}
}
