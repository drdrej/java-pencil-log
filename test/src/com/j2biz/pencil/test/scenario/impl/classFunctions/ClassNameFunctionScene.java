package com.j2biz.pencil.test.scenario.impl.classFunctions;

import com.j2biz.pencil.test.additional.TestLog;

public class ClassNameFunctionScene {
	
	static {
		TestLog.debug("ClassNameFunctionScene.<clinit>() = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		TestLog.debug("ClassNameFunctionScene.<clinit>().full = " +  com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		
		{
			TestLog.debug("ClassNameFunctionScene.<clinit>().local = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
			TestLog.debug("ClassNameFunctionScene.<clinit>().full.local = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		}
	}

	{
		TestLog.debug("ClassNameFunctionScene.<init>() = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		TestLog.debug("ClassNameFunctionScene.<init>().full = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		
		{
			TestLog.debug("ClassNameFunctionScene.<init>().local = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
			TestLog.debug("ClassNameFunctionScene.<init>().full.local = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());	
		}
	}

	public static void main( String[] args ) {
		TestLog.debug("ClassNameFunctionScene.main() = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		TestLog.debug("ClassNameFunctionScene.main().full = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());

		new ClassNameFunctionScene().nonStaticMethod();

		class EmbeddedClass {
			{
				TestLog.debug("ClassNameFunctionScene.EmbeddedClass.<init>() = " + EmbeddedClass.class.getName().replace("impl.", "") );
				TestLog
						.debug("ClassNameFunctionScene.EmbeddedClass.<init>().full = " + EmbeddedClass.class.getName().replace("impl.", "") );
				
				{
					TestLog.debug("ClassNameFunctionScene.EmbeddedClass.<init>().local = " + EmbeddedClass.class.getName().replace("impl.", "") );
					TestLog
							.debug("ClassNameFunctionScene.EmbeddedClass.<init>().full.local = " + EmbeddedClass.class.getName().replace("impl.", "") );	
				}
			}
		}
		
		new EmbeddedClass();
	}
	
	
	protected final void nonStaticMethod() {
		TestLog.debug("ClassNameFunctionScene.nonStaticMethod() = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		TestLog.debug("ClassNameFunctionScene.nonStaticMethod().full = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		
		{
			TestLog.debug("ClassNameFunctionScene.nonStaticMethod().local = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
			TestLog.debug("ClassNameFunctionScene.nonStaticMethod().full.local = " + com.j2biz.pencil.test.scenario.classFunctions.ClassNameFunctionScene.class.getName());
		}
	}
}
