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
package com.j2biz.pencil.asm;

import org.objectweb.asm.Type;

import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.fields.FieldMatcher;
import com.j2biz.pencil.asm.fields.SimpleFieldMatcher;
import com.j2biz.pencil.asm.fields.StaticFieldMatcher;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.ex.CompileTimeWarningException;

/**
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com
 */
public class JavaHelper {

	public static final AbstractClassInfoNode checkInheritance(
		final AbstractClassInfoNode callerClass,
		final VariableNode castedVariable,
		final String castedTo,
		final ErrorStatusLogger logger ) throws CompileTimeWarningException {
		// evtl. in die VariableNode Klasse verschieben.
		final Type type = castedVariable.getType();
		final String internalClassName = type.getInternalName();
		if ( type.getSort() == Type.ARRAY ) {
			throw new CompileTimeWarningException(
					"variable can't be casted, cause it is an array. this type of a cast is not supported in this version.");
		}

		if ( type == Type.BYTE_TYPE || type == Type.BOOLEAN_TYPE
				|| type == Type.CHAR_TYPE || type == Type.DOUBLE_TYPE
				|| type == Type.FLOAT_TYPE || type == Type.INT_TYPE
				|| type == Type.LONG_TYPE || type == Type.SHORT_TYPE ) {
			throw new CompileTimeWarningException(
					"variable can't be casted, cause it is a simple type. simple types are not castable in java.");
		}

		// TODO: verschiebe in getOwner() der LocalVar und des Feldes...
		final ClassManager manager = callerClass.getClassManager();
		final AbstractClassInfoNode classChild = manager
				.getClass(internalClassName);

		final AbstractClassInfoNode castedToNode = manager.getClass(castedTo);

		if ( castedToNode == callerClass || castedToNode == classChild ) {
			return castedToNode;
		}

		return checkInheritance(classChild, castedToNode, logger);
	}

	/**
	 * @param childNode
	 * @param parentClassName
	 *            Name of the class, which is a parent class/interface of the
	 *            childNode. NULL-Values are not allowed.
	 * @return parent-node of the childClass as instance of type ClassNodeInfo.
	 *         NULL, if this Class is not parent or the class can't be loaded...
	 *         (todo:warning)
	 */
	private static final AbstractClassInfoNode checkInheritance(
		final AbstractClassInfoNode childNode,
		final AbstractClassInfoNode castedToNode,
		final ErrorStatusLogger logger ) {
		if ( childNode == null ) {
			Assert.shouldNeverReachHere("cant' casted to " + castedToNode
					+ " cause childNode is NULL.");
		}

		boolean rval = checkInterfaceInheritance(childNode, castedToNode,
				logger);
		if ( rval ) {
			return castedToNode;
		}

		rval = checkSuperClassInheritance(childNode, castedToNode, logger);
		if ( rval ) {
			return castedToNode;
		}

		return null;
	}

	private static final boolean checkSuperClassInheritance(
		final AbstractClassInfoNode childNode,
		final AbstractClassInfoNode parentClass,
		final ErrorStatusLogger logger ) {
		final AbstractClassInfoNode nextChildNode = childNode
				.getSuperClassInfo();

		if ( nextChildNode == null )
			return false;

		if ( parentClass == nextChildNode ) {
			return true;
		} else {
			return checkInheritance(nextChildNode, parentClass, logger) != null;
		}
	}

	private static final boolean checkInterfaceInheritance(
		final AbstractClassInfoNode childNode,
		final AbstractClassInfoNode parentNode,
		final ErrorStatusLogger logger ) {

		final ClassManager manager = childNode.getClassManager();

		final String[] ifs = childNode.getInterfaces();
		for ( int i = 0; i < ifs.length; i++ ) {
			final String ifName = ifs[i];
			AbstractClassInfoNode ifNode = manager.getClass(ifName);

			if ( ifNode == null )
				return false;

			if ( parentNode == ifNode ) {
				return true;
			} else {
				boolean founded = checkInterfaceInheritance(ifNode, parentNode,
						logger);
				if ( founded ) {
					return true;
				}
			}

		}

		return false;
	}

	public static final FirstPartFieldRefInstruction getField(
		final String name,
		final AbstractClassInfoNode classInfo,
		final AbstractClassInfoNode callerClass,
		final MethodInfoNode callerMethod,
		final boolean firstPart,
		final boolean isStaticScope ) {
		
		final FieldMatcher matcher;
		if ( !isStaticScope ) {
			matcher = new SimpleFieldMatcher(name, callerClass, callerMethod);
		} else {
			matcher = new StaticFieldMatcher(new SimpleFieldMatcher(name,
					callerClass, callerMethod));
		}

		final FirstPartFieldRefInstruction fieldRef = new FirstPartFieldRefInstruction();
		FieldVariableDefinition field = classInfo.findField(matcher, firstPart,
				fieldRef, isStaticScope);
		if ( field != null ) {
			return fieldRef;
		} else {
			return null;
		}
	}

	public static FirstPartFieldRefInstruction addOwnFieldInstructions(
		final FieldVariableDefinition field,
		final boolean firstPart,
		final FirstPartFieldRefInstruction fieldRef ) {

		final FirstPartFieldRefInstruction rval = fieldRef;

		if ( !field.isStatic ) {
			if ( firstPart ) {
				rval.setAload0();
			}
		}

		rval.setField(field);
		return rval;
	}

}