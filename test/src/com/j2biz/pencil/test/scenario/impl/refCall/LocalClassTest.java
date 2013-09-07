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
package com.j2biz.pencil.test.scenario.impl.refCall;


import com.j2biz.pencil.test.additional.DotCallTestObject;
import com.j2biz.pencil.test.additional.TestLog;

public class LocalClassTest {

	public int publicField = new Integer(1).intValue();
	protected int protectedField = new Integer(2).intValue();
	private int privateField = new Integer(3).intValue();
	
	
	public static class NestedClass {
		public int publicField = new Integer(1).intValue();
		protected int protectedField = new Integer(2).intValue();
		private int privateField = new Integer(3).intValue();
	}
	
	public static void main(String[] args) {
		class RefData {
			public int publicField = new Integer(1).intValue();
			protected int protectedField = new Integer(2).intValue();
			private int privateField = new Integer(3).intValue();
			
			public void testEnclosingClass() {
				LocalClassTest encClass = new LocalClassTest();
				TestLog.debug("refCall.LocalClassTest.refData.testEnclosingClass().public = " + encClass.publicField);
				TestLog.debug("refCall.LocalClassTest.refData.testEnclosingClass().protected = " + encClass.protectedField);
				TestLog.debug("refCall.LocalClassTest.refData.testEnclosingClass().private = " + encClass.privateField);
			}
		}
		
		RefData refData = new RefData();

		TestLog.debug("refCall.LocalClassTest.main().public = " + refData.publicField);
		TestLog.debug("refCall.LocalClassTest.main().protected = " + refData.protectedField);
		TestLog.debug("refCall.LocalClassTest.main().private = " + refData.privateField);
		
		class LocalExtendsNestedClass extends NestedClass {

		}
		
		LocalExtendsNestedClass localExtNested = new LocalExtendsNestedClass();
		TestLog.debug("refCall.LocalClassTest.main().LocalExtendsNestedClass.public = " + localExtNested.publicField);
		TestLog.debug("refCall.LocalClassTest.main().LocalExtendsNestedClass.protected = " + localExtNested.protectedField);
		TestLog.debug("refCall.LocalClassTest.main().LocalExtendsNestedClass.private = ${localExtNested.privateField}");
		

		DotCallTestObject subClass = DotCallTestObject.createSublCass();
		TestLog.debug("refCall.LocalClassTest.main().subClassIsHidden.public = " + subClass.field1);
		TestLog.debug("refCall.LocalClassTest.main().subClassIsHidden.protected = ${subClass.field2}");
		TestLog.debug("refCall.LocalClassTest.main().subClassIsHidden.private = ${subClass.field3}");
	}
	
}
