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

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.antlr.CallerContext;
import com.j2biz.pencil.antlr.visitor.IdPartVisitor;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.fields.FieldMatcher;
import com.j2biz.pencil.asm.fields.ReferencedCallFieldMatcher;
import com.j2biz.pencil.asm.logexpr.NextPartFieldRefInstruction;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class NextIdPartASTDecorator extends IdPartASTDecorator {

	private final String	castOfPrevVarPart;

	private NextPartFieldRefInstruction			refererForFollowingCall;

	NextIdPartASTDecorator( AST wrappedVariableToken,
			final String castOfPreviousVariablePart ) {
		super(wrappedVariableToken);

		assert (castOfPreviousVariablePart != null);
		Assert.stringIsNotEmpty(castOfPreviousVariablePart);

		this.castOfPrevVarPart = castOfPreviousVariablePart;
	}

	NextIdPartASTDecorator( AST wrappedVariableToken ) {
		super(wrappedVariableToken);

		assert (wrappedVariableToken != null);

		castOfPrevVarPart = null;
	}

	private boolean previousVariableIsCasted( ) {
		return castOfPrevVarPart != null;
	}

	public NextPartFieldRefInstruction findField(
		final VariableNode calledOnVariable,
		final MethodInfoNode callerMethod,

		final AbstractClassInfoNode ownerClassNode )
			throws CompileTimeWarningException {

		final AST fieldToken = this.getWrappedVariableToken();

		if ( fieldToken == null )
			throw new CompileTimeWarningException("cant find this field.");

		final String idName = fieldToken.getText();

		final FieldMatcher matcher = new ReferencedCallFieldMatcher(idName,
				callerMethod.getOwner(), callerMethod, calledOnVariable);

		final FieldVariableDefinition field = ownerClassNode
				.findThisField(matcher);

		if ( field == null ) 
			throw new CompileTimeWarningException("cant find/see this field.");
		

		final NextPartFieldRefInstruction fieldRef = new NextPartFieldRefInstruction(
				field);

		return fieldRef;
	}

	public AbstractClassInfoNode getVariableInstanceClassNode(
		final ClassManager classManager,
		final VariableNode lastVar ) {

		final String internalName = lastVar.getVariableInstanceType();

		if ( internalName == null )
			return null;

		return classManager.getClass(internalName);
	}

	public Node prepareValueOfVariable(
		final VariableNode calledOnVariable,
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		final int localVarIndex ) throws CompileTimeWarningException {
		
		assert (calledOnVariable != null);
		assert (logEntry != null);
		assert (callerMethod != null);
		assert (localVarIndex > -1);

		if ( previousVariableIsCasted() ) {
			// return parseAstAsRestOfReference(calledOnVariable,
			// this.getWrappedVariableToken(), logEntry, callerMethod);
			Assert.shouldNeverReachHere("no casting supported");
			return null;
		} else {
			final InstructionGroup rval = new InstructionGroup();

			final NextPartFieldRefInstruction fieldRef = getAssignedVariableReferer();
				
			rval.addInstruction(fieldRef);

			selectWhatIsToDoWithNextPart(fieldRef.getDefinition(), logEntry,
					callerMethod, localVarIndex, rval);

			return rval;
		}
	}

	public void initVariableReferer(
		final CallerContext callerCtx,
		final VariableNode calledOnVariable )
			throws CompileTimeWarningException {
		assert callerCtx != null;
		assert this.refererForFollowingCall == null;

		final MethodInfoNode callerMethod = callerCtx.callerMethod;

		final AbstractClassInfoNode fieldOwnerInstanceClass = getVariableInstanceClassNode(
				callerMethod.getOwner().getClassManager(), calledOnVariable);

		if ( fieldOwnerInstanceClass == null ) {
			throw new CompileTimeWarningException(
					"only instances of type OBJECT allow to call fields on it.");
		}

		this.refererForFollowingCall = findField(calledOnVariable,
				callerMethod, fieldOwnerInstanceClass);

		if ( this.refererForFollowingCall == null )
			throw new CompileTimeWarningException(
					"[PENCIL-WARNING] only instances of type OBJECT allow to call fields on it.");
	}

	@Override
	public NextPartFieldRefInstruction getAssignedVariableReferer( ) {
		assert this.refererForFollowingCall != null;
		
		return this.refererForFollowingCall; 
	}
	
	
	@Override
	public Node createCodeBlockToStoreInATempStringVariable(
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		final int localVarIndex ) throws CompileTimeWarningException {

		throw new UnsupportedOperationException();
	}

	@Override
	public final void accept( final IdPartVisitor visitor )
			throws CompileTimeWarningException {
		assert visitor != null;

		visitor.visit(this);
	}
}
