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

import org.objectweb.asm.Constants;
import org.objectweb.asm.Type;

import antlr.collections.AST;

import com.j2biz.pencil.antlr.CallerContext;
import com.j2biz.pencil.antlr.visitor.IfBlockCreatorIdPartVisitor;
import com.j2biz.pencil.antlr.visitor.PrepareIdPartVisitor;
import com.j2biz.pencil.asm.LdcWrapper;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.asm.tree.simple.VarInsnNode;
import com.j2biz.pencil.el.Template;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class ReferencePartASTDecorator extends LogMsgPartASTDecorator {

	private static final int	UNKNOWN_LOCAL_VAR_INDEX	= -1;

	private int					localVariableIndex		= UNKNOWN_LOCAL_VAR_INDEX;
	
	private CompileTimeWarningException	exception =  null;

	ReferencePartASTDecorator( AST wrappedNode ) {
		super(wrappedNode);
	}

	public Node createSetStringCode(
		final MethodInfoNode callerMethod,
		final LogMessageNode logEntry ) {
		final InstructionGroup rval = new InstructionGroup();

		assert (localVarIsSetted()) : "Local variable should be setted, but is not.\n"
				+ " reference: "
				+ getReferenceAsString()
				+ ", callerClass: "
				+ callerMethod.getOwner()
				+ ", in callerMethod: "
				+ callerMethod.getName();

		addLoadStringVariable(rval);

		addStringBubbersAppendMethodCall(rval);

		return rval;
	}

	private void addStringBubbersAppendMethodCall(
		final InstructionGroup destinationGroup ) {
		assert (destinationGroup != null) : "the destination instruction group must be not NULL.";

		final String type4Append = Type.getDescriptor(String.class);

		destinationGroup.addInstruction(new MethodInsnNode(
				Constants.INVOKEVIRTUAL, "java/lang/StringBuffer", "append",
				"(" + type4Append + ")Ljava/lang/StringBuffer;"));
	}

	private void addLoadStringVariable( final InstructionGroup rval ) {
		VarInsnNode.addLoadReferenceVariableCode(this.localVariableIndex, rval);
	}

	private String getReferenceAsString( ) {
		return Template.referenceToString(getWrappedVariableToken());
	}

	private IdPartASTDecorator getFirstIdPart( ) throws CompileTimeWarningException {
		final AST referenceAST = getWrappedVariableToken();
		final AST firstIdPartAST = referenceAST.getFirstChild();

		final IdPartASTDecorator idPart = IdPartASTDecorator
				.createFirstPart(firstIdPartAST);

		return idPart;
	}

	@Override
	public Node createCodeBlockBeforeDebugCall(
		final MethodInfoNode callerMethod,
		final LogMessageNode logEntry ) {

		assert (!localVarIsSetted()) : "localVariableIndex is allready setted";

		this.localVariableIndex = logEntry.getScope().createTempVariable();

		try {

			final IdPartASTDecorator idPart = getFirstIdPart();

			final CallerContext callerCtx = new CallerContext(callerMethod,
					logEntry);

			final PrepareIdPartVisitor prepareVisitor = new PrepareIdPartVisitor(
					callerCtx);
			idPart.accept(prepareVisitor);

			final IfBlockCreatorIdPartVisitor ifCreatorVisitor = new IfBlockCreatorIdPartVisitor(
					callerCtx, this.localVariableIndex, prepareVisitor
							.getVariableRefererStack());

			idPart.accept(ifCreatorVisitor);
			
			return ifCreatorVisitor.getCodeBlock();
		} catch ( final CompileTimeWarningException x ) {
			final InstructionGroup rval = new InstructionGroup();
			final String refAsString = getReferenceAsString();

			final String valueOfTypeForString = Type
					.getDescriptor(Object.class);

			rval.addInstruction(new LdcWrapper(String.valueOf(refAsString)));

			rval.addInstruction(new VarInsnNode(Constants.ASTORE,
					this.localVariableIndex));

			final CallerContext ctx = new CallerContext(callerMethod, logEntry);

			collectWarningData(x, ctx, this);
			return rval;
		}
	}

	private void collectWarningData(
		final CompileTimeWarningException x,
		final CallerContext ctx,
		final ReferencePartASTDecorator referenceNode ) {

		this.exception = x;
		this.exception.setCallerClassName( ctx.getCallerClass().toString() );
		this.exception.setCallerMethodName( ctx.callerMethod.getName() );
		this.exception.setCallerSource( ctx.getCallerClass().getSource() );
		this.exception.setLineNumber( ctx.logEntry.getLineNr() );
		
		this.exception.addAdditionalInfo( "logEntry", String.valueOf( ctx.logEntry) );
		this.exception.addAdditionalInfo( "callerMethodHeader", String.valueOf( ctx.callerMethod ) );
		this.exception.addAdditionalInfo( "referencePart", getReferenceAsString());
	}
	
	public boolean hasWarning() {
		return this.exception != null;
	}
	
	public CompileTimeWarningException getWarning() {
		if( this.exception == null )
			throw new IllegalStateException( "has no compile time warnings." );
		
		return this.exception;
	}
	
	private boolean localVarIsSetted( ) {
		return this.localVariableIndex > UNKNOWN_LOCAL_VAR_INDEX;
	}
}
