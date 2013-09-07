package com.j2biz.pencil.test.el;

import java.io.StringReader;

import com.j2biz.pencil.el.TemplateTreeLexer;
import com.j2biz.pencil.el.TemplateTreeParser;

public class TemplateParserFactory {

	public static TemplateTreeParser createParser(String template) {
	    /**
	     * @test
	     */
	    StringReader reader = new StringReader(template);
	    /**
	     * @deprecated
	     */
	    TemplateTreeLexer lexer = new TemplateTreeLexer(reader);
	    TemplateTreeParser parser = new TemplateTreeParser(lexer);
	    return parser;
	}

}
