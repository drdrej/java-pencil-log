
public class ObjectVSArray {

	
	public static void main(String[] args) {
		create();
//		get();
//		set();
	}
	
	private static void create() {
		int dumpvar = 0;
		int dumpvar1 = 0;
		int dumpvar2 = 0;
		int dumpvar3 = 0;
		
		Object obj = new Object();
		int nrOfCycles = 100000000;
		
		long start = System.currentTimeMillis();
		
		TestObject object;
		for(int i = 0; i < nrOfCycles; i++) {
			object = new TestObject();
			object.value = obj;
		}
		
		long end = System.currentTimeMillis() - start;
		System.out.println("create object: " + end);
		
		start = System.currentTimeMillis();
		
		Object[] array;
		for(int i = 0; i < nrOfCycles; i++) {
			array = new Object[2];
			array[0] = obj;
		}
		
		end = System.currentTimeMillis() - start;
		System.out.println("create array: " + end);
		
		start = System.currentTimeMillis();
		
		int[] array2;
		for(int i = 0; i < nrOfCycles; i++) {
			array2 = new int[2];
			array2[0] = 1;
		}
		
		end = System.currentTimeMillis() - start;
		System.out.println("create array: " + end);
	}

	static class TestObject {
		int flag;
		Object value;
	}
	
	static class TestObject2 {
		int flag;
		int value;
	}
}
