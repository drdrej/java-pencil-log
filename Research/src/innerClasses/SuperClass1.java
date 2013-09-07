package innerClasses;

public class SuperClass1 extends SuperSuperClass1 {
    public int field1 = new Integer(3).intValue();
	public int field2 = new Integer(3).intValue();
	

	
	public class SuperClass2  extends SuperSuperClass2 {
//		public int field1 = new Integer(2).intValue();
		
		public class SuperClass3  extends SuperSuperClass3 {
//			public int field1 = new Integer(5).intValue();
			
			public class SuperClass4  extends SuperSuperClass4 {
//				public int field1 = new Integer(44).intValue();
				protected int testField = new Integer(1);
			}
		}
	}
}
