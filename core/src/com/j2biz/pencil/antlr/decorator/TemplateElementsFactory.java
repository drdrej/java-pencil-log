package com.j2biz.pencil.antlr.decorator;

import antlr.collections.AST;

import com.j2biz.pencil.el.TemplateTreeParserTokenTypes;

public class TemplateElementsFactory {

	
	private static final TemplateElementsFactory INSTANCE = new TemplateElementsFactory();
	
	public LogMsgPartASTDecorator createLogMsgPart( final AST wrappedNode ) {
		assert(wrappedNode != null);
		
		switch ( wrappedNode.getType() ) {
		case TemplateTreeParserTokenTypes.TEXT :
			return new TextPartASTDecorator(wrappedNode);
		case TemplateTreeParserTokenTypes.REFERENCE :
			return new ReferencePartASTDecorator(wrappedNode);
		case TemplateTreeParserTokenTypes.CLASS_FUNCTION:
			return new ClassFunctionPartASTDecorator(wrappedNode);
		default:
			throw new UnsupportedOperationException(
					"This type of AST is not a LogMsgPart-type. type == "
							+ wrappedNode.getType());
		}
	}
	
	
	
	public static final TemplateElementsFactory getInstance() {
		return INSTANCE;
	}
}
