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

package com.j2biz.pencil.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.j2biz.pencil.ex.ClassParseException;
import com.j2biz.pencil.test.scenario.CallAVariableInTheMethodCall;
import com.j2biz.pencil.test.scenario.Casting;
import com.j2biz.pencil.test.scenario.ClassWithoutStaticBlock;
import com.j2biz.pencil.test.scenario.DeepEnclosingClasses;
import com.j2biz.pencil.test.scenario.FieldScopes;
import com.j2biz.pencil.test.scenario.LoggerExists;
import com.j2biz.pencil.test.scenario.MethodWithoutLocalVariable;
import com.j2biz.pencil.test.scenario.ThisStory;
import com.j2biz.pencil.test.scenario.TypeCheck;
import com.j2biz.pencil.test.scenario.classFunctions.ClassLineNumberFunctionScene;
import com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene;
import com.j2biz.pencil.test.scenario.npe.SimpleNullPointerTest;
import com.j2biz.pencil.test.scenario.refCall.DotCall;
import com.j2biz.pencil.test.scenario.refCall.DotCall2SuperClass;
import com.j2biz.pencil.test.scenario.refCall.LocalClassTest;

/**
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com
 */
public class EnhancedClassesTestCase extends TestCase {

	private static Properties	ENHANCED_VALUES			= new Properties();

	private static Properties	ORIGINAL_VALUES			= new Properties();

	public static final String	OUTPUT_FILE_NAME		= "enhanced.out";

	public static final String	ORIG_OUTPUT_FILE_NAME	= "original.out";
	
	public static final String[] NO_ARGS = new String[0];

	public static final void executeEnhancedApplication( ) {
		try {
			System.out
					.println("[EnhancedClassesTestCase] Begin with test of the TestApplication");
			final PrintStream oldOut = System.err;
			final FileOutputStream dropedOut = new FileOutputStream(
					OUTPUT_FILE_NAME);
			final PrintStream newOut = new PrintStream(dropedOut);
			System.setErr(newOut);

			try {
				FieldScopes.main(NO_ARGS);
				ClassWithoutStaticBlock.main(NO_ARGS);
				DeepEnclosingClasses.main(NO_ARGS);
				TypeCheck.main(NO_ARGS);
				DotCall.main(NO_ARGS);
				DotCall2SuperClass.main(NO_ARGS);
				LoggerExists.main(NO_ARGS);
				ThisStory.main(NO_ARGS);
				Casting.main(NO_ARGS);
				LocalClassTest.main(NO_ARGS);
				SimpleNullPointerTest.main(NO_ARGS);
				MethodWithoutLocalVariable.main(NO_ARGS);
				ClassNameFunctionScene.main(NO_ARGS);
				ClassLineNumberFunctionScene.main(NO_ARGS);
			} catch ( final Throwable x ) {
				System.out
						.println("[EnhancedClassesTestCase:ERROR] can't execute TestApplication, cause: "
								+ x.getLocalizedMessage());
				x.printStackTrace();
			}
			
			System.setErr(oldOut);
			newOut.flush();
			newOut.close();

			System.out.println("[EnhancedClassesTestCase] load properties");
			ENHANCED_VALUES.load(new FileInputStream(OUTPUT_FILE_NAME));

			System.out
					.println("[EnhancedClassesTestCase] properties loaded, ready to test ...");
		} catch ( final IOException x ) {
			System.out
					.println("[EnhancedClassesTestCase] Failure occured during initialization of the TestCase. cause: "
							+ x.getLocalizedMessage());
			x.printStackTrace();
		}
	}

	public EnhancedClassesTestCase( ) throws IOException {
		;
	}

	/**
	 * 
	 */
	public static void executeOriginalApplikation( ) {
		try {
			System.out
					.println("[EnhancedClassesTestCase] Begin with test of the TestApplication");
			PrintStream oldOut = System.err;
			FileOutputStream dropedOut = new FileOutputStream(
					ORIG_OUTPUT_FILE_NAME);
			PrintStream newOut = new PrintStream(dropedOut);
			System.setErr(newOut);

			try {
				com.j2biz.pencil.test.scenario.impl.FieldScopes.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.ClassWithoutStaticBlock
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.DeepEnclosingClasses
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.TypeCheck.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.refCall.DotCall
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.refCall.DotCall2SuperClass
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.LoggerExists.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.ThisStory.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.Casting.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.refCall.LocalClassTest
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.npe.SimpleNullPointerTest
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.MethodWithoutLocalVariable
						.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.classFunctions
				       .ClassNameFunctionScene.main(NO_ARGS);
				com.j2biz.pencil.test.scenario.impl.classFunctions.ClassLineNumberFunctionScene.main(NO_ARGS);
			} catch ( final Throwable x ) {
				System.out
						.println("[EnhancedClassesTestCase:ERROR] can't execute TestApplication, cause: "
								+ x.getLocalizedMessage());
				x.printStackTrace();
			}
			
			System.setErr(oldOut);
			newOut.flush();
			newOut.close();

			System.out.println("[EnhancedClassesTestCase] load properties");

			ORIGINAL_VALUES.load(new FileInputStream(ORIG_OUTPUT_FILE_NAME));

			System.out
					.println("[EnhancedClassesTestCase] properties loaded, ready to test ...");
		} catch ( IOException x ) {
			System.out
					.println("[EnhancedClassesTestCase] Failure occured during initialization of the TestCase. cause: "
							+ x.getLocalizedMessage());
			x.printStackTrace();
		}
	}

	public void testCreatedFiles( ) {
		File enhanced = new File(OUTPUT_FILE_NAME);
		assertTrue("enhanced output. does not exist.", enhanced.exists());
		assertTrue("enhanced output is empty.", enhanced.length() > 0);

		File original = new File(ORIG_OUTPUT_FILE_NAME);
		assertTrue("original output. does not exist.", original.exists());
		assertTrue("original output is empty.", original.length() > 0);
	}

	public void testOriginalValues( ) throws FileNotFoundException {
		final PrintWriter fout = new PrintWriter(new FileOutputStream("error_"
				+ ORIG_OUTPUT_FILE_NAME));

		int counter = 0;

		for ( Enumeration i = ORIGINAL_VALUES.keys(); i.hasMoreElements(); ) {
			try {
				String key = (String) i.nextElement();
				String originalValue = ORIGINAL_VALUES.getProperty(key);
				String enhancedValue = ENHANCED_VALUES.getProperty(key);
				assertNotNull("original value is NULL: " + key, originalValue);
				assertNotNull("enhanced value is NULL:" + key, enhancedValue);
				assertEquals("original " + key, enhancedValue.trim(),
						originalValue.trim());

			} catch ( Throwable t ) {
				counter++;
				fout.println(counter + ". error : ");
				fout.println(">>> ");
				t.printStackTrace(fout);
				fout.println("<<< ");
				fout.println();
			}
		}

		if ( counter > 0 ) {
			fout.println("\t\t Number of errors: " + counter);
		}

		fout.flush();
		fout.close();

		if ( counter > 0 ) {
			throw new AssertionFailedError(
					"found "
							+ counter
							+ " values of an original class, which doesn't exist in the output of the enhanced class.");
		}
	}

	public void testEnhancedValues( ) throws IOException {
		final PrintWriter fout = new PrintWriter(new FileOutputStream("error_"
				+ OUTPUT_FILE_NAME));

		int counter = 0;

		for ( Enumeration i = ENHANCED_VALUES.keys(); i.hasMoreElements(); ) {
			try {
				String key = (String) i.nextElement();
				String originalValue = ORIGINAL_VALUES.getProperty(key);
				String enhancedValue = ENHANCED_VALUES.getProperty(key);
				assertNotNull("original value is NULL:" + key, originalValue);
				assertNotNull("enhanced value is NULL:" + key, enhancedValue);
				assertEquals("enhanced " + key, enhancedValue.trim(),
						originalValue.trim());
			} catch ( Throwable t ) {
				counter++;
				fout.println(counter + ". error : ");
				fout.println(">>> ");
				t.printStackTrace(fout);
				fout.println("<<< ");
				fout.println();
			}
		}

		if ( counter > 0 ) {
			fout.println("\t\t Number of errors: " + counter);
		}

		fout.flush();
		fout.close();

		if ( counter > 0 ) {
			throw new AssertionFailedError(
					"found "
							+ counter
							+ " values of an enhanced class, which doesn't exist in the output of the original class.");
		}
	}
	
	
	
	
    public void testCallVariableInADebugMethodCall() {
		CallAVariableInTheMethodCall.main(NO_ARGS);
		
		try {
			CallAVariableInTheMethodCall.main(NO_ARGS);
		} catch (ClassParseException x) {
			;
		}
    }
}