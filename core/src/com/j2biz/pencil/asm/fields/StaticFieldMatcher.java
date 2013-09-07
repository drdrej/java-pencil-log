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
package com.j2biz.pencil.asm.fields;

import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.MethodInfoNode;

public class StaticFieldMatcher implements FieldMatcher {

	private final FieldMatcher wrappedMatcher;

	public StaticFieldMatcher(final FieldMatcher matcher) {
		if( matcher == null) {
			throw new NullPointerException( "parameter:matcher" );
		}
		wrappedMatcher = matcher;
	}

	public boolean matchField(final AbstractClassInfoNode actualClassInfo,
			final FieldVariableDefinition field) {
		final boolean wrappedMatcherMatches = wrappedMatcher.matchField(actualClassInfo,
				field);
		
		if (wrappedMatcherMatches && field.isStatic) {
			return true;
		}

		return false;
	}

	public AbstractClassInfoNode getCallerClass() {
		return wrappedMatcher.getCallerClass();
	}

	public boolean isStatic() {
		return wrappedMatcher.isStatic();
	}

	public String getFieldName() {
		return this.wrappedMatcher.getFieldName();
	}
	
	public MethodInfoNode getCallerMethod() {
		return this.wrappedMatcher.getCallerMethod();
	}

}
