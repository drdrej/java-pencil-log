/*
 * Copyright 2004 Andreas Siebert (j2biz community)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.j2biz.pencil.ant;

import java.io.File;
import java.io.IOException;

public class MsgKeys {

	public static final String	getERR_CONFIG_ATTR_REQUIRED	() { 
		return "[? PROBLEM]: To cofigure pencil pencil-task need a pencil configuration file. \n"
																+ "\t You forgot to set the path to the configuration file. \n"
																+ "[! SOLUTION]: To set the path to the configuration file please use the config-attribute of the pencil-task. \n"
																+ "\t (f.e. <pencil config='./my-config-file.xml' ... >)";
	}

	public static final String getERR_CONFIG_FILE_NOT_EXIST(
		final String givenPath,
		final File calculatedPath ) {
		return "[? PROBLEM]: The configuration file, which was defined in the config-attribute of the pencil-task, does not exist.\n"
				+ "[! SOLUTION]: Maybe the path to the file or the file-name is wrong. \n"
				+ "\t You have entered the following path: " + givenPath + ".\n"
				+ "\t Pencil-task calculates the following path based on your entered path: " + calculatedPath.getAbsolutePath() + ".\n"
				+ "\t Please correct your entered path to the configuration file!";
	}
	
	public static final String getERR_CONFIG_FILE_IS_A_DIRECTORY(
		final String givenPath,
		final File calculatedPath ) {
		return "[? PROBLEM]: The path to the configuration file, which was defined in the config-attribute of the pencil-task, \n" +
				"\t refers to a directory but should refer to a file.\n"
				+ "[! SOLUTION]: Maybe the path to the file or the file-name is wrong. \n"
				+ "\t You have entered the following path: " + givenPath + ".\n"
				+ "\t Pencil-task calculates the following path based on your entered path: " + calculatedPath.getAbsolutePath() + ".\n"
				+ "\t Please correct your entered path to the configuration file!";
	}
	
	
	public static final String getERR_CANT_CREATE_TEMP_CONFIG_FILE(final IOException x, final String givenPath, final File calculatedPath) {
		return 
		"[? PROBLEM]: Pencil can't create a temporary copy of the configuration file.\n" +
		"[> CAUSE]: " + x.getLocalizedMessage() +
		"[! SOLUTION]: Check permissions! Maybe pencil (java/ant) have no rights to create or to write a file.\n"
		+ "\t You have entered the following path: " + givenPath + ".\n"
		+ "\t Pencil-task calculates the following path based on your entered path: " + calculatedPath.getAbsolutePath() + ".\n";
	}

}
