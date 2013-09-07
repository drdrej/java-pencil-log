package innerClasses;
public class SuperSuperClass1 {
    public int field1 = new Integer(31).intValue();
	public int field2 = new Integer(3).intValue();
	

	
	public class SuperSuperClass2 {
//		protected int field1 = new Integer(32).intValue();
		
		public class SuperSuperClass3 {
//			public int field1 = new Integer(5).intValue();
			
			public class SuperSuperClass4 {
//				public int field1 = new Integer(44).intValue();
			}
		}
	}
}