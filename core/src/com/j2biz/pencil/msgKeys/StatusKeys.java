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
package com.j2biz.pencil.msgKeys;

/**
 * This class contains message keys, used for internal logging.
 * 
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com
 */
public final class StatusKeys {

	/**
	 * initialization outside of the class is not allowed. this class should has
	 * no instances.
	 */
	private StatusKeys( ) {
	}

	/**
	 * situation: a classInfo is loaded from the ClassLoader of the compiler. It
	 * is not so good, cause the CompilerClassLoader.
	 */
	public static final String	LOAD_CLASS_FROM_COMPILER_CLASSLOADER	= "LOAD_CLASS_FROM_COMPILER_CLASSLOADER";

	public static final String	CANT_READ_CLASS_FILE					= "CANT_READ_CLASS_FILE";

	public static String getERR_LOGGER_STATIC_FIELD_EXIST(
		final String description ) {
		return "a field with a logger-name is allready declared, but has another type: "
				+ description;
	}

	public static String getERR_LOG_STATEMENT_NOT_BEGINS_WITH_LABEL(
		final String enclosingMethodName,
		final String methodName,
		final String logMessageTemplate,
		final String classNameOfInstruction ) {

		return "[? PROBLEM]: No line number (marked with a bytecode-label) for the log-statement was found.\n"
				+ "[> CAUSE]: To parse the bytecode of a class pencil needs debug informations.\n"
				+ "\t Debuginformations are line numbers and names of local variables.\n"
				+ "\t Pencil can fails if a debug information failed.\n"
				+ "[! SOLUTION]: Try to compile your classes with debug informations.\n"
				+ "[. DATA >>>>]: \n"
				+ "\t Name of the class of the found instruction instead of the expected label: "
				+ classNameOfInstruction
				+ "\n"
				+ "\t The problem is occured in the class: "
				+ " CLASS_UNKNOWN "
				+ " in the method: "
				+ enclosingMethodName
				+ "\n"
				+ "\t Called log expression is: "
				+ methodName
				+ "( "
				+ logMessageTemplate + " )";
	}
}
