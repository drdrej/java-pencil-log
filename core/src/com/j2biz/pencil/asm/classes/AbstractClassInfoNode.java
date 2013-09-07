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
package com.j2biz.pencil.asm.classes;

import java.util.Iterator;

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.JavaHelper;
import com.j2biz.pencil.asm.fields.EnclosingClassFieldMatcher;
import com.j2biz.pencil.asm.fields.FieldMatcher;
import com.j2biz.pencil.asm.fields.StaticFieldMatcher;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;

/**
 * @author Andreas Siebert
 */
public abstract class AbstractClassInfoNode {

	public final ClassManager manager;

	/**
	 * internal name of the aggregated class.
	 */
	private final String iName;

	public abstract boolean isInnerClass();

	public abstract Iterator fields();

	public abstract String getEnclosingClassClassName();

	public abstract boolean isLocalClass();

	public abstract boolean isNestedClass();

	/**
	 * 
	 */
	public AbstractClassInfoNode(final String internalName,
			final ClassManager classManager) {
		if (internalName == null) {
			throw new NullPointerException("parameter:internalName");
		}
		iName = internalName;

		if (classManager == null) {
			throw new NullPointerException("parameter:classManager");
		}

		manager = classManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getClassName()
	 */
	public String getClassName() {
		return iName;
	}

	public ClassManager getClassManager() {
		return manager;
	}

	public final FieldVariableDefinition scanSuperClass(
			final FieldMatcher matcher, boolean callerIsStatic) {
		final AbstractClassInfoNode superClassInfo = this
				.getSuperClassInfo();
		if (hasSuperClass()) {
			final FieldVariableDefinition field = superClassInfo
					.analyseFields(matcher);
			if (field != null) {
				return field;
			} else {
				return superClassInfo.scanHierarchy(matcher, callerIsStatic);
			}
		}

		return null;
	}

	public abstract AbstractClassInfoNode getSuperClassInfo();

	public abstract String getSuperClassName();

	private boolean hasSuperClass() {
		return this.getSuperClassName() != null
				&& !this.getSuperClassName().equals("java/lang/Object");
	}

	public abstract String[] getInterfaces();

	/**
	 * @param name
	 * @param classInfo
	 * @param callerClass
	 * @param callerMethod
	 * @param logger
	 * @return
	 */
	public FieldVariableDefinition scanInterfaces(final FieldMatcher matcher,
			boolean callerIsStatic) {
		final String[] ifs = this.getInterfaces();
		final ClassManager classManager = this.getClassManager();

		FieldVariableDefinition field = null;
		for (int i = 0; i < ifs.length; i++) {
			field = matchField(matcher, callerIsStatic, classManager, ifs[i]);
			if (field == null) {
				continue;
			}
		}

		return field;
	}

	private FieldVariableDefinition matchField(final FieldMatcher matcher,
			boolean callerIsStatic, final ClassManager classManager,
			final String ifName) {

		final AbstractClassInfoNode ifNode = classManager.getClass(ifName);

		FieldVariableDefinition field = ifNode.analyseFields(matcher);

		if (field != null) {
			return field;
		} else {
			return ifNode.scanInterfaces(matcher, callerIsStatic);
		}
	}

	private FieldVariableDefinition scanHierarchy(final FieldMatcher matcher,
			boolean callerIsStatic) {

		FieldVariableDefinition rval = this.scanInterfaces(matcher,
				callerIsStatic);

		if (rval == null) {
			rval = this.scanSuperClass(matcher, callerIsStatic);
		}

		return rval;
	}

	public FieldVariableDefinition findField(final FieldMatcher matcher,
			final boolean firstPart,
			final FirstPartFieldRefInstruction fieldRef,
			final boolean isStaticScope) {

		final boolean callerIsStatic = matcher.isStatic() || isStaticScope;

		FieldVariableDefinition rval = this.analyseFields(matcher);
		if (rval == null) {
			rval = this.scanHierarchy(matcher, callerIsStatic);
		}

		if (rval != null) {
			JavaHelper.addOwnFieldInstructions(rval, firstPart, fieldRef);

			return rval;
		}

		return selectEnclosingClassScan(matcher, fieldRef, callerIsStatic, rval);
	}

	private FieldVariableDefinition selectEnclosingClassScan(
			final FieldMatcher matcher,
			final FirstPartFieldRefInstruction fieldRef,
			final boolean callerIsStatic, FieldVariableDefinition rval) {
		if (this.isInnerClass()) {
			if (this.isNestedClass()) {
				rval = scanEnclosingClass(new StaticFieldMatcher(
						getEnclosingMatcher(matcher)), fieldRef, true);
			} else {
				rval = scanEnclosingClass(getEnclosingMatcher(matcher),
						fieldRef, callerIsStatic);
			}
		} else if (this.isLocalClass()) {
			rval = scanEnclosingClass(new StaticFieldMatcher(
					getEnclosingMatcher(matcher)), fieldRef, true);
		}
		return rval;
	}

	private FieldVariableDefinition scanEnclosingClass(
			final FieldMatcher encMatcher,
			final FirstPartFieldRefInstruction fieldRef,
			final boolean callerIsStatic) {
		final FieldVariableDefinition field = encMatcher.getCallerClass()
				.findField(encMatcher, true, fieldRef, callerIsStatic);

		if (field != null) {
			fieldRef.prepareAfterEnclosing(encMatcher.getCallerClass(), field);
		}
		return field;

	}

	private EnclosingClassFieldMatcher getEnclosingMatcher(
			final FieldMatcher matcher) {
		final AbstractClassInfoNode outerClass = getEnclosingClass();
		final EnclosingClassFieldMatcher encMatcher = new EnclosingClassFieldMatcher(
				outerClass, matcher);
		return encMatcher;
	}

	private AbstractClassInfoNode getEnclosingClass() {
		final String encClassName = getEnclosingClassClassName();
		if (encClassName == null)
			return null;

		return this.getClassManager().getClass(encClassName);
	}

	public FieldVariableDefinition findThisField(FieldMatcher matcher) {
		FieldVariableDefinition rval = this.analyseFields(matcher);
		if (rval == null) {
			rval = scanHierarchy(matcher, matcher.isStatic());
		}
		return rval;
	}

	public final boolean shouldDeclareLogger() {
		return !isEnclosedByClass();
	}

	public FieldVariableDefinition analyseFields(final FieldMatcher matcher) {
		for (Iterator i = this.fields(); i.hasNext();) {
			final FieldVariableDefinition field = (FieldVariableDefinition) i
					.next();
			if (matcher.matchField(this, field)) {
				return field;
			}
		}

		return null;
	}

	public abstract String getJavaClassName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return getJavaClassName() + " (id: " + System.identityHashCode( this ) + ")";
	}

	@Override
	public boolean equals(Object obj) {
		AbstractClassInfoNode cl = (AbstractClassInfoNode) obj;
		if (cl == this)
			return true;

		if (this.getClassName() != null)
			return this.getClassName().equals(cl.getClassName());
		else
			return cl.getClassName().equals(this.getClassName());
	}

	@Override
	public int hashCode() {
		return this.getClassName().hashCode();
	}

	public boolean isEnclosedInHierarchyBy(
			AbstractClassInfoNode maybeParentClass) {
		Assert.isTheSame(this, maybeParentClass);

		return isEnclosedBy(maybeParentClass);
	}

	private boolean isEnclosedBy(AbstractClassInfoNode maybeParentClass) {
		if (!isEnclosed())
			return false;

		final AbstractClassInfoNode encClass = getEnclosingClass();

		if (encClass == maybeParentClass)
			return true;

		return encClass.isEnclosedBy(maybeParentClass);
	}

	private boolean isEnclosed() {
		return isEnclosedByClass() || isEnclosedByMethod();
	}

	private boolean isEnclosedByMethod() {
		return isLocalClass();
	}

	private boolean isEnclosedByClass() {
		return isInnerClass();
	}

	public boolean isSubClassOf(AbstractClassInfoNode maybeSuperClass) {
		Assert.isNotNull(maybeSuperClass);

		return implementsIt(maybeSuperClass) || extendsIt(maybeSuperClass);
	}

	public boolean isSuperClassOf(AbstractClassInfoNode maybeSubClass) {
		Assert.isNotNull(maybeSubClass);
		return maybeSubClass.isSubClassOf(this);
	}

	private boolean extendsIt(AbstractClassInfoNode maybeSuperClass) {
		Assert.isNotNull(maybeSuperClass);

		if (maybeSuperClass.isInterface())
			return false;

		AbstractClassInfoNode superClass = this.getSuperClassInfo();
		if (superClass == null)
			return false;

		if (superClass == maybeSuperClass)
			return true;

		return superClass.isSubClassOf(maybeSuperClass);
	}

	public abstract boolean isInterface();

	private boolean implementsIt(AbstractClassInfoNode maybeInterfaceClass) {
		Assert.isNotNull(maybeInterfaceClass);

		if (!maybeInterfaceClass.isInterface())
			return false;

		final String[] infts = this.getInterfaces();

		for (int i = 0; i < infts.length; i++) {
			final AbstractClassInfoNode intfClass = getClassManager().getClass(
					infts[i]);

			if (maybeInterfaceClass == intfClass)
				return true;

			if (intfClass.implementsIt(maybeInterfaceClass))
				return true;
		}

		return false;
	}

	public AbstractClassInfoNode getRoot() {
		AbstractClassInfoNode encClass = getEnclosingClass();
		if (encClass != null)
			return encClass.getRoot();

		return this;
	}
	
	/**
	 * 
	 * @return internal package name
	 */
	public String getPackageName() {
		final int pos = getClassName().lastIndexOf('/');
		if( pos < 0 )
			return "";
		
		return getClassName().substring(0, pos);
	}

}
