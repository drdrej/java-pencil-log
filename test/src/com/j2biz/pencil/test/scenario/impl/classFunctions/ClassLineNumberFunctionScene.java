package com.j2biz.pencil.test.scenario.impl.classFunctions;

import com.j2biz.pencil.test.additional.TestLog;

public class ClassLineNumberFunctionScene {

	static {
		TestLog.debug("ClassNameFunctionScene.<clinit>() = 8");
		{
			TestLog.debug("ClassNameFunctionScene.<clinit>().local = 10");
		}
	}

	{
		TestLog.debug("ClassNameFunctionScene.<init>() = 15");
		
		{
			TestLog.debug("ClassNameFunctionScene.<init>().local = 18");
		}
	}

	public static void main( String[] args ) {
		TestLog.debug("ClassNameFunctionScene.main() = 23");

		new ClassLineNumberFunctionScene();

		class EmbeddedClass {
			{
				TestLog.debug("ClassNameFunctionScene.EmbeddedClass.<init>() = 29");
				
				{
					TestLog.debug("ClassNameFunctionScene.EmbeddedClass.<init>().local = 32");
				}
			}
		}
		
		new EmbeddedClass();
	}
}