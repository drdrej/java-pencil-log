/* 
 * "Pencil - Log message compiler" is (c) 2004 Andreas Siebert (j2biz community)
 *
 * Author: Andreas Siebert.
 *  
 * This file is part of "Pencil - Log message compiler".
 *
 * "Pencil - Log message compiler" is free software; 
 * you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version.
 *
 * "Pencil - Log message compiler" is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Pencil - Logger message compiler"; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package com.j2biz.pencil.el;

import java.io.StringReader;

import antlr.collections.AST;


/**
 * @author andrej
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class Template {

    
    /**
     * Factory-Method for parsing.
     * 
     * @param veloCode must be not null.
     * 
     * @return NULL or a TemplateInstance.
     */
    public static TemplateTreeParser parse(final String template) {
        final StringReader reader = new StringReader(template);
        final TemplateTreeLexer lexer = new TemplateTreeLexer(reader);
        final TemplateTreeParser parser = new TemplateTreeParser(lexer);
        
        return parser;
    }
    
    // in der aktuallen Version keine komplexen referenzen erlaubt.
    public static String referenceToString(AST reference) {
        return partToStringWithPrefixDownFromNextChild(reference, "");
    }
    
    public static String partToStringWithPrefixDownFromNextChild(final AST reference, final String prefix) {
    	assert reference != null;
    	assert reference.getFirstChild() != null;
    	assert prefix != null;
    	
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "${" );
        buffer.append( prefix );
        
        for (AST next = reference.getFirstChild(); next != null; next = next.getFirstChild()) {
            buffer.append(next.getText());
            if( next.getFirstChild() != null ) {
                buffer.append('.');
            }
        }
        
        buffer.append('}');
        return buffer.toString();
    }
    
    public static String getScopeIdText(final AST idAst) {
        if( idAst == null ) {
            throw new NullPointerException( "parameter:idAst" );
        }
        if( idAst.getType() != TemplateTreeParserTokenTypes.SCOPE ) {
            throw new IllegalArgumentException( "parameter:idAst; wrong type == " + idAst.getType() );
        }
        final StringBuffer buffer = new StringBuffer();
        for(AST nextIdAst = idAst; nextIdAst != null ; nextIdAst = nextIdAst.getFirstChild() ) {
            buffer.append(nextIdAst.getText());
            if( nextIdAst.getFirstChild() != null ) {
                buffer.append( '.' );
            }
        }
        
        return buffer.toString();
    }
}
