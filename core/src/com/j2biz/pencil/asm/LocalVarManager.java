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
package com.j2biz.pencil.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.CodeWriter;

import com.j2biz.pencil.asm.tree.LocalVariableDefinition;

public class LocalVarManager implements Acceptable {

	private final List<LocalVariableDefinition> localVarDefinitions = new ArrayList<LocalVariableDefinition>();

	public void addVariableDefinition(final LocalVariableDefinition newVariable) {
		Assert.isNotNull(newVariable);

		this.localVarDefinitions.add(newVariable);
	}

	public List localVars() {
		return Collections.unmodifiableList(this.localVarDefinitions);
	}

	public boolean hasDefinitions() {
		return !this.localVarDefinitions.isEmpty();
	}

	public void accept(CodeWriter writer) {
		for (LocalVariableDefinition definition : localVarDefinitions) {
			definition.accept(writer);
		}
	}

	public int getNextPossibleVariableIndex() {

		LocalVariableDefinition definitionWithHighestIdx = null;

		for (LocalVariableDefinition definition : localVarDefinitions) {
			definitionWithHighestIdx = definitionWithHighestIdx == null
					|| definition.index > definitionWithHighestIdx.index ? definition
					: definitionWithHighestIdx;
		}

		if( definitionWithHighestIdx == null )
			return 0;
		
		final int rval = definitionWithHighestIdx.index + definitionWithHighestIdx.getType().getSize(); 
		
		return rval;
	}

}
