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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Type;

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;

/**
 * @author Andreas Siebert
 */
public class ReflectionClassInfoNode extends AbstractClassInfoNode {

	/**
	 * internal name of the main-class (outer class)
	 */
	private final String encClassName;

	/**
	 * this flag is true, if this class is a class, which is enclosed by another class.
	 */
	private final boolean isEnclosedClass;
	
	/**
	 * Aggregated class.
	 */
	private final Class aClass;

	/**
	 * names of all interfaces which are inherited by the aggregated class.
	 */
	private final String[] itfNames;

	/**
	 * Name of the super-class of the aggregated class.
	 */
	private final String sName;
	
	/**
	 * the infos of the fields of the aggregated class.
	 */
	private final List fInfos;

	public ReflectionClassInfoNode(final Class aggregatedClass,
			final ClassManager manager) {
		// initialize internal classname & call super-constructor.
		super(aggregatedClass.getName().replace('.', '/'), manager);

		if (aggregatedClass == null) {
			throw new NullPointerException("parameter:aggregatedClass");
		}

		// set class
		aClass = aggregatedClass;

		// initialize interfaces
		final Class[] interfaceClasses = aClass.getInterfaces();
		final int interfaceCount = interfaceClasses.length;
		itfNames = new String[interfaceCount];
		for (int i = 0; i < interfaceClasses.length; i++) {
			itfNames[i] = interfaceClasses[i].getName().replace('.', '/');
		}

		// initialize the name of the super-class
		final Class sClass = aggregatedClass.getSuperclass();
		if (sClass != null) {
			sName = sClass.getName().replace('/', '.');
		} else {
			sName = null;
		}
		
		// initialize fields 
		final Field[] fields = aClass.getFields();
		final int fCount = fields.length;
		final FieldVariableDefinition[] fieldInfos = new FieldVariableDefinition[ fCount ];
		for (int i = 0; i < fCount; i++) {
			Field field = fields[i];
			// Verbesserung: einen eigenen typ einführen!
			fieldInfos[i] = new FieldVariableDefinition(field.getModifiers(), field.getName(),  Type.getDescriptor( field.getType()), null, null, this );
		}
		this.fInfos = Arrays.asList(fieldInfos);
		
		// main-class:
		final Class encClass = aggregatedClass.getEnclosingClass();
		if( encClass == null ) {
			encClassName = "";
			isEnclosedClass = false;
		} else {
			encClassName = encClass.getName().replace('/', '.');
			isEnclosedClass = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getInterfaces()
	 */
	public String[] getInterfaces() {
		return itfNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getJavaClassName()
	 */
	public String getJavaClassName() {
		return aClass.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getSuperClassName()
	 */
	public String getSuperClassName() {
		return sName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getJavaSource()
	 */
	public String getJavaSource() {
		throw new UnsupportedOperationException(
				"the source-code of the classes in the classLoader is unknown. class == "
						+ getClassName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getSuperClassInfo()
	 */
	public AbstractClassInfoNode getSuperClassInfo() {
		final ClassManager manager = getClassManager();
		if( sName == null ) {
			return null;
		}
		
		return manager.getClass(sName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#getAccessFlags()
	 */
	public int getAccessFlags() {
		// asm-flags has the same values
		return aClass.getModifiers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2biz.pencil.asm.tree.ClassInfoNode#fields()
	 */
	public Iterator fields() {
		return fInfos.iterator();
	}

	/**
	 * @return true if the aggregated class is an inner, 
	 *                member or annonymous class. 
	 *                (some sort of enclosed classes)
	 */
	public boolean isInnerClass() {
		return this.isEnclosedClass;
	}

	public String getEnclosingClassClassName() {
		return this.encClassName;
	}
	
	public boolean isLocalClass() {
		return this.aClass.getEnclosingMethod() != null;
	}
	
	public boolean isNestedClass() {
		return isEnclosedClass && (this.aClass.getModifiers() & Modifier.STATIC )!= 0;
	}

	public boolean isInterface() {
		return this.aClass.isInterface();
	}
}
