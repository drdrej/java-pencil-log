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
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.CodeVisitor;

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.asm.method.MethodAnalyzerVisitor;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.ex.ClassParseException;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class ClassAnalyzerVisitor implements ClassVisitor {

	private ASMClassInfoNode classInfo;

	private final ClassManager classManager;

	private final List maCache = new ArrayList();

	private final File fileSrc;

	public ClassAnalyzerVisitor(final ClassManager classManager,
			final File fileSource) {
		if (classManager == null) 
			throw new NullPointerException("parameter:classManager");
		
		this.classManager = classManager;

		if (fileSource == null) 
			throw new NullPointerException("parameter:fileSource");
		
		this.fileSrc = fileSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visit(int, java.lang.String,
	 *      java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void visit(int version, int access, String name, String superName,
			String[] interfaces, String sourceFile) {
		this.classInfo = new ASMClassInfoNode(version, access, name, superName,
				interfaces, sourceFile, this.classManager, this.fileSrc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitInnerClass(java.lang.String,
	 *      java.lang.String, java.lang.String, int)
	 */
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		this.classInfo.addInnerClassNode(name, outerName, innerName, access);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitField(int, java.lang.String,
	 *      java.lang.String, java.lang.Object, org.objectweb.asm.Attribute)
	 */
	public void visitField(int access, String name, String desc, Object value,
			Attribute attrs) {
		this.classInfo.getFieldManager().addField(access, name, desc, value, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String,
	 *      java.lang.String, java.lang.String[], org.objectweb.asm.Attribute)
	 */
	public CodeVisitor visitMethod(int access, String name, String desc,
			String[] exceptions, Attribute attrs) {
		final MethodInfoNode method = new MethodInfoNode(access, name,
				desc, exceptions, attrs, this.classInfo);
		
		this.classInfo.addMethod(method);

		if (!this.classInfo.isCompiled()) {
			final MethodAnalyzerVisitor analyzer = new MethodAnalyzerVisitor(
					method, this.classManager.getLogger());
			
			this.maCache.add(analyzer);

			return analyzer;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitAttribute(org.objectweb.asm.Attribute)
	 */
	public void visitAttribute(Attribute attr) {
		this.classInfo.addAttribute(attr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitEnd()
	 */
	public void visitEnd() {
		if (this.classInfo.isCompiled()) {
			return;
		}

		// prepare Methods:
		for (Iterator i = this.maCache.iterator(); i.hasNext();) {
			final MethodAnalyzerVisitor methodVisitor = (MethodAnalyzerVisitor) i
					.next();
			if (methodVisitor.hasLogCode()) {
				this.classInfo.setHasLog(true);
				methodVisitor.prepareLocals();
			}
			
			handleClassParseExceptions(methodVisitor);
		}

		this.maCache.clear();
	}

	private void handleClassParseExceptions( final MethodAnalyzerVisitor methodVisitor ) {
		final List<ClassParseException> exceptions = methodVisitor.getClassParseExceptions();
		
		for ( final Iterator j = exceptions.iterator(); j.hasNext(); ) {
		    final	ClassParseException exception = (ClassParseException) j.next();
		    
		    System.err.println("[PENCIL-WARNING] can't parse the class." );
		    System.err.println("\t "  + exception.getLocalizedMessage());
		    System.err.println("\t at " + classInfo + "." + exception.getAffectedMethod() +"("
					+ classInfo.getSource() + ":"
					+ exception.getLineNumber() + ")");
		    System.err.println();
		}
	}

	/**
	 * @return
	 */
	public ASMClassInfoNode getClassInfo() {
		return this.classInfo;
	}

}