package com.j2biz.pencil.ex;


@SuppressWarnings("serial")
public class ByteCodeHasNoDebugInfosException extends ClassParseException {

	public ByteCodeHasNoDebugInfosException( String arg0, Throwable arg1 ) {
		super(arg0, arg1);
	}
	
	public ByteCodeHasNoDebugInfosException( String arg0) {
		super(arg0);
	}
	
	
	@Override
	public synchronized Throwable fillInStackTrace( ) {
		return this;
	}

}
