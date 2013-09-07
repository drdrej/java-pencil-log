package innerClasses;

import classUtils.ClassType;

public class Class1 extends SuperClass1 {
	public int field1 = new Integer(1).intValue();

	public int field2 = new Integer(1).intValue();

	public class Class2 extends SuperClass1.SuperClass2 {

		// public int field1 = new Integer(6).intValue();

		public class Class3 extends SuperClass3 {
			// {
			// System.out.println("field1 == " + field1);
			// }

			public class Class4 extends SuperClass4 {

				{
					System.out.println("field1 == " + field1);
				}
			}
		}
	}

	public static class EncClass1 {
		{
			System.out.println("member class: "
					+ EncClass1.class.isMemberClass());
		}

		int field4 = new Integer(51).intValue();

		public void test() {

			class MyEncMe {
				public int field3 = new Integer(1).intValue();

				public void test1() {
					class EncClass2 {
						{ // suche anch dem Feld.
							System.out.println("EncClass2.field3: " + field3);
							// System.out.println("EncClass2.field1: " +
							// field1); n.v.
						}

					}
				}
			}
		}

		public static class EncClass3 {
			static int field4 = new Integer(5).intValue();

			public class EncClass4 {
				{
					System.out.println("::: " + field4);
				}
			}
		}
	}

	public static void main(String[] args) {
		 assert( args.length < 1 );

//		assert (args.length < 1) : "should never reach here";
		
		Class1.Class2.Class3.Class4 cl4 = new Class1().new Class2().new Class3().new Class4();
		System.out.println(cl4.testField);
		ClassType cl = new ClassType();
		// System.out.println(cl.p);
		ClassTypePackage ctp = new ClassTypePackage();
		System.out.println(ctp.protectedField);
	}
}
