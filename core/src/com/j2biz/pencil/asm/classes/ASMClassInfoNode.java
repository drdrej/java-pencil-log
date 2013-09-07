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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Constants;
import org.objectweb.asm.attrs.EnclosingMethodAttribute;

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.asm.CodeTransformation;
import com.j2biz.pencil.asm.LdcWrapper;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.Transformer;
import com.j2biz.pencil.asm.fields.FieldManager;
import com.j2biz.pencil.asm.fields.FieldMatcher;
import com.j2biz.pencil.asm.fields.SimpleFieldMatcher;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.InnerClassNode;
import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.ModifiableClassInfoNode;
import com.j2biz.pencil.asm.tree.simple.JumpInsnNode;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.ex.LoggerNotDefinedException;
import com.j2biz.utils.ListFactory;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class ASMClassInfoNode extends AbstractClassInfoNode implements
		ModifiableClassInfoNode {

	private String encClassName;

	private boolean isEnclosedByClass;

	private final int version;

	private final int accessFlags;

	/**
	 * name of this class in the java-syntax.
	 */
	private final String jName;

	private final String superInternalName;

	private final List methods = new ArrayList();

	private final List innerClassNodes = new ArrayList();

	final FieldManager fieldManager;

	private Attribute attr;

	/**
	 * internal names of the interfaces, implemented by this class.
	 */
	private final String[] interfaces;

	/**
	 * relative path to the source-file.
	 */
	private final String sourceFile;

	private final File resource;

	/**
	 * this flag is true, if one of methods contains a log-entry.
	 */
	private boolean hasLogEntry = false;

	private boolean isEnclosedByMethod;

	private boolean isNestedClass;

	private byte[] newContent;

	private List<CodeTransformation>	manipulator = ListFactory.create();
	
	public void addAdditionalCodeManipulation(final CodeTransformation transformation) {
		assert transformation != null;
		
		this.manipulator.add( transformation);
	}
	
	public List<CodeTransformation> transformations() {
		return Collections.unmodifiableList(this.manipulator);
	}
	
	public void addMethod(final MethodInfoNode methodInfo) {
		methods.add(methodInfo);
	}

	/**
	 * All names are internal names in this context. I.e. the names containig a
	 * not dot-delimiter but slash-delimiter.
	 * 
	 * @param version
	 *            is the version of a class which one is represented by this
	 *            instance
	 * @param access
	 *            PUBLIC, STATIC an other Java-access-flags.
	 * @param internalName
	 *            never NULL
	 * @param superName
	 *            never NULL.
	 * @param interfaces
	 *            never NULL.
	 * @param sourceFile
	 *            NULL-value is allowed.
	 * 
	 * @throws NullPointerException
	 *             if one of the parameter is NULL.
	 */
	public ASMClassInfoNode(final int version, final int access,
			final String internalName, final String superName,
			final String[] interfaces, final String sourceFile,
			final ClassManager parentClassManager, final File resource) {
		super(internalName, parentClassManager);
		this.version = version;
		this.accessFlags = access;

		jName = getClassName().replace('/', '.');

		this.superInternalName = superName;

		if (interfaces == null) {
			throw new NullPointerException("parameter:interfaces");
		}
		this.interfaces = interfaces;

		this.sourceFile = sourceFile;

		if (resource == null) {
			throw new NullPointerException("parameter:resource");
		}
		this.resource = resource;

		this.fieldManager = new FieldManager(this);
	}

	public File getResource() {
		return this.resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getMainClassName()
	 */
	public AbstractClassInfoNode getSuperClassInfo()
			{
		if (this.superInternalName == null) {
			return null;
		}
		return manager.getClass(this.superInternalName);
	}
	

	public String getSuperClassName() {
		return this.superInternalName;
	}

	public String[] getInterfaces() {
		return this.interfaces;
	}

	public String getJavaSource() {
		return this.sourceFile;
	}

	public int getAccessFlags() {
		return this.accessFlags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getJavaClassName()
	 */
	public String getJavaClassName() {
		return jName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ModifiableClassInfoNode#isCompiled()
	 */
	public boolean isCompiled() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ModifiableClassInfoNode#addField(java.lang.String,
	 *      java.lang.String, int, org.objectweb.asm.Attribute)
	 */
	public void addField(int accessFlags, String name, String description,
			Object value, Attribute attrs) {
		this.getFieldManager().addField(accessFlags, name, description, value,
				attrs);
	}

	public FieldManager getFieldManager() {
		return this.fieldManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ModifiableClassInfoNode#modify(com.j2biz.pencil.asm.Transformer)
	 */
	public byte[] modify(Transformer transformer) {
		if (this.newContent == null) 
			this.newContent = transformer.start(this);
		

		return this.newContent;
	}

	/**
	 * @return
	 */
	public Iterator methods() {
		return this.methods.iterator();
	}

	public Iterator innerClassNodes() {
		return this.innerClassNodes.iterator();
	}

	public void addInnerClassNode(final String name, final String outerName,
			final String innerName, final int access) {
		InnerClassNode node = new InnerClassNode(name, outerName, innerName,
				access);
		this.innerClassNodes.add(node);
		if (isRealInnerClass(name, outerName)) {
			markItAsEnclosedByClass(outerName, access);
		}
	}

	private void markItAsEnclosedByClass(final String outerName,
			final int access) {
		encClassName = outerName;
		isEnclosedByClass = true;
		isNestedClass = (access & Constants.ACC_STATIC) == Constants.ACC_STATIC;
	}

	private boolean isRealInnerClass(final String name, final String outerName) {
		return name.equals(getClassName()) && outerName != null;
	}

	public void addAttribute(Attribute attr) {
		if (attr instanceof EnclosingMethodAttribute) {
			EnclosingMethodAttribute emAttr = ((EnclosingMethodAttribute) attr);
			encClassName = emAttr.owner;
			this.isEnclosedByMethod = true;
		}

		attr.next = this.attr;
		this.attr = attr;
	}

	public boolean isNestedClass() {
		return isEnclosedByClass && isNestedClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ModifiableClassInfoNode#fields()
	 */
	public Iterator fields() {
		return this.getFieldManager().iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ModifiableClassInfoNode#getFirstAttribute()
	 */
	public Attribute getFirstAttribute() {
		return this.attr;
	}

	/**
	 * @param hasLog
	 *            is true, if one LogEntry exists
	 */
	public void setHasLog(boolean hasLog) {
		this.hasLogEntry |= hasLog;
	}

	/**
	 * @return if one or more log-entries exists.
	 */
	public boolean hasLogEntries() {
		return this.hasLogEntry;
	}

	public int getVersion() {
		return this.version;
	}

	public boolean isInnerClass() {
		return this.isEnclosedByClass;
	}

	public String getEnclosingClassClassName() {
		return this.encClassName;
	}

	public boolean isLocalClass() {
		return this.isEnclosedByMethod;
	}

	public AbstractClassInfoNode getLoggerOwner(LogTransformer transformer) {
		final String encClassName = getEnclosingClassClassName();
		if (encClassName == null) {
			return this;
		} else {
			return this.getClassManager().getClass(encClassName);
		}
	}

	@Override
	public FieldVariableDefinition analyseFields(final FieldMatcher matcher) {
		if (LogTransformer.LOGGER_FIELD_NAME.equals(matcher.getFieldName())
				&& !this.getFieldManager().isPrepared()) {
			
			this.getFieldManager().prepare();
//			this.prepareClinitMethod();
		}

		return super.analyseFields(matcher);
	}
	
	public FirstPartFieldRefInstruction getLoggerFieldRef(
			final MethodInfoNode callerMethod) {
		
		final FirstPartFieldRefInstruction fieldRef = new FirstPartFieldRefInstruction(
				this);
		
		final FieldMatcher matcher = new SimpleFieldMatcher(
				LogTransformer.LOGGER_FIELD_NAME, this, callerMethod);

		final FieldVariableDefinition field = findField(matcher, true, fieldRef,
				matcher.isStatic());

		if (field == null) 
			throw new LoggerNotDefinedException("in the class : "
					+ getClassName() + " is no logger-field defined.");

		return fieldRef;
	}

	public InstructionGroup addSimpleTextLogMsg(LogTransformer transformer,
			final MethodInfoNode method, final LogMessageNode logEntry) {
		final InstructionGroup rval = new InstructionGroup();

		rval.addInstruction(getLoggerFieldRef(method));
		rval.addInstruction(new MethodInsnNode(Constants.INVOKEINTERFACE,
				"org/apache/commons/logging/Log", logEntry.getIsEnabledName(),
				"()Z"));
		LabelWrapper l0 = LabelWrapper.create();
		rval.addInstruction(new JumpInsnNode(Constants.IFEQ, l0
				.getWrappedNode()));
		rval.addInstruction(getLoggerFieldRef(method));
		rval.addInstruction(new LdcWrapper(logEntry.getTemplate()));
		rval.addInstruction(logEntry.createIsDebugEnabledCall());
		rval.addInstruction(l0);
		return rval;
	}

	@Override
	public boolean isInterface() {
		return (this.accessFlags & Constants.ACC_INTERFACE) == Constants.ACC_INTERFACE;
	}

	public String getSource( ) {
		return this.sourceFile;
	}

}
