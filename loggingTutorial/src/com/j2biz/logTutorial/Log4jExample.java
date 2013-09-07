package com.j2biz.logTutorial;

import org.apache.log4j.Logger;

public class Log4jExample {

	private static final Logger LOG = Logger.getLogger( Log4jExample.class );
	
	public static void main( String[] args ) {
		LOG.debug("application started");
		executeApplication();
		LOG.debug("application stoped");
	}
	
	private static void executeApplication( ) {
		
	}
}
