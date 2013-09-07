
public class MethodVSVariable {

	private static final int CYCLES = 10000000;

	private static int field = new Integer(10).intValue();
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < CYCLES; i++) {
			int var = field + (int) System.currentTimeMillis();
		}
		
		long end = System.currentTimeMillis() - start;
		System.out.println(" field call ::: " + end);
		
		long start2 = System.currentTimeMillis();
		
		for (int i = 0; i < CYCLES; i++) {
			int var = getField();
		}
		
		long end2 = System.currentTimeMillis() - start2;
		System.out.println(" method call ::: " + end2);
	}

	private static int getField() {
		return field + (int) System.currentTimeMillis();
	}
}
