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

public class FieldScopesSuperClass {

	/**
	 * this field is shadowed by an field5 - attribte in the subclass.
	 */
	public final int field5 = new Integer(50).intValue();
	
	/**
	 * to test fields which are not visible in the subclass. cause private.
	 */
	private final int field6 = new Integer(60).intValue();
	
	/**
	 * to test fields, which are visible form the subclass.
	 */
	protected int field7 = new Integer(70).intValue();
}
