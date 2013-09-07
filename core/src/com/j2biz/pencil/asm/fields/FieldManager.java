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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.grlea.log.SimpleLogger;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Constants;
import org.objectweb.asm.Type;

import com.j2biz.pencil.asm.CLinitLogInitializationCreator;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.ex.ClassParseException;
import com.j2biz.pencil.msgKeys.StatusKeys;

public class FieldManager {

	private static final SimpleLogger LOG = new SimpleLogger(FieldManager.class);
	
	private final ASMClassInfoNode				ownerClass;

	private final List<FieldVariableDefinition>	fields	= new ArrayList<FieldVariableDefinition>();

	private boolean								isPrepared;

	public FieldManager( final ASMClassInfoNode ownerClassInfo ) {
		if ( ownerClassInfo == null ) {
			throw new NullPointerException("parameter:ownerClassInfo");
		}

		ownerClass = ownerClassInfo;
	}

	public void addField(
		final int accessFlags,
		final String name,
		final String description,
		final Object value,
		final Attribute attrs ) {

		checkEnablementToAddField(name, description);

		final FieldVariableDefinition node = new FieldVariableDefinition(
				accessFlags, name, description, value, attrs, getOwnerClass());

		this.fields.add(node);
	}

	private void checkEnablementToAddField( String name, String description ) {
		if ( isLoggerFieldName(name) ) {
			assert !isPrepared() : "Logger-field: " + name
					+ " is allready added. code has a bug in the flow.";

			if ( !isLoggerClass(description) )
				throw new ClassParseException(StatusKeys
						.getERR_LOGGER_STATIC_FIELD_EXIST(description));

			isPrepared = true;
		}
	}

	private boolean isLoggerFieldName( String name ) {
		return LogTransformer.LOGGER_FIELD_NAME.equals(name);
	}

	private boolean isLoggerClass( String description ) {
		return Type.getType(Log.class).getDescriptor().equals(description);
	}

	public ASMClassInfoNode getOwnerClass( ) {
		return this.ownerClass;
	}
	
	public Iterator<FieldVariableDefinition> iterator( ) {
		return this.fields.iterator();
	}

	public boolean isPrepared( ) {
		return this.isPrepared;
	}

	private void createLoggerField( ) {
		LOG.entry("createLoggerField( )");
		LOG.debugObject("this class need a logger", getOwnerClass());
		
		if ( getOwnerClass().shouldDeclareLogger() ) {
			LOG.debugObject("this class should has a logger-field", getOwnerClass());
			
			final int access = Constants.ACC_SYNTHETIC + Constants.ACC_PRIVATE
					+ Constants.ACC_FINAL + Constants.ACC_STATIC;

			this.addField(access, LogTransformer.LOGGER_FIELD_NAME,
					"Lorg/apache/commons/logging/Log;", null, null);
			
			getOwnerClass( ).addAdditionalCodeManipulation( new CLinitLogInitializationCreator(getOwnerClass()) );
		}
		
		LOG.exit("createLoggerField( )");
	}

	public void prepare( ) {
		assert !isPrepared() : "not allowed to call prepare()-method. class is allready prepared: "
				+ getOwnerClass().getClassName();

		createLoggerField();
	}

	public void prepareLoggerField( ) {
		if ( !isPrepared() ) 
			prepare();
	}

}
