package com.j2biz.pencil.ex;

import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.utils.StringUtils;

public class NotAllowedInstructionBeforeLogCall extends ClassParseException {

	private final Node instruction;
	
	public NotAllowedInstructionBeforeLogCall( final String msg, final Node instruction) {
		super( msg );
		
		assert msg != null;
		assert instruction != null;
		
		this.instruction = instruction;
	}

	private static final long	 serialVersionUID	= 1L;

	public Node getInstruction( ) {
		return this.instruction;
	}
	
	
	/**
	 * @param instr
	 * @return
	 */
	private String getClassNameOfInstruction( Object instr ) {
		return StringUtils.getClassName(instr);
	}
}
