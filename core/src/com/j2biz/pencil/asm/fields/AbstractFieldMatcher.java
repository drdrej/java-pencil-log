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

import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.MethodInfoNode;

public abstract class AbstractFieldMatcher implements FieldMatcher {

	private final AbstractClassInfoNode cc;

	protected final String n;

	private final MethodInfoNode cm;

	private final boolean isSC;

	/**
	 * is static method.
	 */
	protected final boolean isStaticMethod;

	public AbstractFieldMatcher(final String fieldName,
			final AbstractClassInfoNode callerClass,
			final MethodInfoNode callerMethod) {
		if (callerClass == null) {
			throw new NullPointerException("parameter:callerClass");
		}
		cc = callerClass;

		if (fieldName == null) {
			throw new NullPointerException("parameter:fieldName");
		}
		n = fieldName;

		if (callerMethod == null) {
			throw new NullPointerException("parameter:callerMethod");
		}
		cm = callerMethod;

		isSC = (cm.getAccess() & Constants.ACC_STATIC) == Constants.ACC_STATIC;

		isStaticMethod = isSC;
	}

	public AbstractClassInfoNode getCallerClass() {
		return this.cc;
	}

	public final boolean isStaticContext() {
		return isSC;
	}
	
	public boolean isStatic() {
		return isStaticMethod;
	}
	
	public abstract boolean isVisible(final AbstractClassInfoNode actualClassInfo,
			final FieldVariableDefinition field);

	public boolean matchField(final AbstractClassInfoNode actualClassInfo, final FieldVariableDefinition field) {
		return theSameName(field) && isVisible(actualClassInfo, field);
	}

	private boolean theSameName(final FieldVariableDefinition field) {
		return this.n.equals(field.getName());
	}

	public String getFieldName() {
		return this.n;
	}
	
	public MethodInfoNode getCallerMethod() {
		return this.cm;
	}
}
