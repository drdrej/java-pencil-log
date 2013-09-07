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
package com.j2biz.pencil.asm.logexpr;

import java.util.Map;

import org.objectweb.asm.Type;

import com.j2biz.pencil.asm.tree.LocalVariableDefinition;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.utils.MapFactory;

/**
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com
 */
public class LogMsgScope implements Scope {

	private final Map map;

	private int nextVariableIndex;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.Scope#getField(java.lang.String,
	 *      com.j2biz.pencil.asm.ScopeCaller)
	 */
	public VariableNode getVariable(String name, ScopeCaller caller) {
		return (VariableNode) this.map.get(name);
	}

	/**
	 * initialize an empty scope. this scope is used inside a code of a method
	 */
	public LogMsgScope(final int nextPossibleLocalVarIdx) {
		assertNextPossibleLocalVarIdx(nextPossibleLocalVarIdx);

		this.map = MapFactory.create();
		this.nextVariableIndex = nextPossibleLocalVarIdx;
		
	}

	/**
	 * initialize a scope with variables from another scope
	 */
	public LogMsgScope(final LogMsgScope pattern,
			final int nextPossibleLocalVarIdx) {
		assertNextPossibleLocalVarIdx(nextPossibleLocalVarIdx);
		assert (pattern != null) : "parameter:pattern must be not NULL.";

		this.map = MapFactory.create(pattern.map);
		this.nextVariableIndex = nextPossibleLocalVarIdx;
	}

	private void assertNextPossibleLocalVarIdx(final int nextPossibleLocalVarIdx) {
		assert (nextPossibleLocalVarIdx > -1) : "Next possible local variable index must be bigger than -1";
	}

	public void registerVariable(final LocalVariableDefinition variable) {
		if (variable == null) {
			throw new NullPointerException("parameter:variable");
		}

		this.map.put(variable.getName(), variable);
	}

	public void unregisterVariable(LocalVariableDefinition variable) {
		if (variable == null) {
			throw new NullPointerException("parameter:variable");
		}

		Object removedObj = this.map.remove(variable.getName());
		if (variable != removedObj) {
			throw new IllegalStateException(
					"the name is wrong binded. variable == "
							+ variable.getName());
		}
	}

	public static final LogMsgScope create(final LogMsgScope actualScope,
			final MethodInfoNode ownerMethod) {
		assert (ownerMethod != null) : "parameter:ownerMethod must be not NULL";
		assert (actualScope != null) : "parameter:actualScope must be not NULL.";
		
		final int nextPossibleVarIdx = ownerMethod.getVariableManager().getNextPossibleVariableIndex();
		final LogMsgScope rval = new LogMsgScope(actualScope, nextPossibleVarIdx);
		
		return rval;
	}
	
	public static final LogMsgScope createEmpty() {
		return new LogMsgScope(0);
	}

	public int createTempVariable() {
		final int rval = this.nextVariableIndex;
		this.nextVariableIndex += Type.getType(String.class).getSize();

		return rval;
	}

	public void destroyTempVariable() {
//		assert (this.nextVariableIndex > this.maxIdxOfLocalUsedVar) : "the lastVarIdx is to low. maybe to many destroys in the past ?";
//
//		this.nextVariableIndex--;
	}

}
