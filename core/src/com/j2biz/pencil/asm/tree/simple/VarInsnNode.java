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
package com.j2biz.pencil.asm.tree.simple;

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.Acceptable;
import com.j2biz.pencil.asm.tree.InstructionGroup;

/**
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com
 */
public class VarInsnNode extends AbstractOpcodeNode implements Acceptable {

	private int	var;

	/**
	 * @param opcode
	 */
	public VarInsnNode( int opcode, int var ) {
		super(opcode);
		this.var = var;
	}

	/**
	 * @return
	 */
	public int getVar( ) {
		return this.var;
	}

	public void accept( CodeWriter codeWriter ) {
		codeWriter.visitVarInsn(this.getOpcode(), this.getVar());
	}

	public static void addLoadReferenceVariableCode(
		final int localVariableIndex,
		final InstructionGroup destinationGroup ) {
		
		assert (localVariableIndex > -1) : "local variable idx is never less than 0. idx == "
				+ localVariableIndex;

		assert (destinationGroup != null) : "destinationGroup must be not NULL.";
		
		
		destinationGroup.addInstruction( new VarInsnNode(Constants.ALOAD, localVariableIndex) );
	}

}
