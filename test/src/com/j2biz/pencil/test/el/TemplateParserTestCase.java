/*
 * Copyright 2004 Andreas Siebert (j2biz community)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.j2biz.pencil.test.el;


import junit.framework.TestCase;
import antlr.collections.AST;

import com.j2biz.pencil.el.TemplateTreeParser;
import com.j2biz.pencil.el.TemplateTreeParserTokenTypes;


/**
 * @author andrej
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TemplateParserTestCase extends TestCase {

    /**
     * Dieser Abschnitt testet den Einsatz von einfachen Referenzen. 
     */
    public void testLengthNull() { 
        TemplateTreeParser parser = TemplateParserFactory.createParser( "" );
        try {
            parser.parseStart();
        } catch (Exception e) {
            fail( "No exception expected." );
        }
    }
    
    public void testSimpleRef() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${idName.idName2.parameter}afterText" );
        try {
            parser.parseStart();
        } catch (Exception e) {
            e.printStackTrace();
            fail( "No exception expected." );
        }
    }
    
    public void testSecondIdBeginsWithDigit() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${idName.1idName2}afterText" );
        try {
            parser.parseStart();
            fail( "exception expected." );
        } catch (Exception e) {
            ;
        }
    }
    
    public void testNoClosedBracket() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${idName.idName2{.parameter}afterText" );
        try {
            parser.parseStart();
            fail( "exception expected." );
        } catch (Exception e) {
            ;
        }
    }
    
    
    public void testASTSimpleConstruction() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${idName}afterText" );
        try {
            parser.parseStart();
            final AST root = parser.getAST();
            assertEquals("root correct", root.getType(), TemplateTreeParserTokenTypes.TEMPLATE);
            assertEquals("number of elements", root.getNumberOfChildren(), 3);
            
            final AST before = root.getFirstChild();
            assertEquals("before text node", before.getType(), TemplateTreeParserTokenTypes.TEXT);
            final AST reference = before.getNextSibling();
            assertEquals("reference", reference.getType(), TemplateTreeParserTokenTypes.REFERENCE);
            final AST after = reference.getNextSibling();
            assertEquals("after", after.getType(), TemplateTreeParserTokenTypes.TEXT);
            
            final AST idName = reference.getFirstChild();
            assertEquals("first child", idName.getText(), "idName");

        } catch (Exception e) {
            e.printStackTrace();
            fail( "No exception expected.");
        }
    }
    
    public void testASTSimple2Construction() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${idName.idName.idName2}afterText" );
        try {
            parser.parseStart();
            final AST root = parser.getAST();
            assertEquals("root correct", root.getType(), TemplateTreeParserTokenTypes.TEMPLATE);
            assertEquals("number of elements", root.getNumberOfChildren(), 3);
            
            final AST before = root.getFirstChild();
            assertEquals("before text node", before.getType(), TemplateTreeParserTokenTypes.TEXT);
            final AST reference = before.getNextSibling();
            assertEquals("reference", reference.getType(), TemplateTreeParserTokenTypes.REFERENCE);
            final AST after = reference.getNextSibling();
            assertEquals("after", after.getType(), TemplateTreeParserTokenTypes.TEXT);
            
            final AST idName = reference.getFirstChild();
            assertEquals("first child", idName.getText(), "idName");
            final AST idNameChild = idName.getFirstChild();
            assertEquals("next child but name == idName", idNameChild.getText(), "idName" );
            final AST idName2 = idNameChild.getFirstChild();
            assertEquals("last child: idName2", idName2.getText(), "idName2");
        } catch (Exception e) {
            e.printStackTrace();
            fail( "No exception expected.");
        }
    }
    
    public void testASTScope() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${(TestScope):idName.(com.TestScope2):idName2}afterText" );
        try {
            parser.parseStart();
            final AST root = parser.getAST();
            assertEquals("root correct", root.getType(), TemplateTreeParserTokenTypes.TEMPLATE);
            assertEquals("number of elements", root.getNumberOfChildren(), 3);
            
            final AST before = root.getFirstChild();
            assertEquals("before text node", before.getType(), TemplateTreeParserTokenTypes.TEXT);
            final AST reference = before.getNextSibling();
            assertEquals("reference", reference.getType(), TemplateTreeParserTokenTypes.REFERENCE);
            final AST after = reference.getNextSibling();
            assertEquals("after", after.getType(), TemplateTreeParserTokenTypes.TEXT);
            
            final AST idName = reference.getFirstChild();
            assertEquals("first child", idName.getText(), "idName");
            
            final AST scope = idName.getFirstChild();
            assertNotNull("scope should be not NULL:", scope);
            assertEquals("token type is SCOPE:", scope.getType(), TemplateTreeParserTokenTypes.SCOPE);
            assertEquals("scope is setted:", scope.getText(), "TestScope");
            
            final AST idName2 = scope.getNextSibling();
            assertNotNull("next part of id must be not NULL ", idName2);
            assertEquals("id-name == idName2", idName2.getText(), "idName2" );
            
            final AST scope2firstPart = idName2.getFirstChild();
            assertNotNull("first part of the second scope must be not NULL", scope2firstPart );
            assertEquals("id == com", scope2firstPart.getText(),"com");
            assertEquals("type == scope", scope2firstPart.getType(), TemplateTreeParserTokenTypes.SCOPE);
            
            final AST scope2secondPart = scope2firstPart.getFirstChild();
            assertNotNull( "second part of the second scope must be not NULL", scope2secondPart);
            assertEquals("id == TestScope2", scope2secondPart.getText(), "TestScope2" );
            
        } catch (Exception e) {
            e.printStackTrace();
            fail( "No exception expected.");
        }
    }
    
    
    
    
    public void testASTScopeWithWhiteSpaces() {
        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${    ( TestScope  ) :    idName . ( com  . TestScope2  )  : idName2   }afterText" );
        try {
            parser.parseStart();
            final AST root = parser.getAST();
            assertEquals("root correct", root.getType(), TemplateTreeParserTokenTypes.TEMPLATE);
            assertEquals("number of elements", root.getNumberOfChildren(), 3);
            
            final AST before = root.getFirstChild();
            assertEquals("before text node", before.getType(), TemplateTreeParserTokenTypes.TEXT);
            final AST reference = before.getNextSibling();
            assertEquals("reference", reference.getType(), TemplateTreeParserTokenTypes.REFERENCE);
            final AST after = reference.getNextSibling();
            assertEquals("after", after.getType(), TemplateTreeParserTokenTypes.TEXT);
            
            final AST idName = reference.getFirstChild();
            assertEquals("first child", idName.getText(), "idName");
            
            final AST scope = idName.getFirstChild();
            assertNotNull("scope should be not NULL:", scope);
            assertEquals("token type is SCOPE:", scope.getType(), TemplateTreeParserTokenTypes.SCOPE);
            assertEquals("scope is setted:", scope.getText(), "TestScope");
            
            final AST idName2 = scope.getNextSibling();
            assertNotNull("next part of id must be not NULL ", idName2);
            assertEquals("id-name == idName2", idName2.getText(), "idName2" );
            
            final AST scope2firstPart = idName2.getFirstChild();
            assertNotNull("first part of the second scope must be not NULL", scope2firstPart );
            assertEquals("id == com", scope2firstPart.getText(),"com");
            assertEquals("type == scope", scope2firstPart.getType(), TemplateTreeParserTokenTypes.SCOPE);
            
            final AST scope2secondPart = scope2firstPart.getFirstChild();
            assertNotNull( "second part of the second scope must be not NULL", scope2secondPart);
            assertEquals("id == TestScope2", scope2secondPart.getText(), "TestScope2" );
            
        } catch (Exception e) {
            e.printStackTrace();
            fail( "No exception expected.");
        }
    }
    
    
//    
//    
//    public void testASTScopeCheckJavaBeginningNameChars() {
////    	Character.isJavaIdentifierStart()
//        TemplateTreeParser parser = TemplateParserFactory.createParser( "textBefore${    ( ÜberScope  ) :    äTidName . ( com  . öTestScope2  )  : ?dName2   }afterText" );
//        try {
//            parser.parseStart();
//            final AST root = parser.getAST();
//            assertEquals("root correct", root.getType(), TemplateTreeParserTokenTypes.TEMPLATE);
//            assertEquals("number of elements", root.getNumberOfChildren(), 3);
//            
//            final AST before = root.getFirstChild();
//            assertEquals("before text node", before.getType(), TemplateTreeParserTokenTypes.TEXT);
//            final AST reference = before.getNextSibling();
//            assertEquals("reference", reference.getType(), TemplateTreeParserTokenTypes.REFERENCE);
//            final AST after = reference.getNextSibling();
//            assertEquals("after", after.getType(), TemplateTreeParserTokenTypes.TEXT);
//            
//            final AST idName = reference.getFirstChild();
//            assertEquals("first child", idName.getText(), "idName");
//            
//            final AST scope = idName.getFirstChild();
//            assertNotNull("scope should be not NULL:", scope);
//            assertEquals("token type is SCOPE:", scope.getType(), TemplateTreeParserTokenTypes.SCOPE);
//            assertEquals("scope is setted:", scope.getText(), "TestScope");
//            
//            final AST idName2 = scope.getNextSibling();
//            assertNotNull("next part of id must be not NULL ", idName2);
//            assertEquals("id-name == idName2", idName2.getText(), "idName2" );
//            
//            final AST scope2firstPart = idName2.getFirstChild();
//            assertNotNull("first part of the second scope must be not NULL", scope2firstPart );
//            assertEquals("id == com", scope2firstPart.getText(),"com");
//            assertEquals("type == scope", scope2firstPart.getType(), TemplateTreeParserTokenTypes.SCOPE);
//            
//            final AST scope2secondPart = scope2firstPart.getFirstChild();
//            assertNotNull( "second part of the second scope must be not NULL", scope2secondPart);
//            assertEquals("id == TestScope2", scope2secondPart.getText(), "TestScope2" );
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail( "No exception expected.");
//        }
//    }
    
}
