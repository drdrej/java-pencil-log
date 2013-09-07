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
package com.j2biz.pencil.test.stuff;

/**
 * This class is only for test-purposes, to
 * check the classpath-classloader.
 */
public class Person extends DBEntry {
	 
	public String type = new String("personType");
	
    public String idxStr;
	
	public Integer publicField = new Integer(1);
	protected Integer protectedField = new Integer(2);
	private Integer privateField = new Integer(3);
	
    public Person(int id) {
    	this.idxStr = String.valueOf(id);
    }
	
	public String toString() {
		return idxStr;
	}
}   
