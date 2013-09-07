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

public class EnclosingClassFieldMatcher extends AbstractFieldMatcher {

	private AbstractClassInfoNode enclosingClass;

	public EnclosingClassFieldMatcher(final AbstractClassInfoNode enclosingCallerClass, FieldMatcher matcher) {
		super(matcher.getFieldName(), matcher.getCallerClass(), matcher.getCallerMethod());
		
		if( enclosingCallerClass == null) {
			throw new NullPointerException( "parameter:enclosingCallerClass" );
		}
		enclosingClass = enclosingCallerClass;
	}

	private boolean checkStatic(final FieldVariableDefinition field) {
		if (isStaticMethod) {
			return (field.isStatic);
		} else {
			return true;
		}
	}

	public boolean isVisible(final AbstractClassInfoNode actualClassInfo,
			final FieldVariableDefinition field) {
		if (field.isPrivate) { 
			if (actualClassInfo == this.getCallerClass()) {
				return checkStatic(field);
			} else {
				return false;
			}
		} else {
			return checkStatic(field);
		}
	}
	
	public AbstractClassInfoNode getCallerClass() {
		return this.enclosingClass;
	}

}
