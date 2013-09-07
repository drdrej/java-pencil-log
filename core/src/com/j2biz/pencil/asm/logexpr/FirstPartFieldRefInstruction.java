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

import java.util.List;

import javolution.util.FastList;

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.VariableDefinitionReferer;

public class FirstPartFieldRefInstruction implements Node, VariableDefinitionReferer   {
   
	private AbstractClassInfoNode callerClass;
	
	private boolean aload0 = false;

	/**
	 * enclosing scope-calls.
	 * all classnames, which contains a this$x field
	 * to get needed scope.
	 */
	private List<AbstractClassInfoNode> scopes = new FastList();
	
	private FieldVariableDefinition field;

	
	public FirstPartFieldRefInstruction(final AbstractClassInfoNode callerClass) {
		Assert.isNotNull(callerClass);
		this.callerClass = callerClass;
	}
	
	public FirstPartFieldRefInstruction() {
		this.callerClass = null;
	}
	
	public void setAload0() {
		this.aload0 = true;
	}
	
	
	public void setField( FieldVariableDefinition field) {
		if( field == null ) {
			throw new NullPointerException( "parameter:field" );
		}
		
		this.field = field;
	}
	
	public void addEnclosingScope(final AbstractClassInfoNode scope) {
		if( scope == null ){
			throw new NullPointerException("parameter:scope");
		}
		
		this.scopes.add(scope);
	}


	public FieldVariableDefinition getField() {
		return this.field;
	}
	
	public FieldVariableDefinition getDefinition() {
		return this.field;
	}

	public boolean hasAload0() {
		return this.aload0;
	}


	public List<AbstractClassInfoNode> getScopes() {
		return this.scopes;
	}


	public void prepareAfterEnclosing(final AbstractClassInfoNode outerClass, final FieldVariableDefinition field) {
		if (!field.isStatic) {
			addEnclosingScope(outerClass);
		}
		
		setField(field);
	}


	public void accept(CodeWriter codeWriter, LogTransformer transformer) {
		final FieldVariableDefinition field = getField();
		if ((field.getAccessFlags() & Constants.ACC_STATIC) == Constants.ACC_STATIC) {
			codeWriter.visitFieldInsn(Constants.GETSTATIC, field.getOwner()
					.getClassName(), field.getName(), field
					.getDescription());
		} else {
			if (hasAload0()) {
				codeWriter.visitVarInsn(Constants.ALOAD, 0);
			}
	
			List<AbstractClassInfoNode> scopes = getScopes();
	
			int i = scopes.size() - 1;
	
			// TODO: falls folgendes Beispiel kommt: ${field.variable}
			// dann muss man diesen Abschnitt anders implementieren.
			// man sollte hier die entsprechende Klasse laden.
			AbstractClassInfoNode prevCl = transformer.modifiedClass;
	
			for (AbstractClassInfoNode cln : scopes) {
				codeWriter.visitFieldInsn(Constants.GETFIELD, prevCl
						.getClassName(), "this$" + i, "L"
						+ cln.getClassName() + ";");
			}
	
			codeWriter.visitFieldInsn(Constants.GETFIELD, field.getOwner()
					.getClassName(), field.getName(), field
					.getDescription());
		}
	}
	
	public static final FirstPartFieldRefInstruction createFirstPartReference(final FieldVariableDefinition field) {
		return create(field, true);
	}
	
	
	private static final FirstPartFieldRefInstruction create(final FieldVariableDefinition field, final boolean firstPart) {
		FirstPartFieldRefInstruction rval = new FirstPartFieldRefInstruction();
		
		if ( !field.isStatic) {
			if (firstPart) {
				rval.setAload0();
			}
		}

		rval.setField(field);
		return rval;
	}

	public void accept(CodeWriter codeWriter) {
		final FieldVariableDefinition field = getField();
		if ((field.getAccessFlags() & Constants.ACC_STATIC) == Constants.ACC_STATIC) {
			codeWriter.visitFieldInsn(Constants.GETSTATIC, field.getOwner()
					.getClassName(), field.getName(), field
					.getDescription());
		} else {
			if (hasAload0()) {
				codeWriter.visitVarInsn(Constants.ALOAD, 0);
			}
	
			List<AbstractClassInfoNode> scopes = getScopes();
	
			int i = scopes.size() - 1;
	
			// TODO: falls folgendes Beispiel kommt: ${field.variable}
			// dann muss man diesen Abschnitt anders implementieren.
			// man sollte hier die entsprechende Klasse laden.
			AbstractClassInfoNode prevCl = this.callerClass;
	
			for (AbstractClassInfoNode cln : scopes) {
				codeWriter.visitFieldInsn(Constants.GETFIELD, prevCl
						.getClassName(), "this$" + i, "L"
						+ cln.getClassName() + ";");
			}
	
			codeWriter.visitFieldInsn(Constants.GETFIELD, field.getOwner()
					.getClassName(), field.getName(), field
					.getDescription());
		}
	}

	public static FirstPartFieldRefInstruction createNextPartReference(FieldVariableDefinition field) {
		return create(field, false);
	}
}
