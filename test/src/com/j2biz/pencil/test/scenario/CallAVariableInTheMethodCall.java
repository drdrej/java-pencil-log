package com.j2biz.pencil.test.scenario;

import com.j2biz.log.LOG;

public class CallAVariableInTheMethodCall {

	
	public static void main( String[] args ) {
		final String myVariable = "here is an log-expression-text" + System.currentTimeMillis();
		
		LOG.debug( myVariable );
		
		final String myVariable2 = "this message is maybe compiled correctly, cause final variables can be optimized.";
		LOG.debug( myVariable );
		
		LOG.debug( System.currentTimeMillis() + " milliseconds");
	}
}
