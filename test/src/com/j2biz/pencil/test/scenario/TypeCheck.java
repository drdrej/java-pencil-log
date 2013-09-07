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
import com.j2biz.pencil.test.stuff.Person;

public class TypeCheck {

	
		public static void main(String[] args) {
			String varStr = new String("string variable");
			LOG.debug( "TypeCheck.main.varStr = ${varStr}" );
			
			int varInt = new Integer(1).intValue();
			LOG.debug( "TypeCheck.main.varInt = ${varInt}" );
			
			short varShort = new Short((short)1).shortValue();
			LOG.debug( "TypeCheck.main.varShort = ${varShort}" );
			
			byte varByte = new Byte( (byte) 1).byteValue();
			LOG.debug("TypeCheck.main.varByte = ${varByte}" );
			
			char varChar = new Character( 'a' ).charValue();
			LOG.debug("TypeCheck.main.varChar = ${varChar}");
			
			long varLong = new Long(1L).longValue();
			LOG.debug("TypeCheck.main.varLong = ${varLong}");
			
			double varDouble = new Double(1L).doubleValue();
			LOG.debug("TypeCheck.main.varDouble = ${varDouble}");
			
			float varFloat = new Float(1L).floatValue();
			LOG.debug("TypeCheck.main.varFloat = ${varFloat}");
			
			Person person = new Person(1);
			LOG.debug("TypeCheck.main.person = ${person}");
		}
}
