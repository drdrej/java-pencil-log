package com.j2biz.pencil.test.scenario.classFunctions;

import com.j2biz.log.LOG;

public class ClassLineNumberFunctionScene {

	static {
		LOG.debug("ClassNameFunctionScene.<clinit>() = ${class.lineNumber }");
		{
			LOG.debug("ClassNameFunctionScene.<clinit>().local = ${class.lineNumber }");
		}
	}

	{
		LOG.debug("ClassNameFunctionScene.<init>() = ${class.lineNumber}");
		
		{
			LOG.debug("ClassNameFunctionScene.<init>().local = ${class.lineNumber }");
		}
	}

	public static void main( String[] args ) {
		LOG.debug("ClassNameFunctionScene.main() = ${class.lineNumber}");

		new ClassLineNumberFunctionScene();

		class EmbeddedClass {
			{
				LOG.debug("ClassNameFunctionScene.EmbeddedClass.<init>() = ${class.lineNumber}");
				
				{
					LOG.debug("ClassNameFunctionScene.EmbeddedClass.<init>().local = ${class.lineNumber}");
				}
			}
		}
		
		new EmbeddedClass();
	}
}
