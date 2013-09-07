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
package com.j2biz.pencil.test.scenario.refCall;

import com.j2biz.log.LOG;
import com.j2biz.pencil.test.additional.DotCallTestObject;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class DotCall extends DotCallTestObject {
	public static DotCallTestObject object = new DotCallTestObject();

	public DotCallTestObjectSamePackage packageWideObj = new DotCallTestObjectSamePackage();

	public static DotCall referenceMe = new DotCall(1);

	public static int publicStaticField = new Integer(1).intValue();

	public int publicField = new Integer(2).intValue();

	protected int protectedField = new Integer(3).intValue();

	private int privateField = new Integer(4).intValue();
	
	public class ExtendsClass3 extends DotCallTestObjectSamePackage {
		;
    }
	
	public class ExtendsClass extends DotCallTestObject {
		;
	}
	
	public class ExtendsClass2 extends NestedClass {
		;
	}
	
	public class TypeTestClass {
		public int intField = new Integer(1).intValue();
		public short shortField = new Short((short) 2).shortValue();
		public byte byteField = new Byte((byte) 3).byteValue();
		public char charField = new Character('a').charValue();
		public long longField = new Long(1).longValue();
		public float floatField = new Float(2).floatValue();
		public double doubleField = new Double(3).doubleValue();
	}
	
	private class InnerClass {
		private class InnerClass2 {
			private int privateField = new Integer(1).intValue();
		}
	}

	private class NestedClass {
		private int privateField = new Integer(12).intValue();
		protected int protectedField = new Integer(13).intValue();
		public int publicField = new Integer(14).intValue();
		
		public void testSubClass() {
			ExtendsClass2 subClassCalledInSuper = new ExtendsClass2();
			LOG.debug("DotCall.NestedClass.<init>.subClassCalledInSuper.public = ${subClassCalledInSuper.publicField}");
			LOG.debug("DotCall.NestedClass.<init>.subClassCalledInSuper.protected = ${subClassCalledInSuper.protectedField}" );
			LOG.debug("DotCall.NestedClass.<init>.subClassCalledInSuper.private = ${subClassCalledInSuper.privateField}" );	
		}
	}
	
	private class ExtendsClass4 extends NestedClass2 {
		private int privateField = new Integer(12).intValue();
		protected int protectedField = new Integer(13).intValue();
		public int publicField = new Integer(14).intValue();
	}
	
	private class NestedClass2 {
		public void testSubClass() {
			ExtendsClass4 subClassCalledInSuper = new ExtendsClass4();
			LOG.debug("DotCall.NestedClass2.<init>.subClassCalledInSuper.public = ${subClassCalledInSuper.publicField}");
			LOG.debug("DotCall.NestedClass2.<init>.subClassCalledInSuper.protected = ${subClassCalledInSuper.protectedField}" );
			LOG.debug("DotCall.NestedClass2.<init>.subClassCalledInSuper.private = ${subClassCalledInSuper.privateField}" );	
		}
	}

	static {
		LOG.debug("DotCall.<clinit>.field1 = ${object.field1}");
		LOG.debug("DotCall.<clinit>.field2 = ${object.field2}");

		LOG
				.debug("DotCall.<clinit>.field1.privateField = ${object.field1.privateField}");
		LOG
				.debug("DotCall.<clinit>.field1.protectedField = ${object.field1.protectedField}"); 
		LOG
				.debug("DotCall.<clinit>.field1.publicField = ${object.field1.publicField}");

		LOG.debug("DotCall.<clinit>.field2 = ${object.field2}");
		LOG.debug("DotCall.<clinit>.fieldNotExist = ${object.fieldNotExist}");

		DotCallTestObject objectLocal = new DotCallTestObject();
		LOG
				.debug("DotCall.<clinit>.objectLocal.field1 = ${objectLocal.field1}");
	}

	{
		LOG
				.debug("DotCall.<init>.packageWideObj = ${packageWideObj.publicField}");
		LOG
				.debug("DotCall.<init>.packageWideObj.protected = ${packageWideObj.protectedField}");

		LOG.debug("DotCall.<init>.field1 = ${object.field1}");
		LOG.debug("DotCall.<init>.this.field1 = ${this.object.field1}");

		LOG
				.debug("DotCall.<init>.field1.privateField = ${object.field1.privateField}");
		LOG
				.debug("DotCall.<init>.field1.protectedField = ${object.field1.protectedField}");
		LOG
				.debug("DotCall.<init>.field1.publicField = ${object.field1.publicField}");

		LOG.debug("DotCall.<init>.field2 = ${object.field2}");
		LOG.debug("DotCall.<init>.fieldNotExist = ${object.fieldNotExist}");

		DotCallTestObject objectLocal = new DotCallTestObject();
		LOG.debug("DotCall.<init>.objectLocal.field1 = ${objectLocal.field1}");

	}

	public DotCall() {
		class EmbeddedClass {
			private int privateField = new Integer(42).intValue();
		}

		LOG
				.debug("DotCall.<constructor>.referenceMe.public = ${referenceMe.publicField}");
		LOG
				.debug("DotCall.<constructor>.referenceMe.protected = ${referenceMe.protectedField}");
		LOG
				.debug("DotCall.<constructor>.referenceMe.private = ${referenceMe.privateField}");

		
		LOG.debug("DotCall.<constructor>.parentClass.public = ${referenceMe.field1}");
		LOG.debug("DotCall.<constructor>.parentClass.protected = ${referenceMe.field2}");
		LOG.debug("DotCall.<constructor>.parentClass.private = ${referenceMe.field3}");
		
		
		InnerClass.InnerClass2 subClass = new InnerClass().new InnerClass2();
		LOG
				.debug("DotCall.<constructor>.subClass.private = ${subClass.privateField}");

		NestedClass nestedClass = new NestedClass();
		LOG
				.debug("DotCall.<constructor>.nestedClass.private = ${nestedClass.privateField}");
		
		nestedClass.testSubClass();
		
		EmbeddedClass embeddedClass = new EmbeddedClass();
		LOG.debug("DotCall.<constructor>.embeddedClass.private = ${embeddedClass.privateField}");
		
		TypeTestClass typeTest = new TypeTestClass();
		LOG.debug("DotCall.<constructor>.typeTest.intField = ${typeTest.intField.value}");
		LOG.debug("DotCall.<constructor>.typeTest.shortField = ${typeTest.shortField.value}");
		LOG.debug("DotCall.<constructor>.typeTest.byteField = ${typeTest.byteField.value}");
		LOG.debug("DotCall.<constructor>.typeTest.charField = ${typeTest.charField.value}");
		LOG.debug("DotCall.<constructor>.typeTest.longField = ${typeTest.longField.value}");
		LOG.debug("DotCall.<constructor>.typeTest.doubleField = ${typeTest.doubleField.value}");
		LOG.debug("DotCall.<constructor>.typeTest.floatField = ${typeTest.floatField.value}");
		
		ExtendsClass extendsClass = new ExtendsClass();
		LOG.debug("DotCall.<constructor>.extendsClass.public = ${extendsClass.field1}");
		LOG.debug("DotCall.<constructor>.extendsClass.protected = ${extendsClass.field2}");
		LOG.debug("DotCall.<constructor>.extendsClass.private = ${extendsClass.field3}");
		
		ExtendsClass2 extendsClass2 = new ExtendsClass2();
		LOG.debug("DotCall.<constructor>.extendsClass2.public = ${extendsClass2.publicField}");
		LOG.debug("DotCall.<constructor>.extendsClass2.protected = ${extendsClass2.protectedField}");
		LOG.debug("DotCall.<constructor>.extendsClass2.private = ${extendsClass2.privateField}" );
		
		ExtendsClass3 extendsClass3 = new ExtendsClass3();
		LOG.debug("DotCall.<constructor>.extendsClass3.public = ${extendsClass3.publicField}" );
		LOG.debug("DotCall.<constructor>.extendsClass3.protected = ${extendsClass3.protectedField}");
		LOG.debug("DotCall.<constructor>.extendsClass3.private = ${extendsClass3.privateField}" );
		
		ExtendsClass4 extendsClass4 = new ExtendsClass4();
		extendsClass4.testSubClass();
	}

	public DotCall(int test) {
		; // this is only for test. to
	}

	public static void main(String[] args) {
		new DotCall();
	}
}
