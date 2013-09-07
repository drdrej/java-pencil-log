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

import java.util.NoSuchElementException;

import org.objectweb.asm.Constants;

import antlr.collections.AST;

import com.j2biz.pencil.antlr.visitor.IdPartVisitor;
import com.j2biz.pencil.asm.AsmUtils;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.VariableDefinitionReferer;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.asm.tree.simple.VarInsnNode;
import com.j2biz.pencil.el.AntlrUtils;
import com.j2biz.pencil.el.TemplateTreeParserTokenTypes;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public abstract class IdPartASTDecorator extends ASTDecorator {

	private boolean				isInitialized	= false;

	private NextIdPartASTDecorator	nextPart;

	IdPartASTDecorator( AST wrappedNode ) {
		super(wrappedNode);
		assert wrappedNode != null;
	}

	public static IdPartASTDecorator createFirstPart( AST idPartAst )
			throws CompileTimeWarningException {
		Assert.isNotNull(idPartAst); 

		if ( "super".equalsIgnoreCase(idPartAst.getText()) )
			return new SuperIdPartASTDecorator(idPartAst);
		if ( "this".equalsIgnoreCase(idPartAst.getText()) )
			return new ThisIdPartASTDecorator(idPartAst);
		if ( idPartAst.getText().length() > 0 )
			return new FirstIdPartASTDecorator(idPartAst);

		throw new IllegalArgumentException(
				"[parameter:idPartAST] this astNode is not supported. idPartAst == "
						+ idPartAst);
	}

	public abstract Node createCodeBlockToStoreInATempStringVariable(
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		int localVarIndex ) throws CompileTimeWarningException;

	public boolean hasNextVariablePart( ) {
		initializeLazy();
		return nextPart != null;
	}

	private void initializeLazy( ) {
		if ( isInitialized )
			return;

		AST firstToken = getWrappedVariableToken().getFirstChild();
		if ( firstToken == null ) {
			isInitialized = true;
			return;
		}

		final AST nextPartToken;
		final String scopeString;
		if ( isScopeToken(firstToken) ) {
			scopeString = AntlrUtils.createScopeId(firstToken);
			nextPartToken = firstToken.getNextSibling();
		} else {
			nextPartToken = firstToken;
			scopeString = null;
		}

		initializeNextPart(nextPartToken, scopeString);
		isInitialized = true;
	}

	private void initializeNextPart(
		final AST nextVariablePartToken,
		final String scopeString ) {
		if ( nextVariablePartToken == null )
			return;

		this.nextPart = create(nextVariablePartToken, scopeString);
	}

	public static NextIdPartASTDecorator create(
		final AST nextVariablePartToken,
		final String scopeString ) {
		
		if ( scopeString == null ) {
			return new NextIdPartASTDecorator(nextVariablePartToken);
		} else {
			return new NextIdPartASTDecorator(nextVariablePartToken, scopeString);
		}
	}

	private static boolean isScopeToken( final AST ast ) {
		Assert.isNotNull(ast);

		return ast.getType() == TemplateTreeParserTokenTypes.SCOPE;
	}

	public NextIdPartASTDecorator getNextVariablePart( ) {
		if ( this.hasNextVariablePart() ) {
			return this.nextPart;
		} else {
			throw new NoSuchElementException(
					"this part has no next part. node-text : " + this.getText());
		}
	}

	protected void selectWhatIsToDoWithNextPart(
		final VariableNode calledOnVariable,
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		final int tempLocalVarIdx,
		final InstructionGroup instructions ) throws CompileTimeWarningException {

		if ( hasNextVariablePart() )
			prepareNextPart(calledOnVariable, logEntry, callerMethod,
					tempLocalVarIdx, instructions);
		else
			addValueOfCode(calledOnVariable, tempLocalVarIdx, instructions);
	}

	private void addValueOfCode(
		final VariableNode calledOnVariable,
		final int tempLocalVarIdx,
		final InstructionGroup instructions ) {
		final String type4Append = AsmUtils.StringBuffer
				.getTypeForValueOf(calledOnVariable);
		instructions.addInstruction(new MethodInsnNode(Constants.INVOKESTATIC,
				"java/lang/String", "valueOf", "(" + type4Append
						+ ")Ljava/lang/String;"));
		instructions.addInstruction(new VarInsnNode(Constants.ASTORE,
				tempLocalVarIdx));
	}

	private void prepareNextPart(
		final VariableNode calledOnVariable,
		final LogMessageNode logEntry,
		final MethodInfoNode callerMethod,
		final int tempLocalVarIdx,
		final InstructionGroup instructions ) throws CompileTimeWarningException {
		
		final NextIdPartASTDecorator nextPart = getNextVariablePart();
		final Node nextPartCode = nextPart.prepareValueOfVariable(
				calledOnVariable, logEntry, callerMethod, tempLocalVarIdx);
		
		instructions.addInstruction(nextPartCode);
	}
	
	public abstract void accept(final IdPartVisitor visitor) throws CompileTimeWarningException;

	public VariableDefinitionReferer getAssignedVariableReferer( ) {
		return null;
	}

}
