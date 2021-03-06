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

import com.j2biz.pencil.test.stuff.Person;

public class Casting {

	private static Person person = new Person(1);
	
	{
//	  LOG.debug( "Casting.<init>.castToOwnerClass.publicField = ${(com.j2biz.pencil.test.stuff.Person):person.type}" );
//	  LOG.debug( "Casting.<init>.castToSuperClass.publicField = ${(com.j2biz.pencil.test.stuff.DBEntry):person.type}" );	
	}
	
	public static void main(String[] args) {
		new Casting();
	}
}
