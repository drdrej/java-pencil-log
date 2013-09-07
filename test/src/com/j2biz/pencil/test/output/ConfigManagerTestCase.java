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

package com.j2biz.pencil.test.output;

import java.io.FileNotFoundException;

import junit.framework.TestCase;

import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.config.ConfigManager;

public class ConfigManagerTestCase extends TestCase {

	
	public void testConfig() {

		String xmlFile = System.getProperty("XML-FILE");
		if( xmlFile == null )
			xmlFile = "../build/test-config.xml";
		
		ConfigManager.initialize();
		
		try {
			ClassManager manager = ConfigManager.readConfiguration(xmlFile);
			assertNotNull("classManager is initialized.", manager);
		} catch (final FileNotFoundException e) {
			fail("cant' load configuration. exception: " + e);
		}
	}
}
