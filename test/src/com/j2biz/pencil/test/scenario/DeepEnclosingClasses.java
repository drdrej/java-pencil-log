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
package com.j2biz.pencil.test.scenario;

import com.j2biz.log.LOG;

public class DeepEnclosingClasses {

	static String field = new String("testField");

	class Class1 {

		class Class2 {

			class Class3 {
				{
					LOG.debug("DeepEnclosingClasses.Class1.Class2.Class3.<init> = ok");
					LOG.debug("DeepEnclosingClasses.Class1.Class2.Class3.<init>.field = ${field}");
				}
			}
		}
	}

	static class NestedClass1 {

		static class NestedClass2 {

			static class NestedClass3 {
				{
					LOG.debug("DeepEnclosingClasses.NestedClass1.NestedClass2.NestedClass3.<init> = ok");
					LOG.debug("DeepEnclosingClasses.NestedClass1.NestedClass2.NestedClass3.<init>.field = ${field}");
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new DeepEnclosingClasses().new Class1().new Class2().new Class3();
		new DeepEnclosingClasses.NestedClass1.NestedClass2.NestedClass3();
	}
}
