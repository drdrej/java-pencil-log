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
package com.j2biz.info;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import antlr.collections.AST;

import com.j2biz.pencil.el.Template;
import com.j2biz.pencil.el.TemplateTreeParser;
import com.j2biz.pencil.el.TemplateTreeParserTokenTypes;


/**
 * @author andrej
 */
public class LoggerMsgManager {
    
    private final ResourceBundle logMsgBundle;
    private final ResourceBundle userMsgBundle;
    
    private final Map logMsgTemplates = new HashMap();
    
    private static LoggerMsgManager INSTANCE = new LoggerMsgManager();
    
    /**
     * 
     */
    public LoggerMsgManager() {
        logMsgBundle = ResourceBundle.getBundle( "logMsg" );
        userMsgBundle = ResourceBundle.getBundle( "userMsg" );
    }
    
    public String getLogMsg(final String key) {
        return getLogMsg(key, Collections.EMPTY_MAP);
    }
    
    // TODO: evtl. hier auf Context umsteigen
    public String getLogMsg(final String key, Map values) {
        if( key == null ) {
            throw new NullPointerException( "parameter:key" );
        }
        if( values == null ) {
            throw new NullPointerException( "parameter:values" );
        }
        final Object cachedTemplate = logMsgTemplates.get(key);
        if( cachedTemplate == null ) {
            AST root;
            synchronized( key ) {
                root = (AST) logMsgTemplates.get(key);
                if( root == null ) {
                    root = parseTemplate(key);
                    logMsgTemplates.put(key, root);
                }
            }
            return getMessage(root, values);
        } else {
            return getMessage((AST) cachedTemplate, values);
        }
    }
    
    /**
     * @param root
     * @param values
     * @return
     */
    private String getMessage(final AST root, final Map values) {
        final StringBuffer buffer = new StringBuffer();
        
        for (AST next  = root.getFirstChild(); next != null; next = next.getNextSibling()) {
             switch( next.getType()) {
                 case TemplateTreeParserTokenTypes.TEXT:
                     buffer.append( next.getText() );
                     break;
                 case TemplateTreeParserTokenTypes.REFERENCE:
                     final AST key = next.getFirstChild();
                     if( key == null ) {
                         buffer.append("${}" );
                     } else {
                         if( key.getFirstChild() != null ) {
                             throw new UnsupportedOperationException( "only firstIDs allowed. without subcalls. ");
                         }
                         final Object value = values.get(key.getText());
                         if( value == null ) {
                             buffer.append("${" );
                             buffer.append(key);
                             buffer.append('}');
                         } else {
                             buffer.append( String.valueOf(value) );   
                         }
                     }
                     break;
                  default: 
                      throw new UnsupportedOperationException( "unsupported type of elemenst fpr the template." );
             }
        }
        
        return buffer.toString();
    }

    /**
     * @param key
     * @return
     */
    private AST parseTemplate(final String key) {
        String msg = logMsgBundle.getString( key );
        if( msg == null ) {
            return null;
        } else {
            final TemplateTreeParser parser = Template.parse(msg);
            try {
                parser.parseStart();
                return parser.getAST();
            } catch (Exception e) {
                // TODO: Logging
                e.printStackTrace();
                return null;
            }
        }
    }

    public String getUserMsg(final String key) {
        String msg = logMsgBundle.getString( key );
        if( msg == null ) {
            return "Unknown problem. ID: " + key;
        } else {
            return msg;
        }
    }
    
    public static LoggerMsgManager getInstance() {
        return INSTANCE;
    }
}
