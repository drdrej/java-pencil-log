// $ANTLR : "TemplateASTParser.g" -> "TemplateTreeParser.java"$

package com.j2biz.pencil.el;



import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class TemplateTreeParser extends antlr.LLkParser       implements TemplateTreeParserTokenTypes
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
	
	  

protected TemplateTreeParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public TemplateTreeParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected TemplateTreeParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public TemplateTreeParser(TokenStream lexer) {
  this(lexer,1);
}

public TemplateTreeParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	private final void parseTemplate() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parseTemplate_AST = null;
		AST child_AST = null;
		
		try {      // for error handling
			{
			_loop240:
			do {
				if ((LA(1)==BEGIN_OF_REF||LA(1)==TEXT)) {
					textOrRef();
					child_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop240;
				}
				
			} while (true);
			}
			parseTemplate_AST = (AST)currentAST.root;
			parseTemplate_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TEMPLATE,"root")).add(parseTemplate_AST));
			currentAST.root = parseTemplate_AST;
			currentAST.child = parseTemplate_AST!=null &&parseTemplate_AST.getFirstChild()!=null ?
				parseTemplate_AST.getFirstChild() : parseTemplate_AST;
			currentAST.advanceChildToEnd();
			parseTemplate_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = parseTemplate_AST;
	}
	
	private final void textOrRef() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST textOrRef_AST = null;
		AST cf_AST = null;
		AST r_AST = null;
		Token  txt = null;
		AST txt_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BEGIN_OF_REF:
			{
				{
				match(BEGIN_OF_REF);
				{
				switch ( LA(1)) {
				case CLASS_ID:
				{
					{
					match(CLASS_ID);
					classFunction();
					cf_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					textOrRef_AST = (AST)currentAST.root;
					
						   textOrRef_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(CLASS_FUNCTION,"classFunction")).add(textOrRef_AST)); 
												   this.complex = true;
					currentAST.root = textOrRef_AST;
					currentAST.child = textOrRef_AST!=null &&textOrRef_AST.getFirstChild()!=null ?
						textOrRef_AST.getFirstChild() : textOrRef_AST;
					currentAST.advanceChildToEnd();
					}
					break;
				}
				case JAVA_ID:
				case OPEN_BRACKET:
				{
					{
					reference();
					r_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					textOrRef_AST = (AST)currentAST.root;
					
												      textOrRef_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(REFERENCE,"reference")).add(textOrRef_AST)); 
												      this.complex = true;
												
					currentAST.root = textOrRef_AST;
					currentAST.child = textOrRef_AST!=null &&textOrRef_AST.getFirstChild()!=null ?
						textOrRef_AST.getFirstChild() : textOrRef_AST;
					currentAST.advanceChildToEnd();
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				textOrRef_AST = (AST)currentAST.root;
				break;
			}
			case TEXT:
			{
				txt = LT(1);
				txt_AST = astFactory.create(txt);
				astFactory.addASTChild(currentAST, txt_AST);
				match(TEXT);
				textOrRef_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = textOrRef_AST;
	}
	
	private final void classFunction() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classFunction_AST = null;
		Token  function = null;
		AST function_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case REF_DOT:
			{
				{
				match(REF_DOT);
				function = LT(1);
				function_AST = astFactory.create(function);
				astFactory.makeASTRoot(currentAST, function_AST);
				match(JAVA_ID);
				function_AST.setText( function_AST.getText().trim() );
				}
				match(END_OF_REF);
				classFunction_AST = (AST)currentAST.root;
				break;
			}
			case END_OF_REF:
			{
				match(END_OF_REF);
				classFunction_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = classFunction_AST;
	}
	
	private final void reference() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST reference_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case OPEN_BRACKET:
			{
				scope();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case JAVA_ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			id = LT(1);
			id_AST = astFactory.create(id);
			astFactory.makeASTRoot(currentAST, id_AST);
			match(JAVA_ID);
			{
			switch ( LA(1)) {
			case REF_DOT:
			{
				idPart();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case END_OF_REF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(END_OF_REF);
						
				         final String idName = id_AST.getText().trim();
				         id_AST.setText( idName ); 
			
			reference_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = reference_AST;
	}
	
	private final void scope() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST scope_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		try {      // for error handling
			match(OPEN_BRACKET);
			id = LT(1);
			id_AST = astFactory.create(id);
			astFactory.makeASTRoot(currentAST, id_AST);
			match(JAVA_ID);
			{
			switch ( LA(1)) {
			case REF_DOT:
			{
				scopeIdPart();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case CLOSE_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(CLOSE_BRACKET);
			match(DOUBLE_POINT);
			
				 id_AST.setText( id_AST.getText().trim() );
				 id_AST.setType(SCOPE); 
				
			scope_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		returnAST = scope_AST;
	}
	
	private final void idPart() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST idPart_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		try {      // for error handling
			match(REF_DOT);
			{
			switch ( LA(1)) {
			case OPEN_BRACKET:
			{
				scope();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case JAVA_ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			id = LT(1);
			id_AST = astFactory.create(id);
			astFactory.makeASTRoot(currentAST, id_AST);
			match(JAVA_ID);
			{
			switch ( LA(1)) {
			case REF_DOT:
			{
				idPart();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case END_OF_REF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			
				 id_AST.setText( id_AST.getText().trim() ); 
			
			idPart_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = idPart_AST;
	}
	
	private final void scopeIdPart() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST scopeIdPart_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		try {      // for error handling
			match(REF_DOT);
			id = LT(1);
			id_AST = astFactory.create(id);
			astFactory.makeASTRoot(currentAST, id_AST);
			match(JAVA_ID);
			{
			switch ( LA(1)) {
			case REF_DOT:
			{
				scopeIdPart();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case CLOSE_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			id_AST.setText( id_AST.getText().trim() );
			scopeIdPart_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		returnAST = scopeIdPart_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"CLASS_FUNCTION",
		"TEMPLATE",
		"REFERENCE",
		"SCOPE",
		"BEGIN_OF_REF",
		"CLASS_ID",
		"TEXT",
		"JAVA_ID",
		"END_OF_REF",
		"REF_DOT",
		"OPEN_BRACKET",
		"CLOSE_BRACKET",
		"DOUBLE_POINT",
		"CLASS_ID_PREFIX",
		"JAVA_OR_CLASS_ID",
		"SPECIAL_ID_CHARS",
		"SIMPLE_LETTERS",
		"DIGITS",
		"OPEN_CURLY",
		"CLOSE_CURLY",
		"DOLLAR",
		"ESCAPE_CHARS",
		"WHITE_SPACE_CHARS",
		"MASKED",
		"JAVA_ID_START",
		"JAVA_ID_PART"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 1282L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 2048L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 4096L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 32768L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	
	}
