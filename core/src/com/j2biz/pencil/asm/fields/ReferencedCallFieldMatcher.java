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

import org.objectweb.asm.Type;

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class ReferencedCallFieldMatcher extends AbstractFieldMatcher {

	/**
	 * on this instance is the matched field called.
	 */
	private final AbstractClassInfoNode fieldOwnerInstance;

	public ReferencedCallFieldMatcher(String fieldName,
			AbstractClassInfoNode callerClass, MethodInfoNode callerMethod,
			final VariableNode fieldOwnerVariable) throws CompileTimeWarningException {
		super(fieldName, callerClass, callerMethod);

		Assert.isNotNull(fieldOwnerVariable);

		fieldOwnerInstance = getOwnerClassNode(fieldOwnerVariable);

		if (fieldOwnerInstance == null) {
			throw new CompileTimeWarningException(
					"only instances of type OBJECT allow to call fields on it. bu the type of the variable ["
							+ fieldOwnerVariable.getName()
							+ "] is : "
							+ fieldOwnerVariable.getDescription());
		}
	}

	public AbstractClassInfoNode getOwnerClassNode(final VariableNode lastVar) {
		final ClassManager classManager = getCallerClass().getClassManager();
		final String internalName;

		final int typeSort = lastVar.getType().getSort();
		if (typeSort != Type.OBJECT)
			return null;

		if (typeSort == Type.ARRAY)
			Assert
					.shouldNeverReachHere("ARRAY is not supported in this version of pencil. variable == "
							+ lastVar.getName());

		internalName = lastVar.getType().getInternalName();

		return classManager.getClass(internalName);
	}

	@Override
	public boolean isVisible(AbstractClassInfoNode actualClassInfo,
			FieldVariableDefinition field) {
		// TODO: $DotCall.1$

		if (this.fieldOwnerInstance != field.getOwner()
				&& field.getOwner() == getCallerClass()
				&& field.getOwner().isSuperClassOf(this.fieldOwnerInstance))
			return !field.isPrivate;

		if (callerAndFieldOwnerAreTheSame(field))
			return true;

		if (field.getOwner() == this.fieldOwnerInstance
				&& this.fieldOwnerInstance.isSubClassOf(getCallerClass())
				&& this.fieldOwnerInstance.getRoot() == getCallerClass()
						.getRoot())
			return true;

		if (fieldOwnerIsEnclosedByCaller(field)) {
			if (ownerInstanceAndFieldOwnerAreTheSame(field)) {
				return true;
			} else {
				// >>> fieldOwnerExtendsOwnerInstance()
				return !field.isPrivate;
			}
		}

		if (callerExtendsFieldOwner(field)) {
			if (getCallerClass() == this.fieldOwnerInstance)
				return !field.isPrivate;
			else
				return field.isPublic;
		}

		if (field.getOwner().isSubClassOf(getCallerClass()))
			return !field.isPrivate;

		return field.isPublic;
	}

	private boolean callerExtendsFieldOwner(FieldVariableDefinition field) {
		return field.getOwner().isSuperClassOf(this.getCallerClass());
	}

	private boolean ownerInstanceAndFieldOwnerAreTheSame(
			FieldVariableDefinition field) {
		return field.getOwner() == this.fieldOwnerInstance;
	}

	private boolean fieldOwnerIsEnclosedByCaller(FieldVariableDefinition field) {
		return field.getOwner().isEnclosedInHierarchyBy(this.getCallerClass());
	}

	private boolean callerAndFieldOwnerAreTheSame(FieldVariableDefinition field) {
		return field.getOwner() == this.getCallerClass();
	}
}
