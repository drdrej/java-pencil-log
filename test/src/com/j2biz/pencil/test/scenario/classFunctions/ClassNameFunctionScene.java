package com.j2biz.pencil.test.scenario.classFunctions;

import com.j2biz.log.LOG;
import com.j2biz.pencil.test.additional.TestLog;

public class ClassNameFunctionScene {

	static {
		LOG.debug("ClassNameFunctionScene.<clinit>() = ${class}");
		LOG.debug("ClassNameFunctionScene.<clinit>().full = ${class.name}");
		
		{
			LOG.debug("ClassNameFunctionScene.<clinit>().local = ${class}");
			LOG.debug("ClassNameFunctionScene.<clinit>().full.local = ${class.name}");
		}
	}

	{
		LOG.debug("ClassNameFunctionScene.<init>() = ${class}");
		LOG.debug("ClassNameFunctionScene.<init>().full = ${class.name}");
		
		{
			LOG.debug("ClassNameFunctionScene.<init>().local = ${class}");
			LOG.debug("ClassNameFunctionScene.<init>().full.local = ${class.name}");	
		}
	}

	public static void main( String[] args ) {
		LOG.debug("ClassNameFunctionScene.main() = ${class}");
		LOG.debug("ClassNameFunctionScene.main().full = ${class.name}");

		new ClassNameFunctionScene().nonStaticMethod();

		class EmbeddedClass {
			{
				LOG.debug("ClassNameFunctionScene.EmbeddedClass.<init>() = ${class}");
				LOG
						.debug("ClassNameFunctionScene.EmbeddedClass.<init>().full = ${class.name}");
				
				{
					LOG.debug("ClassNameFunctionScene.EmbeddedClass.<init>().local = ${class}");
					LOG
							.debug("ClassNameFunctionScene.EmbeddedClass.<init>().full.local = ${class.name}");	
				}
			}
		}
		
		new EmbeddedClass();
	}
	
	protected final void nonStaticMethod() {
		LOG.debug("ClassNameFunctionScene.nonStaticMethod() = ${class}");
		LOG.debug("ClassNameFunctionScene.nonStaticMethod().full = ${class.name}");
		
		{
			LOG.debug("ClassNameFunctionScene.nonStaticMethod().local = ${class}");
			LOG.debug("ClassNameFunctionScene.nonStaticMethod().full.local = ${class.name}");
		}
	}
}
