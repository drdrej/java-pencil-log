package com.j2biz.pencil.ex;

public class OnlyStringsAllowedException extends ClassParseException {

	public OnlyStringsAllowedException( final String msg ) {
		super(msg);
		
		assert msg != null;
		
		
	}

	private static final long	serialVersionUID	= -514609964139936942L;

}
