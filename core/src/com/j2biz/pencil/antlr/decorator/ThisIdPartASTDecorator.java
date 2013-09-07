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
import com.j2biz.pencil.asm.fields.FieldMatcher;
import com.j2biz.pencil.asm.fields.SimpleFieldMatcher;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.el.Template;
import com.j2biz.pencil.ex.CompileTimeWarningException;
import com.j2biz.pencil.ex.VariableNotFoundException;

public class ThisIdPartASTDecorator extends IdPartASTDecorator {

	private FirstPartFieldRefInstruction	refererForFollowingCall;

	ThisIdPartASTDecorator( AST wrappedNode ) throws CompileTimeWarningException {
		super(wrappedNode);

		assert (wrappedNode != null);

		if ( wrappedNode.getFirstChild() == null ) {
			throw new CompileTimeWarningException(
					"this-keyword can't be used alone. without a followed field-name.");
		}
	}

	@Override
	public AST getWrappedVariableToken( ) {
		assert (super.getWrappedVariableToken() != null) : "the super-implementation of this method returns a 'this'-AST-node.";

		return super.getWrappedVariableToken().getFirstChild();
	}

	@Override
	public Node createCodeBlockToStoreInATempStringVariable(
		LogMessageNode logEntry,
		MethodInfoNode callerMethod,
		int localVarIndex ) throws CompileTimeWarningException {

		final InstructionGroup getVariableBlock = new InstructionGroup();

		addGetVariableBlock(getVariableBlock, this.refererForFollowingCall);

		selectWhatIsToDoWithNextPart(
				(getAssignedVariableReferer()
						.getDefinition()), logEntry, callerMethod,
				localVarIndex, getVariableBlock);
		
		final InstructionGroup rval = new InstructionGroup();

		rval.addInstruction(getVariableBlock);

		return rval;
	}

	public void initVariableReferer( final CallerContext callerCtx )
			throws CompileTimeWarningException {
		assert callerCtx != null;
		assert this.refererForFollowingCall == null;

		final MethodInfoNode callerMethod = callerCtx.callerMethod;

		final FieldMatcher matcher = new SimpleFieldMatcher(getText(),
				callerMethod.getOwner(), callerMethod);

		final FieldVariableDefinition field = callerMethod.getOwner()
				.findThisField(matcher);

		if ( field != null )
			this.refererForFollowingCall = FirstPartFieldRefInstruction
					.createFirstPartReference(field);
		else
			throw new VariableNotFoundException(getText());
	}

	@Override
	public FirstPartFieldRefInstruction getAssignedVariableReferer( ) {
		assert this.refererForFollowingCall != null;

		return this.refererForFollowingCall;
	}

	private String getReferenceAsString( ) {
		return Template.referenceToString(getWrappedVariableToken());
	}

	private void addGetVariableBlock(
		final InstructionGroup rval,
		final FirstPartFieldRefInstruction fieldRef ) {
		rval.addInstruction(fieldRef);
	}

	@Override
	public final void accept( final IdPartVisitor visitor )
			throws CompileTimeWarningException {
		assert visitor != null;

		visitor.visit(this);
	}

}
