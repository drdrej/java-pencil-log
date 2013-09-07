package instr;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class ClassLoaderAgent implements ClassFileTransformer {

	public static void premain(final String options, final Instrumentation instr) {
		System.out.println(">>> load agent ...");
//		instr.addTransformer(new ClassLoaderAgent());
	}
	
 	public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// überschreibe define()
		System.out.println("transform class: " + className);
		return null;
	}


}
