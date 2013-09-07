package com.j2biz.pencil.asm;

import java.util.Iterator;

import org.grlea.log.SimpleLogger;
import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.simple.InsnNode;

public class CLinitLogInitializationCreator implements CodeTransformation {

	private static final SimpleLogger LOG = new SimpleLogger(CLinitLogInitializationCreator.class);
	
	private final ASMClassInfoNode classToTransform;
	
	public CLinitLogInitializationCreator(final ASMClassInfoNode classInfoNode) {
		LOG.entry( "this( ASMClassInfoNode classInfoNode )");
		
		assert classInfoNode != null;
		
		LOG.debugObject( "class to transform", classInfoNode );
		this.classToTransform = classInfoNode;
	}
	
	private void prepareClinitMethod( final ASMClassInfoNode  modifiedClass) {
		for ( final Iterator i = modifiedClass.methods(); i.hasNext(); ) {
			final MethodInfoNode method = (MethodInfoNode) i.next();
			if ( "<clinit>".equals(method.getName()) ) {
				fillClinitWithLogInitCode(modifiedClass, method);
				return;
			}
		}

		final MethodInfoNode method = new MethodInfoNode(Constants.ACC_STATIC,
				"<clinit>", "()V", null, null, modifiedClass);

		modifiedClass.addMethod(method);

		fillClinitWithLogInitCode(modifiedClass, method);
		method.addInstruction(new InsnNode(Constants.RETURN));
	}
	
	
	/**
	 * @param method
	 */
	private void fillClinitWithLogInitCode(
		final ASMClassInfoNode classInfo,
		final MethodInfoNode method ) {
		
		if ( method.numberOfInstructions() > 0 ) {
			method.insertLogInitInClearStaticBlock(classInfo);
		} else {
			method.addLogInitAtStaticBlock(classInfo);
		}
	}

	public void transform( ) {
		LOG.entry("transform( )");
		prepareClinitMethod(this.classToTransform);
		LOG.exit("transform( )");
	}
}
