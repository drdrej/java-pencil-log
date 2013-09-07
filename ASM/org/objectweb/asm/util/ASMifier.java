package org.objectweb.asm.util;



public class ASMifier {

	
	public static void main(String[] args) {
//		final FileInputStream in = new FileInputStream(args[0]);
//		final ASMifierClassVisitor visitor = new ASMifierClassVisitor(new PrintWriter(System.out) );
		try {
			ASMifierClassVisitor.main( args );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		final ClassReader reader = new ClassReader(in);
//		try {
//		  reader.accept(visitor, Attributes.getDefaultAttributes(), false);
//		} catch (Throwable x) {
//			throw new RuntimeException("can't load class: " + fileSource, x);
//		}
//		return visitor.getClassInfo();
	}
}
