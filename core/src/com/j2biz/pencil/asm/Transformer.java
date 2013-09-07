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
package com.j2biz.pencil.asm;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.pencil.Defaults;
import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.InnerClassNode;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.LineNumber;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.ModifiableClassInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.simple.TryCatchBlockNode;

/**
 * this class is a visitor for ASM-trees. modification of the tree is allowed.
 * to delete a node, return it self.
 * 
 * to look up, use marks. use markstack, to mark more than one mark.
 * 
 * all classes for modification of trees should implement this class.
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class Transformer {

	private final ErrorStatusLogger logger;

	/**
	 * @param computeMaxs
	 */
	public Transformer(boolean computeMaxs, final ErrorStatusLogger logger) {
		this.computeMaxs = computeMaxs;
		if (logger == null) {
			throw new NullPointerException("parameter:logger");
		}

		this.logger = logger;
	}

	private boolean started = false;

	private final boolean computeMaxs;

	public final byte[] start(ASMClassInfoNode classNode) {
		assert classNode != null;

		// TODO: zuerst den logger initialisieren. bzw. zusaetzliche transformationen ausführen.
		// erst dann schreiben
		for (final Iterator i = classNode.methods(); i.hasNext();) {
			final MethodInfoNode method = (MethodInfoNode) i.next();
//			visit(classNode, node, writer);
			beforeWriteMethod( classNode, method);
		}
		
		init(classNode);
		
		final ClassWriter writer = new ClassWriter(true,
				Defaults.SKIP_UNKNOWN_ATTRIBUTES);


		
		writer.visit(classNode.getVersion(), classNode.getAccessFlags(),
				classNode.getClassName(), classNode.getSuperClassName(),
				classNode.getInterfaces(), classNode.getJavaSource());

		for (final Iterator i = classNode.innerClassNodes(); i.hasNext();) {
			final InnerClassNode node = (InnerClassNode) i.next();
			visit(classNode, node, writer);
		}

		for (final Iterator i = classNode.fields(); i.hasNext();) {
			final FieldVariableDefinition node = (FieldVariableDefinition) i
					.next();
			visit(classNode, node, writer);
		}

		for (final Iterator i = classNode.methods(); i.hasNext();) {
			final MethodInfoNode node = (MethodInfoNode) i.next();
			visit(classNode, node, writer);
		}

		Attribute attrs = classNode.getFirstAttribute();
		while (attrs != null) {
			writer.visitAttribute(attrs);
			attrs = attrs.next;
		}

		
		
		end();

		return writer.toByteArray();
	}

	/**
	 * this method is called after the class is written.
	 * 
	 * @param node
	 *            instance of the class. never NULL.
	 */
	public void end() {
		this.started = false;
	}

	/**
	 * this method is called before the class is written.
	 * 
	 * @param node
	 */
	public void init(ASMClassInfoNode node) {
		this.started = true;
	}

	public void visit(ModifiableClassInfoNode classNode, InnerClassNode node,
			ClassWriter writer) {
		writer.visitInnerClass(node.getName(), node.getOuterName(), node
				.getInnerName(), node.getAccess());
	}

	public void visit(ModifiableClassInfoNode classNode,
			FieldVariableDefinition node, ClassWriter writer) {
		writer.visitField(node.getAccessFlags(), node.getName(), node
				.getDescription(), node.getValue(), node.getAttributes());
	}

	public void visit(final ModifiableClassInfoNode classInfo,
			final MethodInfoNode callerMethod, final ClassWriter writer) {
		CodeWriter codeWriter = (CodeWriter) writer.visitMethod(callerMethod
				.getAccess(), callerMethod.getName(), callerMethod
				.getDescription(), callerMethod.getExceptionsArray(),
				callerMethod.getFirstAttribute());

//		beforeWriteMethod(classInfo, callerMethod);

		writeMethodByteCode(callerMethod, codeWriter);
	}

	private void writeMethodByteCode( final MethodInfoNode callerMethod, CodeWriter codeWriter ) {
		if (callerMethod.numberOfInstructions() > 0) {
			{
				final List list = callerMethod.instructions();
				accept(list, codeWriter);
			}

			{
				final List list = callerMethod.tryCatchBlocks();
				for (int i = 0; i < list.size(); i++) {
					TryCatchBlockNode block = (TryCatchBlockNode) list.get(i);
					codeWriter.visitTryCatchBlock(block.getStart(), block
							.getEnd(), block.getHandler(), block.getType());
				}
				
				codeWriter.visitMaxs(callerMethod.getStackSize(), callerMethod
						.getNumberOfLocals());
			}

			callerMethod.getVariableManager().accept(codeWriter);

			{
				final List list = callerMethod.lineNumbers();
				for (int i = 0; i < list.size(); i++) {
					final LineNumber nr = (LineNumber) list.get(i);
					codeWriter
							.visitLineNumber(nr.getLine(), nr.getStartLabel());
				}
			}

			Attribute attrs = callerMethod.getFirstCodeAttribute();
			while (attrs != null) {
				codeWriter.visitAttribute(attrs);
				attrs = attrs.next;
			}
		}
	}


	/**
	 * @param list
	 */
	private void accept(final List list, final CodeWriter codeWriter) {
		for (int i = 0; i < list.size(); i++) {
			Object instr = getInstruction(list, i);
			if (instr == null) {
				continue;
			}

			if (instr instanceof Node) {
				visitInstruction((Node) instr, codeWriter);
			} else if (instr instanceof Label) {
				final LabelWrapper wrapper = new LabelWrapper((Label) instr);
				wrapper.accept(codeWriter);
			} else if (instr instanceof String || instr instanceof Number
					|| instr instanceof Type) {
				// visitLdc(instr, codeWriter);
				Assert.shouldNeverReachHere("remove it: " + instr);
			} else {
				throw new UnsupportedOperationException("value : " + instr);
			}
		}
	}

	private Object getInstruction(final List list, int i) {
		Object instr = list.get(i);
		if (instr instanceof String || instr instanceof Number
				|| instr instanceof Type) {
			instr = new LdcWrapper(instr);
		}
		return instr;
	}

	/**
	 * This method is called before a method is written. This method should be
	 * overwritten, if you want modify the method code before it is written.
	 * 
	 * @param classInfo
	 *            the instance of the class. never NULL.
	 * @param node
	 *            the instance of the method. never NULL.
	 */
	public void beforeWriteMethod(ModifiableClassInfoNode classInfo,
			MethodInfoNode node) {
		;
	}

	/**
	 * @param instr
	 */
	public static void visitLdc(final Object instr, final CodeWriter codeWriter) {
		new LdcWrapper(instr).accept(codeWriter);
	}

	public void visitInstruction(final Node node, final CodeWriter codeWriter) {
		// if( node instanceof Acceptable ) {
		// ((Acceptable) node).accept(codeWriter);

		if (node instanceof Acceptable) {
			((Acceptable) node).accept(codeWriter);
		} else if (node instanceof InstructionGroup) {
			final InstructionGroup group = (InstructionGroup) node;
			group.accept(codeWriter, this);
		} else {
			throw new UnsupportedOperationException(
					"type of node not support.: " + node);
		}
	}

	/**
	 * 
	 */
	public final ErrorStatusLogger getLogger() {
		return this.logger;
	}
}