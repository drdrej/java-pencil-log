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
import org.objectweb.asm.Label;

import com.j2biz.pencil.asm.Assert;

public class LocalVariableDefinition extends VariableNode {

    private final Label scopeStart;
    private final Label scopeEnd;
    public final int index;
	private final LoadLocalVariableNode loadVarNode;
	
	public LocalVariableDefinition(final String name, final String description, final Label start, final Label end, final int index) {
		super(name, description);

		Assert.isNotNull(name);
		Assert.isNotNull(description);
		Assert.isNotNull(start);
		Assert.isNotNull(end);
		Assert.indexIsNotLessThanZero(index);
		
		this.scopeStart = start;
        this.scopeEnd = end;
		
        this.index = index;
		loadVarNode = new LoadLocalVariableNode(this);
	}

	public void accept(CodeWriter codeWriter) {
		codeWriter.visitLocalVariable(getName(), getDescription(), scopeStart, scopeEnd, index);
	}
	
	public LoadLocalVariableNode getLoadVariableNode() {
		return loadVarNode;
	}

}
