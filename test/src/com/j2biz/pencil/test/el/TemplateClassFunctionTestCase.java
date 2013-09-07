package com.j2biz.pencil.test.el;

import antlr.collections.AST;

import com.j2biz.pencil.el.TemplateTreeParser;
import com.j2biz.pencil.el.TemplateTreeParserTokenTypes;

import junit.framework.TestCase;

public class TemplateClassFunctionTestCase extends TestCase {

	public void testClassFunctionCall( ) {
		TemplateTreeParser parser = TemplateParserFactory
				.createParser("textBefore ${  class.lineNumber } afterText");

		try {
			parser.parseStart();
			final AST root = parser.getAST();
			assertEquals("root correct", root.getType(),
					TemplateTreeParserTokenTypes.TEMPLATE);
			assertEquals("number of elements", root.getNumberOfChildren(), 3);

			final AST before = root.getFirstChild();
			assertEquals("before text node", before.getType(),
					TemplateTreeParserTokenTypes.TEXT);

			final AST reference = before.getNextSibling();
			assertEquals("classFunction", reference.getType(),
					TemplateTreeParserTokenTypes.CLASS_FUNCTION);
			
			final AST idName = reference.getFirstChild();
			assertEquals("first child", idName.getText(), "lineNumber" );

			final AST after = reference.getNextSibling();
			assertEquals("after", after.getType(),
					TemplateTreeParserTokenTypes.TEXT);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("No exception expected.");
		}
	}
	
	public void testClassAsPrefixOfVariableName( ) {
		TemplateTreeParser parser = TemplateParserFactory
				.createParser("textBefore ${  classLineNumber } afterText");

		try {
			parser.parseStart();
			final AST root = parser.getAST();
			assertEquals("root correct", root.getType(),
					TemplateTreeParserTokenTypes.TEMPLATE);
			assertEquals("number of elements", root.getNumberOfChildren(), 3);

			final AST before = root.getFirstChild();
			assertEquals("before text node", before.getType(),
					TemplateTreeParserTokenTypes.TEXT);

			final AST reference = before.getNextSibling();
			assertEquals("classFunction", reference.getType(),
					TemplateTreeParserTokenTypes.REFERENCE);
			
			final AST idName = reference.getFirstChild();
			assertEquals("first child", idName.getText(), "classLineNumber" );

			final AST after = reference.getNextSibling();
			assertEquals("after", after.getType(),
					TemplateTreeParserTokenTypes.TEXT);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("No exception expected.");
		}
	}
}
