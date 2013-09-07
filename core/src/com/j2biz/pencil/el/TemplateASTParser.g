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
header {
package com.j2biz.pencil.el;


}

class TemplateTreeParser extends Parser;
options {
	buildAST = true;
}

tokens {
   CLASS_FUNCTION;
   TEMPLATE;
   REFERENCE;
   SCOPE;
}

{   

	/**
     * true if message contains references
     */
    private boolean complex = false;
	
	public boolean isComplex() {
		return complex;
	}
	
	
	public void parseStart() throws Exception {
		parseTemplate();
	} 
	
	  
}

private parseTemplate
                : ( child:textOrRef )* { #parseTemplate = #([TEMPLATE, "root"], #parseTemplate); };  
              
private textOrRef 
                : (BEGIN_OF_REF! 
                          (  (CLASS_ID! cf:classFunction {
                        	   #textOrRef = #([CLASS_FUNCTION, "classFunction" ], #textOrRef); 
							   this.complex = true; } 
							 ) | 
							 (r:reference 
							   {  
							      #textOrRef = #([REFERENCE, "reference" ], #textOrRef); 
							      this.complex = true;
							   } )
						)
                  )
                | txt:TEXT 
        ;                    

// TODO: es ist evtl. besser statt zu trimmen lieber die entsprechenden chars direct vom lexer rauswerfen.
private reference: (scope)? id:JAVA_ID^ (idPart)? END_OF_REF! { 			
	         final String idName = #id.getText().trim();
	         #id.setText( idName ); 
};

private classFunction: 
    (REF_DOT! function:JAVA_ID^ { #function.setText( #function.getText().trim() ); }) END_OF_REF!
    | END_OF_REF!;

private idPart: REF_DOT! (scope)? id:JAVA_ID^ (idPart)?     
{ 
	 #id.setText( #id.getText().trim() ); 
};

private scope: OPEN_BRACKET! id:JAVA_ID^ (scopeIdPart)? CLOSE_BRACKET! DOUBLE_POINT! 
    {
	 #id.setText( #id.getText().trim() );
	 #id.setType(SCOPE); 
	};        

private scopeIdPart: REF_DOT! id:JAVA_ID^ (scopeIdPart)?
    { #id.setText( #id.getText().trim() ); };


/**
 * Parsed den normalen Text, kümmert sich um
 * eingelegte Referenzen. Sobald eine Referenz kommt wird
 * das Token Referenz zurückgegeben.
 * Daraufhin erzeugt der Parser einen AST-Baum.
 */
class TemplateTreeLexer extends Lexer;
options { 
	k=1;
	charVocabulary = '\u0001'..'\uFFFE';
}
{
	private boolean openBracket = false;
	private int beginOfRef      = 0;
}

/*
 * -----------------------------------------------------------------
 * PUBLIC-Methoden, d.h. Methoden, die der Parser sieht und aufruft.
 * -----------------------------------------------------------------
 */ 

// muss schauen, dass letzter Buchstabe ~'\\' war.
public BEGIN_OF_REF:
   {!this.openBracket}? '$' '{' (WHITE_SPACE_CHARS)*
       { this.openBracket = true; };
       
public OPEN_BRACKET: {this.openBracket}?  '(' (WHITE_SPACE_CHARS)*;

public CLOSE_BRACKET: {this.openBracket}? ')' (WHITE_SPACE_CHARS)*;

/**
 * Casting-delimiter. Zeigt das ende eines Casts an.
 * Der Doppelpunkt wird zwischen der Klammer und dem folgender Id einer Instanz,
 * die gecastet werden soll, angegeben.
 */
public DOUBLE_POINT : {this.openBracket}? ':' (WHITE_SPACE_CHARS)*;

public TEXT: 
   {!this.openBracket}? ((
    ('\\'! 
        ('{' || '$')  // an dieser Stelle alle Markierten erlauben. 
                      // \\${ ist nicht erlaubt und wird eine Exception werfen
    ) 
    | ~('\\' | '{' | '$') ))+ // d.h. keine unmarkierte Sonderzeichen.
;

// return TOKEN:TEXT, falls etwas gefunden wurde, was nicht dem JAVA_ID entspricht.
// d.h. syntaktisch zumindest entspricht.
// anschliessend wird die Zeichenkette zurückgegeben
public END_OF_REF:
   {this.openBracket}? '}' { this.openBracket = false; };



private CLASS_ID_PREFIX: "class" (WHITE_SPACE_CHARS)* (REF_DOT | '}');

protected CLASS_ID: "class" (WHITE_SPACE_CHARS)* ;

protected JAVA_ID: JAVA_ID_START (JAVA_ID_PART)* (WHITE_SPACE_CHARS)*;

public JAVA_OR_CLASS_ID : {this.openBracket}?
(

   (CLASS_ID_PREFIX) => CLASS_ID { $setType(CLASS_ID); }
   | (JAVA_ID) => JAVA_ID { $setType(JAVA_ID); }
 
);    
   


public REF_DOT: {this.openBracket}? '.' (WHITE_SPACE_CHARS)*;

/* -----------------------------------------------------------------
 * Hilfstokens.
 * Diese Methoden definieren einfache Tokens
 * -----------------------------------------------------------------
 */
 
// Die IDENTIFIER koennen auch andere Buchstaben enthalten...
// An dieser Stelle sich Gedanken machen
private SPECIAL_ID_CHARS : '_' | '$';

private SIMPLE_LETTERS: ('a'..'z') | ('A'.. 'Z');

private DIGITS: ('0' .. '9');

private OPEN_CURLY: '{';

private CLOSE_CURLY: '}';

private DOLLAR: '$'; 

private ESCAPE_CHARS: ('n' | 'r' | 't' );

private WHITE_SPACE_CHARS: (' ' | '\t' | '\r' | '\n' );

private MASKED: (OPEN_CURLY | CLOSE_CURLY | ESCAPE_CHARS | DOUBLE_POINT );

private JAVA_ID_START: {this.openBracket}? SIMPLE_LETTERS | SPECIAL_ID_CHARS ; 
//{ Character.isJavaIdentifierStart( LA(1) ) }? ;
//

private JAVA_ID_PART: {this.openBracket}? SIMPLE_LETTERS | DIGITS | SPECIAL_ID_CHARS ;

// { Character.isJavaIdentifierPart( LA(1) ) }? ;
//

