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
package com.j2biz.pencil.test.additional;

import com.j2biz.pencil.test.stuff.Person;

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class DotCallTestObject {
	
       public Person field1 = new Person(1);
       protected int field2 = new Integer(2).intValue();
	   private int field3 = new Integer(3).intValue();
	   
	   private static class SubClass extends DotCallTestObject {
		   public Person field1 = new Person(10);
	       protected int field2 = new Integer(20).intValue();
		   private int field3 = new Integer(30).intValue();
	   }
	   
	   public static final DotCallTestObject createSublCass() {
		   return new SubClass();
	   }
}
