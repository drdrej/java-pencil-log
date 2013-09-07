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
package com.j2biz.pencil.antlr.decorator;

import antlr.collections.AST;

import com.j2biz.pencil.antlr.CallerContext;
import com.j2biz.pencil.antlr.visitor.IdPartVisitor;
import com.j2biz.pencil.asm.JavaHelper;
import com.j2biz.pencil.asm.NullCodeBlock;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;
import com.j2biz.pencil.asm.logexpr.Scope;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LocalVariableDefinition;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.VariableDefinitionReferer;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class FirstIdPartASTDecorator extends IdPartASTDecorator {

	private VariableDefinitionReferer refererForFollowingCall;
	
	FirstIdPartASTDecorator( AST wrappedNode ) {
		super(wrappedNode);
	}

	private static VariableDefinitionReferer getFirstReferencePart(
		final String id,
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod) throws CompileTimeWarningException {

		final LocalVariableDefinition localVar = findLocalVar(id, logEntry);

		if ( localVar != null )
			return localVar.getLoadVariableNode();

		final FirstPartFieldRefInstruction fieldRef = JavaHelper.getField(id,
				callerMethod.getOwner(), callerMethod.getOwner(), callerMethod,
				true, callerMethod.isStatic());

		if ( fieldRef == null )
			throw new CompileTimeWarningException("can't find the variable: " + id);

		return fieldRef;
	}

	private static LocalVariableDefinition findLocalVar(
		final String id,
		final LogMessageNode logEntry ) {
		final Scope scope = logEntry.getScope();
		return (LocalVariableDefinition) scope.getVariable(id, logEntry);
	}

	@Override
	public Node createCodeBlockToStoreInATempStringVariable(
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		final int localVarIndex ) throws CompileTimeWarningException {

		final InstructionGroup rval = new InstructionGroup();

		final CallerContext ctx = new CallerContext(
				callerMethod, logEntry);
		
		rval.addInstruction(createIfElseCodeBlock(ctx, localVarIndex));

		rval.addInstruction(createValueOfBlock(logEntry, callerMethod,
				localVarIndex));

		return rval;
	}

	private Node createValueOfBlock(
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		final int localVarIndex ) throws CompileTimeWarningException {

		final String id = this.getText();
		final InstructionGroup rval = new InstructionGroup();

		final VariableDefinitionReferer callVariableCodeBlock = getAssignedVariableReferer();

		rval.addInstruction(callVariableCodeBlock);

		final VariableNode varDefinition = callVariableCodeBlock
				.getDefinition();

		selectWhatIsToDoWithNextPart(varDefinition, logEntry, callerMethod,
				localVarIndex, rval);

		return rval;
	}

	public Node createIfElseCodeBlock(
		final CallerContext callerCtx,
		final int localVarIndex ) throws CompileTimeWarningException {
		
		assert callerCtx != null;
		assert localVarIndex > -1;
		
		
		final String id = this.getText();
		final InstructionGroup rval = new InstructionGroup();

		final VariableDefinitionReferer callVariableCodeBlock = getFirstReferencePart(
				id, callerCtx.logEntry, callerCtx.callerMethod);

		rval.addInstruction(callVariableCodeBlock);

		final VariableNode varDefinition = callVariableCodeBlock
				.getDefinition();

		selectWhatIsToDoWithNextPart(varDefinition, callerCtx.logEntry, callerCtx.callerMethod,
				localVarIndex, rval);

		return NullCodeBlock.INSTANCE;
	}
	
	
	
	public void initVariableReferer(final CallerContext callerCtx) throws CompileTimeWarningException {
		assert callerCtx != null;
		assert this.refererForFollowingCall == null;
		
		final String id = this.getText();
		
        this.refererForFollowingCall = getFirstReferencePart(
				id, callerCtx.logEntry, callerCtx.callerMethod);
	}
	
	@Override
	public VariableDefinitionReferer getAssignedVariableReferer( ) {
		assert this.refererForFollowingCall != null;
		
		return this.refererForFollowingCall;
	}
	
	@Override
	public final void accept( final IdPartVisitor visitor ) throws CompileTimeWarningException {
		assert visitor != null;
		
		visitor.visit( this );
	}

}
