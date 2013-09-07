// $ANTLR : "TemplateASTParser.g" -> "TemplateTreeLexer.java"$

package com.j2biz.pencil.el;



import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

/**
 * Parsed den normalen Text, kümmert sich um
 * eingelegte Referenzen. Sobald eine Referenz kommt wird
 * das Token Referenz zurückgegeben.
 * Daraufhin erzeugt der Parser einen AST-Baum.
 */
public class TemplateTreeLexer extends antlr.CharScanner implements TemplateTreeParserTokenTypes, TokenStream
 {

	private boolean openBracket = false;
	private int beginOfRef      = 0;
public TemplateTreeLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public TemplateTreeLexer(Reader in) {
	this(new CharBuffer(in));
}
public TemplateTreeLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public TemplateTreeLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				if (((LA(1)=='$'))&&(!this.openBracket)) {
					mBEGIN_OF_REF(true);
					theRetToken=_returnToken;
				}
				else if (((LA(1)=='('))&&(this.openBracket)) {
					mOPEN_BRACKET(true);
					theRetToken=_returnToken;
				}
				else if (((LA(1)==')'))&&(this.openBracket)) {
					mCLOSE_BRACKET(true);
					theRetToken=_returnToken;
				}
				else if (((LA(1)==':'))&&(this.openBracket)) {
					mDOUBLE_POINT(true);
					theRetToken=_returnToken;
				}
				else if (((_tokenSet_0.member(LA(1))))&&(!this.openBracket)) {
					mTEXT(true);
					theRetToken=_returnToken;
				}
				else if (((LA(1)=='}'))&&(this.openBracket)) {
					mEND_OF_REF(true);
					theRetToken=_returnToken;
				}
				else if (((LA(1)=='.'))&&(this.openBracket)) {
					mREF_DOT(true);
					theRetToken=_returnToken;
				}
				else if (((_tokenSet_1.member(LA(1))))&&(this.openBracket)) {
					mJAVA_OR_CLASS_ID(true);
					theRetToken=_returnToken;
				}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mBEGIN_OF_REF(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BEGIN_OF_REF;
		int _saveIndex;
		
		if (!(!this.openBracket))
		  throw new SemanticException("!this.openBracket");
		match('$');
		match('{');
		{
		_loop260:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop260;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			this.openBracket = true;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mWHITE_SPACE_CHARS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WHITE_SPACE_CHARS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '\t':
		{
			match('\t');
			break;
		}
		case '\r':
		{
			match('\r');
			break;
		}
		case '\n':
		{
			match('\n');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOPEN_BRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OPEN_BRACKET;
		int _saveIndex;
		
		if (!(this.openBracket))
		  throw new SemanticException("this.openBracket");
		match('(');
		{
		_loop263:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop263;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCLOSE_BRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CLOSE_BRACKET;
		int _saveIndex;
		
		if (!(this.openBracket))
		  throw new SemanticException("this.openBracket");
		match(')');
		{
		_loop266:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop266;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
/**
 * Casting-delimiter. Zeigt das ende eines Casts an.
 * Der Doppelpunkt wird zwischen der Klammer und dem folgender Id einer Instanz,
 * die gecastet werden soll, angegeben.
 */
	public final void mDOUBLE_POINT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DOUBLE_POINT;
		int _saveIndex;
		
		if (!(this.openBracket))
		  throw new SemanticException("this.openBracket");
		match(':');
		{
		_loop269:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop269;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTEXT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TEXT;
		int _saveIndex;
		
		if (!(!this.openBracket))
		  throw new SemanticException("!this.openBracket");
		{
		int _cnt276=0;
		_loop276:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				{
				if ((LA(1)=='\\')) {
					{
					_saveIndex=text.length();
					match('\\');
					text.setLength(_saveIndex);
					{
					switch ( LA(1)) {
					case '{':
					{
						match('{');
						break;
					}
					case '$':
					{
						match('$');
						break;
					}
					default:
						{
						}
					}
					}
					}
				}
				else if ((_tokenSet_3.member(LA(1)))) {
					{
					match(_tokenSet_3);
					}
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
			}
			else {
				if ( _cnt276>=1 ) { break _loop276; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt276++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEND_OF_REF(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = END_OF_REF;
		int _saveIndex;
		
		if (!(this.openBracket))
		  throw new SemanticException("this.openBracket");
		match('}');
		if ( inputState.guessing==0 ) {
			this.openBracket = false;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mCLASS_ID_PREFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CLASS_ID_PREFIX;
		int _saveIndex;
		
		match("class");
		{
		_loop280:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop280;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case '.':
		{
			mREF_DOT(false);
			break;
		}
		case '}':
		{
			match('}');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mREF_DOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = REF_DOT;
		int _saveIndex;
		
		if (!(this.openBracket))
		  throw new SemanticException("this.openBracket");
		match('.');
		{
		_loop298:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop298;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mCLASS_ID(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CLASS_ID;
		int _saveIndex;
		
		match("class");
		{
		_loop284:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop284;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mJAVA_ID(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVA_ID;
		int _saveIndex;
		
		mJAVA_ID_START(false);
		{
		_loop287:
		do {
			if ((_tokenSet_4.member(LA(1)))) {
				mJAVA_ID_PART(false);
			}
			else {
				break _loop287;
			}
			
		} while (true);
		}
		{
		_loop289:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				mWHITE_SPACE_CHARS(false);
			}
			else {
				break _loop289;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mJAVA_ID_START(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVA_ID_START;
		int _saveIndex;
		
		if (((_tokenSet_5.member(LA(1))))&&(this.openBracket)) {
			mSIMPLE_LETTERS(false);
		}
		else if ((LA(1)=='$'||LA(1)=='_')) {
			mSPECIAL_ID_CHARS(false);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mJAVA_ID_PART(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVA_ID_PART;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			mDIGITS(false);
			break;
		}
		case '$':  case '_':
		{
			mSPECIAL_ID_CHARS(false);
			break;
		}
		default:
			if (((_tokenSet_5.member(LA(1))))&&(this.openBracket)) {
				mSIMPLE_LETTERS(false);
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mJAVA_OR_CLASS_ID(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVA_OR_CLASS_ID;
		int _saveIndex;
		
		if (!(this.openBracket))
		  throw new SemanticException("this.openBracket");
		{
		boolean synPredMatched293 = false;
		if (((LA(1)=='c'))) {
			int _m293 = mark();
			synPredMatched293 = true;
			inputState.guessing++;
			try {
				{
				mCLASS_ID_PREFIX(false);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched293 = false;
			}
			rewind(_m293);
inputState.guessing--;
		}
		if ( synPredMatched293 ) {
			mCLASS_ID(false);
			if ( inputState.guessing==0 ) {
				_ttype = CLASS_ID;
			}
		}
		else {
			boolean synPredMatched295 = false;
			if (((_tokenSet_1.member(LA(1))))) {
				int _m295 = mark();
				synPredMatched295 = true;
				inputState.guessing++;
				try {
					{
					mJAVA_ID(false);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched295 = false;
				}
				rewind(_m295);
inputState.guessing--;
			}
			if ( synPredMatched295 ) {
				mJAVA_ID(false);
				if ( inputState.guessing==0 ) {
					_ttype = JAVA_ID;
				}
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
			}
			_returnToken = _token;
		}
		
	private final void mSPECIAL_ID_CHARS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SPECIAL_ID_CHARS;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '_':
		{
			match('_');
			break;
		}
		case '$':
		{
			match('$');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mSIMPLE_LETTERS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SIMPLE_LETTERS;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			{
			matchRange('a','z');
			}
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			{
			matchRange('A','Z');
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mDIGITS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGITS;
		int _saveIndex;
		
		{
		matchRange('0','9');
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mOPEN_CURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OPEN_CURLY;
		int _saveIndex;
		
		match('{');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mCLOSE_CURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CLOSE_CURLY;
		int _saveIndex;
		
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mDOLLAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DOLLAR;
		int _saveIndex;
		
		match('$');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mESCAPE_CHARS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESCAPE_CHARS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'n':
		{
			match('n');
			break;
		}
		case 'r':
		{
			match('r');
			break;
		}
		case 't':
		{
			match('t');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	private final void mMASKED(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MASKED;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '{':
		{
			mOPEN_CURLY(false);
			break;
		}
		case '}':
		{
			mCLOSE_CURLY(false);
			break;
		}
		case 'n':  case 'r':  case 't':
		{
			mESCAPE_CHARS(false);
			break;
		}
		case ':':
		{
			mDOUBLE_POINT(false);
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[2048];
		data[0]=-68719476738L;
		data[1]=-576460752303423489L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[1025];
		data[0]=68719476736L;
		data[1]=576460745995190270L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[1025];
		data[0]=4294977024L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2048];
		data[0]=-68719476738L;
		data[1]=-576460752571858945L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[1025];
		data[0]=287948969894477824L;
		data[1]=576460745995190270L;
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[1025];
		data[1]=576460743847706622L;
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	
	}
