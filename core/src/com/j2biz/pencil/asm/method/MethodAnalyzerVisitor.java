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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.CodeVisitor;
import org.objectweb.asm.Label;

import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.logexpr.LogMsgScope;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.LineNumber;
import com.j2biz.pencil.asm.tree.LocalVariableDefinition;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.simple.FieldInsnNode;
import com.j2biz.pencil.asm.tree.simple.IincNode;
import com.j2biz.pencil.asm.tree.simple.InsnNode;
import com.j2biz.pencil.asm.tree.simple.IntInsnNode;
import com.j2biz.pencil.asm.tree.simple.JumpInsnNode;
import com.j2biz.pencil.asm.tree.simple.LookupSwitchInsnNode;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.asm.tree.simple.MultiANewArrayInsnNode;
import com.j2biz.pencil.asm.tree.simple.TableSwitchInsnNode;
import com.j2biz.pencil.asm.tree.simple.TypeInsnNode;
import com.j2biz.pencil.asm.tree.simple.VarInsnNode;
import com.j2biz.pencil.ex.ClassParseException;
import com.j2biz.utils.MapFactory;

/**
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com
 */
public class MethodAnalyzerVisitor implements CodeVisitor {

	private boolean								hasLogEntry	= false;

	private final MethodInfoNode				method;

	private final ErrorStatusLogger				logger;

	private final Map<Label, LogMessageNode>	lineNrCache	= MapFactory
																	.create();

	// TODO: in den iterator verschieben.
	private int									nrOfLogs	= 0;

	private final LogMsgBytecodePatternHandler	logMsgBytecodePatternHandler;

	// hilfsdaten:
	private Map<Label, LocalVariableDefinition>	startLables;

	private Map<Label, LocalVariableDefinition>	endLabels;

	private List<ClassParseException> errors = new ArrayList();
	
	/**
	 * @param access
	 * @param name
	 * @param desc
	 * @param exceptions
	 * @param attrs
	 * @throws NullPointerException
	 *             if parameter method is NULL.
	 */
	public MethodAnalyzerVisitor( final MethodInfoNode method,
			final ErrorStatusLogger logger ) {
		assert (method != null);
		assert (logger != null);

		this.method = method;
		this.logMsgBytecodePatternHandler = new LogMsgBytecodePatternHandler(
				method.getName(), method.instructions());
		this.logger = logger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitInsn(int)
	 */
	public void visitInsn( int opcode ) {
		InsnNode node = new InsnNode(opcode);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitIntInsn(int, int)
	 */
	public void visitIntInsn( final int opcode, final int operand ) {
		IntInsnNode node = new IntInsnNode(opcode, operand);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitVarInsn(int, int)
	 */
	public void visitVarInsn( int opcode, int var ) {
		VarInsnNode node = new VarInsnNode(opcode, var);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitTypeInsn(int, java.lang.String)
	 */
	public void visitTypeInsn( int opcode, String desc ) {
		final TypeInsnNode node = new TypeInsnNode(opcode, desc);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitFieldInsn(int, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void visitFieldInsn(
		int opcode,
		String owner,
		String name,
		String desc ) {
		final FieldInsnNode node = new FieldInsnNode(opcode, name, owner, desc);
		this.method.addInstruction(node);
	}

	/**
	 * check byteCode. if last entries in bytecode is one of both sequences: 1.)
	 * Label, String(ldc) 2.) Label, String(ldc), Label then remove this entries
	 * and create a LogMsgNode. If not, create a warning and don't change the
	 * bytecode.
	 */
	public void visitMethodInsn(
		final int opcode,
		final String owner,
		final String name,
		final String desc ) {

		if ( this.logMsgBytecodePatternHandler
				.isLogMethodByteCodePatternRecognized(opcode, owner) ) {
			try {
				handleLogMessage(name);
			} catch ( final ClassParseException x ) {
				this.errors.add(x);
				x.setAffectedMethod( this.method.getName() );
				
				// find previous label
				x.setLabel( method.getLastRegistredLabel() );
				
				final MethodInsnNode node = new MethodInsnNode(opcode, owner, name,
						desc);
				this.method.addInstruction(node);
			}
		} else {
			final MethodInsnNode node = new MethodInsnNode(opcode, owner, name,
					desc);
			this.method.addInstruction(node);
		}
	}

	private void handleLogMessage( final String name ) {
		final String msgTemp = this.logMsgBytecodePatternHandler
				.getMessageTemplate();

		final LabelWrapper lineNrOfLogExpressionLabel = logMsgBytecodePatternHandler
				.removeLastByteCodesToLastLabel();

		final LogMessageNode logMsgSlot = createAndAddNewLogMsgNode(name,
				msgTemp);

		registerLineNrHook(lineNrOfLogExpressionLabel, logMsgSlot);

		countNextLogEntry();
	}

	private void countNextLogEntry( ) {
		this.hasLogEntry = true;
		this.nrOfLogs++;
	}

	private void registerLineNrHook(
		final LabelWrapper lastLabel,
		final LogMessageNode logMsgSlot ) {
		this.lineNrCache.put(lastLabel.getWrappedNode(), logMsgSlot);
	}

	private final LogMessageNode createAndAddNewLogMsgNode(
		final String name,
		final String lastMsg ) {
		final LogMessageNode rval = new LogMessageNode(name, lastMsg);
		this.method.addInstruction(rval);
		return rval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitJumpInsn(int,
	 *      org.objectweb.asm.Label)
	 */
	public void visitJumpInsn( int opcode, Label label ) {
		JumpInsnNode node = new JumpInsnNode(opcode, label);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitLabel(org.objectweb.asm.Label)
	 */
	public void visitLabel( Label label ) {
		this.method.addLabel(label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitLdcInsn(java.lang.Object)
	 */
	public void visitLdcInsn( Object cst ) {
		this.method.addLdc(cst);

		if ( cst instanceof String )
			this.logMsgBytecodePatternHandler.markPossibleTemplateString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitIincInsn(int, int)
	 */
	public void visitIincInsn( int var, int increment ) {
		IincNode node = new IincNode(var, increment);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitTableSwitchInsn(int, int,
	 *      org.objectweb.asm.Label, org.objectweb.asm.Label[])
	 */
	public void visitTableSwitchInsn(
		final int min,
		final int max,
		final Label dflt,
		final Label[] labels ) {
		final TableSwitchInsnNode node = new TableSwitchInsnNode(min, max,
				dflt, labels);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitLookupSwitchInsn(org.objectweb.asm.Label,
	 *      int[], org.objectweb.asm.Label[])
	 */
	public void visitLookupSwitchInsn( Label dflt, int[] keys, Label[] labels ) {
		LookupSwitchInsnNode node = new LookupSwitchInsnNode(dflt, keys, labels);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitMultiANewArrayInsn(java.lang.String,
	 *      int)
	 */
	public void visitMultiANewArrayInsn( String desc, int dims ) {
		MultiANewArrayInsnNode node = new MultiANewArrayInsnNode(desc, dims);
		this.method.addInstruction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitTryCatchBlock(org.objectweb.asm.Label,
	 *      org.objectweb.asm.Label, org.objectweb.asm.Label, java.lang.String)
	 */
	public void visitTryCatchBlock(
		final Label start,
		final Label end,
		final Label handler,
		final String type ) {
		this.method.addTryCatchBlock(start, end, handler, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitMaxs(int, int)
	 */
	public void visitMaxs( int maxStack, int maxLocals ) {
		this.method.setMax(maxStack, maxLocals);
	}

	// ------------------------------ Additional Debug-Info
	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitLocalVariable(java.lang.String,
	 *      java.lang.String, org.objectweb.asm.Label, org.objectweb.asm.Label,
	 *      int)
	 */
	public void visitLocalVariable(
		final String name,
		final String desc,
		final Label start,
		final Label end,
		final int index ) {

		final LocalVariableDefinition definition = new LocalVariableDefinition(
				name, desc, start, end, index);

		if ( this.startLables == null )
			this.startLables = new FastMap(this.method.getNumberOfLocals());

		if ( this.endLabels == null )
			this.endLabels = new FastMap(this.method.getNumberOfLocals());

		this.startLables.put(start, definition);
		this.endLabels.put(end, definition);

		this.method.getVariableManager().addVariableDefinition(definition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitLineNumber(int,
	 *      org.objectweb.asm.Label)
	 */
	public void visitLineNumber( final int line, final Label start ) {
		this.method.addLineNumber(new LineNumber(line, start));

		final LogMessageNode registredLogMsgSlot = getRegistredLogMsgNode(start);

		if ( registredLogMsgSlot != null )
			registredLogMsgSlot.setLineNr(line);
		
		// setLineNumbers for errors
		for ( Iterator<ClassParseException> i = this.errors.iterator(); i.hasNext(); ) {
			final ClassParseException exception = i.next();
			
			if( exception.getLabel() == start) {
				assert exception.getLineNumber() == -1;
				
				exception.setLineNumber(line);
			}
		}
	}

	private LogMessageNode getRegistredLogMsgNode( final Label start ) {
		assert (start != null);
		final LogMessageNode registredLogMsgSlot = this.lineNrCache.get(start);

		return registredLogMsgSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.CodeVisitor#visitAttribute(org.objectweb.asm.Attribute)
	 */
	public void visitAttribute( final Attribute attr ) {
		this.method.addCodeAttribute(attr);
	}

	// ================ ::: 2 LifeCycle Step - Methods ::: ================
	/**
	 * @return
	 */
	public boolean hasLogCode( ) {
		return hasLogEntry;
	}

	/**
	 * this method prepares local-var-structure.
	 */
	public void prepareLocals( ) {
		// TODO: in den iterator verschieben..
		this.method.setNumberOfLogs(this.nrOfLogs);

		if ( hasNoLocalVars() )
			return;

		final List instr = this.method.instructions();
		LogMsgScope scope = createScopeForTheFirstLogMsg();

		for ( int counter = 0; instr.size() > counter; counter++ ) {
			final Object instrObj = instr.get(counter);
			if ( instrObj instanceof LogMessageNode ) {
				scope = createNewScopeAfterALogMsg(scope,
						(LogMessageNode) instrObj);
			} else if ( instrObj instanceof Label ) {
				Assert.shouldNeverReachHere("this code should be removed...");
			} else if ( instrObj instanceof LabelWrapper ) {
				updateScopeForALabel(scope, instrObj);
			}
		}
	}

	private boolean hasNoLocalVars( ) {
		return !this.method.getVariableManager().hasDefinitions();
	}

	private LogMsgScope createScopeForTheFirstLogMsg( ) {
		final int nextPossibleVarIdx = this.method.getVariableManager()
				.getNextPossibleVariableIndex();
		final LogMsgScope rval = new LogMsgScope(nextPossibleVarIdx);

		return rval;
	}

	private LogMsgScope createNewScopeAfterALogMsg(
		final LogMsgScope actualScope,
		final LogMessageNode actualLogMessage ) {
		actualLogMessage.setScope(actualScope);
		return LogMsgScope.create(actualScope, this.method);
	}

	private void updateScopeForALabel( LogMsgScope scope, final Object instrObj ) {
		final Label label = ((LabelWrapper) instrObj).getWrappedNode();

		final LocalVariableDefinition varAtBeginOfTheScope = this.startLables
				.get(label);

		if ( varAtBeginOfTheScope != null ) {
			scope.registerVariable(varAtBeginOfTheScope);
			return;
		}

		final LocalVariableDefinition varAtEndOfTheScope = this.endLabels
				.get(label);

		if ( varAtEndOfTheScope != null )
			scope.unregisterVariable(varAtEndOfTheScope);

	}

	public List<ClassParseException> getClassParseExceptions( ) {
		return this.errors;
	}
}