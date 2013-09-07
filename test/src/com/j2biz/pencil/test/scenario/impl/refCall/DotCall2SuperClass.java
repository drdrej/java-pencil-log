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

import com.j2biz.pencil.test.additional.TestLog;

public class DotCall2SuperClass {

	
	public void testSubClass() {
		
		DotCall2 subClass = new DotCall2();
		TestLog.debug("DotCall2SuperClass.<init>.subClass.public = " + subClass.publicField);
		TestLog.debug("DotCall2SuperClass.<init>.subClass.protected = " + subClass.protectedField);
		TestLog.debug("DotCall2SuperClass.<init>.subClass.private = ${subClass.privateField}" );
	}
	
	public static void main(String[] args) {
		new DotCall2SuperClass().testSubClass();
	}
}
