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
package com.j2biz.pencil.test.scenario.impl;

import com.j2biz.pencil.test.additional.FieldScopesSuperClass;
import com.j2biz.pencil.test.additional.TestInterface1;
import com.j2biz.pencil.test.additional.TestLog;

/**
 * This class tests public attribute.
 * 
 * @TODO: check if-scopes
 * @TODO: check method-in-method shadowing
 * @TODO: shadowing über unterschiedliche werte testen.
 */
public class FieldScopes extends FieldScopesSuperClass {

	/**
	 * this field is needed to test a call of a global declared variable in
	 * different scopes.
	 */
	public int field1 = new Integer(1).intValue();

	/**
	 * this field is needed to test a shadowing of a global declared variable.
	 */
	public int field2 = new Integer(2).intValue();

	/**
	 * this field is also needed to test shadowing, but only for "local" scopes.
	 * with local-scope i mean a scope inside a simple { }-block, which is
	 * placed inside a method.
	 */
	public int field3 = new Integer(3).intValue();

	/**
	 * this field is to test shadowing of attributes with an parameter.
	 */
	public int field4 = new Integer(4).intValue();

	/**
	 * this field is needed to test the shadowing of an attribute in a
	 * super-class with an attribute in this instace wich has the same name.
	 */
	public int field5 = new Integer(5).intValue();

	/**
	 * this field is to test visibility of a private field in the superclass.
	 */
	public int field6_HOLDER = new Integer(6).intValue();

	/**
	 * this field is to test the visibility of a protected field in the superclass.
	 */
	public int field7_HOLDER = new Integer(7).intValue();

	/**
	 * this field is needed to test a call of a static variable
	 */
	public static int field8 = new Integer(8).intValue();

	/**
	 * this field is needed to test a visibility of a private field in the
	 * enclosing instance
	 */
	private int field9 = new Integer(9).intValue();

	/**
	 * this field is needed to test a visibility of a static private field in
	 * the enclosing instance
	 */
	private static int field10 = new Integer(10).intValue();


	/**
	 * <clinit>has not parameters. this means, that shadowing wof an attribute
	 * with a parameter is unpossible.
	 */
	static {
		int field2 = new Integer(20).intValue();

		TestLog.debug("GlobalPublic.<clinit>().this.field1 = ${this.field1}"); // n.v.
		TestLog.debug("GlobalPublic.<clinit>().field1 = ${field1}"); // n.v.
		TestLog.debug("GlobalPublic.<clinit>().field2 = " + field2);
		TestLog.debug("GlobalPublic.<clinit>().field5 = ${field5}");
		TestLog.debug("GlobalPublic.<clinit>().field6 = ${field6}");
		TestLog.debug("GlobalPublic.<clinit>().field7 = ${field7}");
		TestLog.debug("GlobalPublic.<clinit>().field8 = " + field8);
		TestLog.debug("GlobalPublic.<clinit>().field9 = ${field9}");
		TestLog.debug("GlobalPublic.<clinit>().field10 = " + field10);
		{
			int field3 = new Integer(30).intValue();
			TestLog
					.debug("GlobalPublic.<clinit>().<local>.field1 = ${field1} "); // n.v.
			TestLog
					.debug("GlobalPublic.<clinit>().<local>.this.field1 = ${this.field1} "); // n.v.
			TestLog.debug("GlobalPublic.<clinit>().<local>.field2 = " + field2);
			TestLog.debug("GlobalPublic.<clinit>().<local>.field3 = " + field3);
			TestLog.debug("GlobalPublic.<clinit>().<local>.field5 = ${field5}");
			TestLog.debug("GlobalPublic.<clinit>().<local>.field6 = ${field6}");
			TestLog.debug("GlobalPublic.<clinit>().<local>.field7 = ${field7}");
			TestLog.debug("GlobalPublic.<clinit>().<local>.field8 = " + field8);
			TestLog.debug("GlobalPublic.<clinit>().<local>.field9 = ${field9}");
			TestLog.debug("GlobalPublic.<clinit>().<local>.field10 = "
					+ field10);
		}
	}

	{
		TestLog.debug("GlobalPublic.<init>().this.field1 = " + this.field1);
		TestLog.debug("GlobalPublic.<init>().field1 = " + field1);

		int field2 = new Integer(20).intValue();
		TestLog.debug("GlobalPublic.<init>().field2 = " + field2);
		TestLog.debug("GlobalPublic.<init>().field5 = " + field5);
		TestLog.debug("GlobalPublic.<init>().field6 = ${field6}" );
		TestLog.debug("GlobalPublic.<init>().field7 = " + field7 );
		TestLog.debug("GlobalPublic.<init>().field8 = " + field8);
		TestLog.debug("GlobalPublic.<init>().field9 = " + field9);
		TestLog.debug("GlobalPublic.<init>().field10 = " + field10);
		{
			TestLog.debug("GlobalPublic.<init>().<local>.field1 = " + field1);
			TestLog.debug("GlobalPublic.<init>().<local>.this.field1 = "
					+ field1);
			TestLog.debug("GlobalPublic.<init>().<local>.field2 = " + field2);

			int field3 = new Integer(30).intValue();
			TestLog.debug("GlobalPublic.<init>().<local>.field3 = " + field3);
			TestLog.debug("GlobalPublic.<init>().<local>.field5 = " + field5);
			TestLog.debug("GlobalPublic.<init>().<local>.field6 = ${field6}");
			TestLog.debug("GlobalPublic.<init>().<local>.field7 = " + field7);
			TestLog.debug("GlobalPublic.<init>().<local>.field8 = " + field8);
			TestLog.debug("GlobalPublic.<init>().<local>.field9 = " + field9);
			TestLog.debug("GlobalPublic.<init>().<local>.field10 = " + field10);
		}

		new InnerClass().nonStaticMethod(new Integer(40).intValue());

		class NonStaticEmbeddedClass {
			{
				TestLog.debug("NonStaticEmbeddedClass.<init>.field1 = "
						+ field1);
				TestLog.debug("NonStaticEmbeddedClass.<init>.field5 = "
						+ field5);
				TestLog.debug("NonStaticEmbeddedClass.<init>.field6 = ${field6}");
				TestLog.debug("NonStaticEmbeddedClass.<init>.field7 = " + field7);
				TestLog.debug("NonStaticEmbeddedClass.<init>.field9 = "
						+ field9);
				TestLog.debug("NonStaticEmbeddedClass.<init>.field10 = "
						+ field10);
			}
		}
	}

	public static void main(String[] args) {
		TestLog.debug("GlobalPublic.main().field1 = ${field1} "); // n.v.
		TestLog.debug("GlobalPublic.main().this.field1 = ${this.field1} "); // n.v.

		int field2 = new Integer(20).intValue();
		TestLog.debug("GlobalPublic.main().field2 = " + field2);
		TestLog.debug("GlobalPublic.main().field5 = ${field5}");
		TestLog.debug("GlobalPublic.main().field6 = ${field6}");
		TestLog.debug("GlobalPublic.main().field8 = " + field8);
		TestLog.debug("GlobalPublic.main().field9 = ${field9}");
		TestLog.debug("GlobalPublic.main().field10 = " + field10);
		{
			TestLog.debug("GlobalPublic.main().<local>.field1 = ${field1}"); // n.v.
			TestLog
					.debug("GlobalPublic.main().<local>.this.field1 = ${this.field1}"); // n.v.

			int field3 = new Integer(30).intValue();
			TestLog.debug("GlobalPublic.main().<local>.field2 = " + field2);
			TestLog.debug("GlobalPublic.main().<local>.field3 = " + field3);
			TestLog.debug("GlobalPublic.main().<local>.field5 = ${field5}");
			TestLog.debug("GlobalPublic.main().<local>.field6 = ${field6}");
			TestLog.debug("GlobalPublic.main().<local>.field8 = " + field8);
			TestLog.debug("GlobalPublic.main().<local>.field9 = ${field9}");
			TestLog.debug("GlobalPublic.main().<local>.field10 = " + field10);
		}

		final FieldScopes app = new FieldScopes();
		app.nonStaticMethod(new Integer(40).intValue());

		staticMethod(new Integer(40).intValue());

		class EmbeddedClass {
			{
				TestLog
						.debug("GlobalPublic.EmbeddedClass.<init>().field1 = ${field1}"); // n.v.
				TestLog
						.debug("GlobalPublic.EmbeddedClass.<init>().this.field1 = ${this.field1}"); // n.v.

				int field2 = new Integer(20).intValue();
				TestLog.debug("GlobalPublic.EmbeddedClass.<init>().field2 = "
						+ field2);
				TestLog
						.debug("GlobalPublic.EmbeddedClass.<init>().field5 = ${field5}");
				TestLog
				.debug("GlobalPublic.EmbeddedClass.<init>().field6 = ${field6}");
				TestLog
				.debug("GlobalPublic.EmbeddedClass.<init>().field7 = ${field7}");
				TestLog.debug("GlobalPublic.EmbeddedClass.<init>().field8 = "
						+ field8);
				TestLog
						.debug("GlobalPublic.EmbeddedClass.<init>().field9 = ${field9}");
				TestLog.debug("GlobalPublic.EmbeddedClass.<init>().field10 = "
						+ field10);
				{
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field1 = ${field1}"); // n.v.
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.this.field1 = ${this.field1}"); // n.v.

					int field3 = new Integer(30).intValue();
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field2 = "
									+ field2);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field3 = "
									+ field3);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field5 = ${field5}");
					TestLog
					.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field6 = ${field6}");
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field8 = "
									+ field8);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field9 = ${field9}");
					TestLog
							.debug("GlobalPublic.EmbeddedClass.<init>().<local>.field10 = "
									+ field10);
				}
			}

			// ------------- field8 noch nicht fertig ------------------

			protected final void nonStaticMethod(int field4) {
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().this.field1 = ${this.field1} "); // n.v.

				int field2 = new Integer(20).intValue();
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field4 = "
								+ field4);

				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().this.field4 = ${this.field4}"); // n.v.
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field5 = ${field5}");
				TestLog
				.debug("GlobalPublic.EmbeddedClass.nonStatic().field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field9 = ${field9}");
				TestLog
						.debug("GlobalPublic.EmbeddedClass.nonStatic().field10 = "
								+ field10);

				{
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field1 = ${field1} "); // n.v.
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.this.field1 = ${this.field1} "); // n.v.

					int field3 = new Integer(30).intValue();
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field2 = "
									+ field2);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field3 = "
									+ field3);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field4 = "
									+ field4);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.this.field4 = ${this.field4}");
					TestLog
					.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field5 = ${field5}");
					TestLog
					.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field6 = ${field6}");
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field8 = "
									+ field8);
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field9 = ${field9}");
					TestLog
							.debug("GlobalPublic.EmbeddedClass.nonStatic().<local>.field10 = "
									+ field10);
				}
			}
		}

		new EmbeddedClass().nonStaticMethod(new Integer(40).intValue());

		InnerStaticClass.staticMethod(new Integer(40).intValue());
		new InnerStaticClass().nonStaticMethod(new Integer(40).intValue());

		new TestInterface1() {
			{
				TestLog
						.debug("GlobalPublic.TestInterface1.<init>().field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.TestInterface1.<init>().this.field1 = ${this.field1} "); // n.v.

				int field2 = new Integer(20).intValue();
				TestLog.debug("GlobalPublic.TestInterface1.<init>().field2 = "
						+ field2);
				TestLog.debug("GlobalPublic.TestInterface1.<init>().field5 = ${field5}");
				TestLog.debug("GlobalPublic.TestInterface1.<init>().field6 = ${field6}");
				TestLog.debug("GlobalPublic.TestInterface1.<init>().field7 = ${field7}");
				TestLog.debug("GlobalPublic.TestInterface1.<init>().field8 = "
						+ field8);
				TestLog
						.debug("GlobalPublic.TestInterface1.<init>().field9 = ${field9}");
				TestLog.debug("GlobalPublic.TestInterface1.<init>().field10 = "
						+ field10);

				{
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.field1 = ${field1}"); // n.v.
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.this.field1 = ${this.field1}"); // n.v.

					int field3 = new Integer(30).intValue();
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.field2 = "
									+ field2);
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.field3 = "
									+ field3);
					TestLog
					.debug("GlobalPublic.TestInterface1.<init>().<local>.field5 = ${field5}");
					TestLog
					.debug("GlobalPublic.TestInterface1.<init>().<local>.field6 = ${field6}");
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.field8 = "
									+ field8);
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.field9 = ${field9}");
					TestLog
							.debug("GlobalPublic.TestInterface1.<init>().<local>.field10 = "
									+ field10);
				}
			}

			public void nonStaticMethod(int field4) {
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().this.field1 = ${this.field1} "); // n.v.

				int field2 = new Integer(20).intValue();
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().field4 = "
								+ field4);
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().this.field4 = ${this.field4}");
				TestLog
				.debug("GlobalPublic.TestInterface1.nonStatic().field5 = ${field5}");
				TestLog
				.debug("GlobalPublic.TestInterface1.nonStatic().field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().field9 = ${field9}");
				TestLog
						.debug("GlobalPublic.TestInterface1.nonStatic().field10 = "
								+ field10);

				{
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field1 = ${field1} "); // n.v.
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.this.field1 = ${this.field1} "); // n.v.

					int field3 = new Integer(30).intValue();
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field2 = "
									+ field2);
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field3 = "
									+ field3);
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field4 = "
									+ field4);
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.this.field4 = ${this.field4}");
					TestLog
					.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field5 = ${field5}");
					TestLog
					.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field6 = ${field6}");
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field8 = "
									+ field8);
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field9 = ${field9}");
					TestLog
							.debug("GlobalPublic.TestInterface1.nonStatic().<local>.field10 = "
									+ field10);
				}
			}
		}.nonStaticMethod(new Integer(40).intValue());
	}

	private static void staticMethod(final int field4) {
		TestLog.debug("GlobalPublic.static().field1 = ${field1} "); // n.v.
		TestLog.debug("GlobalPublic.static().this.field1 = ${this.field1}"); // n.v.

		int field2 = new Integer(20).intValue();
		TestLog.debug("GlobalPublic.static().field2 = " + field2);
		TestLog.debug("GlobalPublic.static().field4 = " + field4);
		TestLog.debug("GlobalPublic.static().this.field4 = ${this.field4}"); // n.v.
		TestLog.debug("GlobalPublic.static().field5 = ${field5}");
		TestLog.debug("GlobalPublic.static().field6 = ${field6}");
		TestLog.debug("GlobalPublic.static().field8 = " + field8);
		TestLog.debug("GlobalPublic.static().field9 = ${field9}");
		TestLog.debug("GlobalPublic.static().field10 = " + field10);

		{
			TestLog.debug("GlobalPublic.static().<local>.field1 = ${field1}"); // n.v.
			TestLog
					.debug("GlobalPublic.static().<local>.this.field1 = ${this.field1}"); // n.v.

			int field3 = new Integer(30).intValue();
			TestLog.debug("GlobalPublic.static().<local>.field2 = " + field2);
			TestLog.debug("GlobalPublic.static().<local>.field3 = " + field3);
			TestLog.debug("GlobalPublic.static().<local>.field4 = " + field4);
			TestLog
					.debug("GlobalPublic.static().<local>.this.field4 = ${this.field4}");
			TestLog.debug("GlobalPublic.static().<local>.field5 = ${field5}");
			TestLog.debug("GlobalPublic.static().<local>.field6 = ${field6}");
			TestLog.debug("GlobalPublic.static().<local>.field8 = " + field8);
			TestLog.debug("GlobalPublic.static().<local>.field9 = ${field9}");
			TestLog.debug("GlobalPublic.static().<local>.field10 = " + field10);
		}
	}

	public void nonStaticMethod(final int field4) {
		TestLog.debug("GlobalPublic.nonStatic().field1 = " + field1);
		TestLog.debug("GlobalPublic.nonStatic().this.field1 = " + this.field1);

		int field2 = new Integer(20).intValue();
		TestLog.debug("GlobalPublic.nonStatic().field2 = " + field2);
		TestLog.debug("GlobalPublic.nonStatic().field4 = " + field4);
		TestLog.debug("GlobalPublic.nonStatic().this.field4 = " + this.field4);
		TestLog.debug("GlobalPublic.nonStatic().field5 = " + field5);
		TestLog.debug("GlobalPublic.nonStatic().field6 = ${field6}");
		TestLog.debug("GlobalPublic.nonStatic().field7 = " + field7);
		TestLog.debug("GlobalPublic.nonStatic().field8 = " + field8);
		TestLog.debug("GlobalPublic.nonStatic().field9 = " + field9);
		TestLog.debug("GlobalPublic.nonStatic().field10 = " + field10);

		{
			TestLog
					.debug("GlobalPublic.nonStatic().<local>.field1 = "
							+ field1);
			TestLog.debug("GlobalPublic.nonStatic().<local>.this.field1 = "
					+ this.field1);

			int field3 = new Integer(30).intValue();
			TestLog
					.debug("GlobalPublic.nonStatic().<local>.field2 = "
							+ field2);
			TestLog
					.debug("GlobalPublic.nonStatic().<local>.field3 = "
							+ field3);
			TestLog
					.debug("GlobalPublic.nonStatic().<local>.field4 = "
							+ field4);
			TestLog.debug("GlobalPublic.nonStatic().<local>.this.field4 = "
					+ this.field4);
			TestLog
			.debug("GlobalPublic.nonStatic().<local>.field5 = "
					+ field5);
			TestLog
			.debug("GlobalPublic.nonStatic().<local>.field6 = ${field6}");
			TestLog
			.debug("GlobalPublic.nonStatic().<local>.field7 = " + field7);
			TestLog
					.debug("GlobalPublic.nonStatic().<local>.field8 = "
							+ field8);
			TestLog
					.debug("GlobalPublic.nonStatic().<local>.field9 = "
							+ field9);
			TestLog.debug("GlobalPublic.nonStatic().<local>.field10 = "
					+ field10);
		}
	}

	static class InnerStaticClass {

		static {
			TestLog
					.debug("GlobalPublic.InnerStaticClass.<clinit>().field1 = ${field1} "); // n.v.
			TestLog
					.debug("GlobalPublic.InnerStaticClass.<clinit>().this.field1 = ${this.field1} "); // n.v.

			int field2 = new Integer(20).intValue();
			TestLog.debug("GlobalPublic.InnerStaticClass.<clinit>().field2 = "
					+ field2);
			TestLog.debug("GlobalPublic.InnerStaticClass.<clinit>().field5 = ${field5}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<clinit>().field6 = ${field6}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<clinit>().field7 = ${field7}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<clinit>().field8 = "
					+ field8);
			TestLog
					.debug("GlobalPublic.InnerStaticClass.<clinit>().field9 = ${field9}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<clinit>().field10 = "
					+ field10);

			{
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.this.field1 = ${this.field1} "); // n.v.

				int field3 = new Integer(30).intValue();
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field3 = "
								+ field3);
				TestLog
				.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field5 = ${field5}"
						);
				TestLog
				.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field6 = ${field6}"
						);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field9 = ${field9}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<clinit>().<local>.field10 = "
								+ field10);
			}
		}

		{
			TestLog
					.debug("GlobalPublic.InnerStaticClass.<init>().field1 = ${field1} "); // n.v.
			TestLog
					.debug("GlobalPublic.InnerStaticClass.<init>().this.field1 = ${this.field1} "); // n.v.

			int field2 = new Integer(20).intValue();
			TestLog.debug("GlobalPublic.InnerStaticClass.<init>().field2 = "
					+ field2);
			TestLog.debug("GlobalPublic.InnerStaticClass.<init>().field5 = ${field5}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<init>().field6 = ${field6}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<init>().field7 = ${field7}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<init>().field8 = "
					+ field8);
			TestLog
					.debug("GlobalPublic.InnerStaticClass.<init>().field9 = ${field9}");
			TestLog.debug("GlobalPublic.InnerStaticClass.<init>().field10 = "
					+ field10);
			{
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.this.field1 = ${this.field1} "); // n.v.

				int field3 = new Integer(30).intValue();
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field3 = "
								+ field3);
				TestLog
				.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field5 = ${field5}");
				TestLog
				.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field9 = ${field9}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.<init>().<local>.field10 = "
								+ field10);
			}
		}

		private void nonStaticMethod(int field4) {
			TestLog
					.debug("GlobalPublic.InnerStaticClass.nonStatic().field1 = ${field1} "); // n.v.
			TestLog
					.debug("GlobalPublic.InnerStaticClass.nonStatic().this.field1 = ${this.field1} "); // n.v.

			int field2 = new Integer(20).intValue();
			TestLog.debug("GlobalPublic.InnerStaticClass.nonStatic().field2 = "
					+ field2);
			TestLog.debug("GlobalPublic.InnerStaticClass.nonStatic().field4 = "
					+ field4);
			TestLog
					.debug("GlobalPublic.InnerStaticClass.nonStatic().this.field4 = ${this.field4}");
			TestLog.debug("GlobalPublic.InnerStaticClass.nonStatic().field5 = ${field5}");
			TestLog.debug("GlobalPublic.InnerStaticClass.nonStatic().field6 = ${field6}");
			TestLog.debug("GlobalPublic.InnerStaticClass.nonStatic().field8 = "
					+ field8);
			TestLog
					.debug("GlobalPublic.InnerStaticClass.nonStatic().field9 = ${field9}");
			TestLog
					.debug("GlobalPublic.InnerStaticClass.nonStatic().field10 = "
							+ field10);

			{
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.this.field1 = ${this.field1} "); // n.v.

				int field3 = new Integer(30).intValue();
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field3 = "
								+ field3);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field4 = "
								+ field4);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.this.field4 = ${this.field4}");
				TestLog
				.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field5 = ${field5}");
				TestLog
				.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field9 = ${field9}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.nonStatic().<local>.field10 = "
								+ field10);
			}
		}

		public static void staticMethod(int field4) {
			TestLog
					.debug("GlobalPublic.InnerStaticClass.static().field1 = ${field1} "); // n.v.
			TestLog
					.debug("GlobalPublic.InnerStaticClass.static().this.field1 = ${this.field1} "); // n.v.

			int field2 = new Integer(20).intValue();
			TestLog.debug("GlobalPublic.InnerStaticClass.static().field2 = "
					+ field2);
			TestLog.debug("GlobalPublic.InnerStaticClass.static().field4 = "
					+ field4);
			TestLog
					.debug("GlobalPublic.InnerStaticClass.static().this.field4 = ${this.field4}"); // n.v.
			TestLog.debug("GlobalPublic.InnerStaticClass.static().field5 = ${field5}");
			TestLog.debug("GlobalPublic.InnerStaticClass.static().field6 = ${field6}");
			TestLog.debug("GlobalPublic.InnerStaticClass.static().field8 = "
					+ field8);
			TestLog
					.debug("GlobalPublic.InnerStaticClass.static().field9 = ${field9}");
			TestLog.debug("GlobalPublic.InnerStaticClass.static().field10 = "
					+ field10);

			{
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field1 = ${field1} "); // n.v.
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.this.field1 = ${this.field1} "); // n.v.

				int field3 = new Integer(30).intValue();
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field3 = "
								+ field3);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field4 = "
								+ field4);
				TestLog
				.debug("GlobalPublic.InnerStaticClass.static().<local>.field5 = ${field5}");
				TestLog
				.debug("GlobalPublic.InnerStaticClass.static().<local>.field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.this.field4 = ${this.field4}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field9 = ${field9}");
				TestLog
						.debug("GlobalPublic.InnerStaticClass.static().<local>.field10 = "
								+ field10);
			}
		}
	}

	class InnerClass {
		{
			TestLog
					.debug("GlobalPublic.InnerClass.<init>().field1 = "
							+ field1);
			TestLog
					.debug("GlobalPublic.InnerClass.<init>().this.field1 = ${this.field1}"); // n.v.

			int field2 = new Integer(20).intValue();
			TestLog
					.debug("GlobalPublic.InnerClass.<init>().field2 = "
							+ field2);
			TestLog
			.debug("GlobalPublic.InnerClass.<init>().field5 = "
					+ field5);
			TestLog
			.debug("GlobalPublic.InnerClass.<init>().field6 = ${field6}");
			TestLog
			.debug("GlobalPublic.InnerClass.<init>().field7 = " + field7);
			TestLog
					.debug("GlobalPublic.InnerClass.<init>().field8 = "
							+ field8);
			TestLog
					.debug("GlobalPublic.InnerClass.<init>().field9 = "
							+ field9);
			TestLog.debug("GlobalPublic.InnerClass.<init>().field10 = "
					+ field10);

			{
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.field1 = "
								+ field1);
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.this.field1 = ${this.field1}"); // n.v.

				int field3 = new Integer(30).intValue();
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.field3 = "
								+ field3);
				TestLog
				.debug("GlobalPublic.InnerClass.<init>().<local>.field5 = "
						+ field5);
				TestLog
				.debug("GlobalPublic.InnerClass.<init>().<local>.field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.field9 = "
								+ field9);
				TestLog
						.debug("GlobalPublic.InnerClass.<init>().<local>.field10 = "
								+ field10);
			}

		}

		private void nonStaticMethod(int field4) {
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field1 = "
					+ field1);
			TestLog
					.debug("GlobalPublic.InnerClass.nonStatic().this.field1 = ${this.field1}"); // n.v.

			int field2 = new Integer(20).intValue();
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field2 = "
					+ field2);
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field4 = "
					+ field4);
			TestLog
					.debug("GlobalPublic.InnerClass.nonStatic().this.field4 = ${this.field4}");
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field5 = "
					+ field5);
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field6 = ${field6}" );
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field8 = "
					+ field8);
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field9 = "
					+ field9);
			TestLog.debug("GlobalPublic.InnerClass.nonStatic().field10 = "
					+ field10);
			{
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field1 = "
								+ field1);
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.this.field1 = ${this.field1}"); // n.v.

				int field3 = new Integer(30).intValue();
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field2 = "
								+ field2);
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field3 = "
								+ field3);

				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field4 = "
								+ field4);
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.this.field4 = ${this.field4}");
				TestLog
				.debug("GlobalPublic.InnerClass.nonStatic().<local>.field5 = "
						+ field5);
				TestLog
				.debug("GlobalPublic.InnerClass.nonStatic().<local>.field6 = ${field6}");
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field8 = "
								+ field8);
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field9 = "
								+ field9);
				TestLog
						.debug("GlobalPublic.InnerClass.nonStatic().<local>.field10 = "
								+ field10);
			}
		}
	}
}
