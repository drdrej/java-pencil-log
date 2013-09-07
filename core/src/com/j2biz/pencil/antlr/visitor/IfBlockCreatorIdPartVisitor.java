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
package com.j2biz.pencil.antlr.visitor;

import java.util.List;

import org.objectweb.asm.Constants;

import com.j2biz.pencil.antlr.CallerContext;
import com.j2biz.pencil.antlr.decorator.FirstIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.IdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.NextIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.ThisIdPartASTDecorator;
import com.j2biz.pencil.asm.AsmUtils;
import com.j2biz.pencil.asm.LdcWrapper;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.VariableDefinitionReferer;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.asm.tree.simple.JumpInsnNode;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.asm.tree.simple.VarInsnNode;
import com.j2biz.pencil.el.Template;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class IfBlockCreatorIdPartVisitor extends IdPartVisitor {

	private final int						tempLocalVarIdx;

	private final InstructionGroup			codeBlock				= new InstructionGroup();

	private final CallerContext				callerContext;

	private int								levelCounter			= -1;

	private List<VariableDefinitionReferer>	variableStack;

	private LabelWrapper					lastJumpWrapper			= new LabelWrapper();

	private final LabelWrapper				callLoggerJumpWrapper	= new LabelWrapper();

	public IfBlockCreatorIdPartVisitor( final CallerContext callerCtx,
			final int tempLocalVarIdx,
			final List<VariableDefinitionReferer> variableStack ) {
		assert tempLocalVarIdx > -1;
		assert callerCtx != null;
		assert variableStack != null;

		this.tempLocalVarIdx = tempLocalVarIdx;
		this.callerContext = callerCtx;
		this.variableStack = variableStack;
	}

	@Override
	public void visit( final FirstIdPartASTDecorator part )
			throws CompileTimeWarningException {
		assert part != null;

		visitPart(part);
	}

	private void visitPart( final IdPartASTDecorator part )
			throws CompileTimeWarningException {
		this.levelCounter++;

		if ( part.hasNextVariablePart() )
			createIfBlock(part);
		else
			createElseBlock(part);

		next(part);
	}

	private void createElseBlock( final IdPartASTDecorator part ) {
		assert this.variableStack != null;
		assert this.levelCounter >= 0;
		assert this.levelCounter < this.variableStack.size();

		createGetVariableBlock();

		addValueOfCode(part);

		this.codeBlock.addInstruction(this.callLoggerJumpWrapper);
	}

	private void createGetVariableBlock( ) {
		for ( int i = 0; i <= this.levelCounter; i++ ) {
			this.codeBlock.addInstruction(this.variableStack.get(i));
		}
	}

	private void createIfBlock( final IdPartASTDecorator part ) {
		assert this.variableStack != null;
		assert this.levelCounter >= 0;
		assert this.levelCounter < this.variableStack.size();

		createGetVariableBlock();

		final JumpInsnNode jumpIfNotNull = new JumpInsnNode(
				Constants.IFNONNULL, this.lastJumpWrapper);

		this.codeBlock.addInstruction(jumpIfNotNull);

		final LdcWrapper refAsString = createRefAsString(part);

		this.codeBlock.addInstruction(refAsString);

		this.codeBlock.addInstruction(new VarInsnNode(Constants.ASTORE,
				this.tempLocalVarIdx));

		this.codeBlock.addInstruction(new JumpInsnNode(Constants.GOTO,
				this.callLoggerJumpWrapper));

		this.codeBlock.addInstruction(this.lastJumpWrapper);

		this.lastJumpWrapper = new LabelWrapper();
	}

	private LdcWrapper createRefAsString( final IdPartASTDecorator part ) {
		assert part != null;

		final String refPartIsNull = Template.partToStringWithPrefixDownFromNextChild(part
				.getWrappedVariableToken(), "NULL.");
		return new LdcWrapper(refPartIsNull);
	}

	private void addValueOfCode( final IdPartASTDecorator part ) {
		assert part != null;
		assert codeBlock != null;
		assert tempLocalVarIdx > -1;

		final VariableNode calledOnVariable = part.getAssignedVariableReferer()
				.getDefinition();

		final String type4Append = AsmUtils.StringBuffer
				.getTypeForValueOf(calledOnVariable);

		codeBlock.addInstruction(new MethodInsnNode(Constants.INVOKESTATIC,
				"java/lang/String", "valueOf", "(" + type4Append
						+ ")Ljava/lang/String;"));

		codeBlock.addInstruction(new VarInsnNode(Constants.ASTORE,
				tempLocalVarIdx));
	}

	@Override
	public void visit( final ThisIdPartASTDecorator part )
			throws CompileTimeWarningException {
		assert part != null;

		visitPart(part);
	}

	@Override
	public void visit( final NextIdPartASTDecorator part )
			throws CompileTimeWarningException {
		assert part != null;

		visitPart(part);
	}

	public InstructionGroup getCodeBlock( ) {
		assert codeBlock != null;

		return codeBlock;
	}

}
