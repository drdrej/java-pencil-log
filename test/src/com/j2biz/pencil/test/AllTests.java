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

import com.j2biz.pencil.test.compiler.RemoveLastStringStrategyTest;
import com.j2biz.pencil.test.el.TemplateClassFunctionTestCase;
import com.j2biz.pencil.test.el.TemplateParserTestCase;
import com.j2biz.pencil.test.output.ConfigManagerTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author andrej
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class AllTests {

	public static Test suite() throws Exception {

		TestSuite suite = new TestSuite("Test for com.j2biz.pencil.test");

		// $JUnit-BEGIN$
		try {
			suite.addTestSuite(ConfigManagerTestCase.class);
			suite.addTestSuite(TemplateParserTestCase.class);
			suite.addTestSuite(TemplateClassFunctionTestCase.class);
			suite.addTestSuite( RemoveLastStringStrategyTest.class );

			EnhancedClassesTestCase.executeEnhancedApplication();
			EnhancedClassesTestCase.executeOriginalApplikation();
			
			suite.addTestSuite(EnhancedClassesTestCase.class);
		} catch (Exception x) {
			System.out.println("Test missed. exception: " + x);
			x.printStackTrace();
		}

		// $JUnit-END$

		return suite;
	}
}