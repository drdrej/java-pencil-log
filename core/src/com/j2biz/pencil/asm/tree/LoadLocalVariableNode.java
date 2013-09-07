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
package com.j2biz.pencil.asm.tree;

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Constants;
import org.objectweb.asm.Type;

import com.j2biz.pencil.asm.Acceptable;

/**
 * An instance of this class represents a local variable.
 * 
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class LoadLocalVariableNode  implements VariableDefinitionReferer, Node, Acceptable {
	
	private final LocalVariableDefinition definition;
    
    public LoadLocalVariableNode(final LocalVariableDefinition definition) {
		this.definition = definition;
    }

    /**
     * @return
     */
    private int getIndex() {
        return this.definition.index;
    }

	public void accept(CodeWriter codeWriter) {
		final Type varType = this.definition.getType();
		if (Type.BOOLEAN_TYPE.equals(varType)
				|| Type.BYTE_TYPE.equals(varType)
				|| Type.CHAR_TYPE.equals(varType)
				|| Type.INT_TYPE.equals(varType)
				|| Type.SHORT_TYPE.equals(varType)) {
			codeWriter.visitVarInsn(Constants.ILOAD, getIndex());
		} else if (Type.LONG_TYPE.equals(varType)) {
			codeWriter.visitVarInsn(Constants.LLOAD, getIndex());
		} else if (Type.DOUBLE_TYPE.equals(varType)) {
			codeWriter.visitVarInsn(Constants.DLOAD, getIndex());
		} else if (Type.FLOAT_TYPE.equals(varType)) {
			codeWriter.visitVarInsn(Constants.FLOAD, getIndex());
		} else {
			codeWriter.visitVarInsn(Constants.ALOAD, getIndex());
		}
	}

	public VariableNode getDefinition() {
		return this.definition;
	}

}
