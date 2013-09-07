package com.j2biz.pencil.test.el;

import junit.framework.TestCase;

public class TestFuckIt extends TestCase {

	
	public void testMe( ) throws Exception {
		new ExceptionAssert(IllegalStateException.class) {
			public void execute( )  {
				
			}
		};
	}
	
	static abstract class ExceptionAssert {
		ExceptionAssert(Class c) {
			
		}
		
		public abstract void execute() throws Exception;
	}
}
