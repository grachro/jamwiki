/* The following code was generated by JFlex 1.3.5 on 6/15/06 12:07 PM */

package org.vqwiki.lex;


/*
Very Quick Wiki - WikiWikiWeb clone
Copyright (C) 2001-2003 Gareth Cronin

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program (gpl.txt); if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

import org.apache.log4j.Logger;
import org.vqwiki.WikiBase;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.3.5
 * on 6/15/06 12:07 PM from the specification file
 * <tt>file:/E:/code/workspace/vqwiki/branches/ryan/vqwiki/src/lex/format.jflex</tt>
 */
public class FormatLex implements org.vqwiki.lex.Lexer {

  /** This character denotes the end of file */
  final public static int YYEOF = -1;

  /** initial size of the lookahead buffer */
  final private static int YY_BUFFERSIZE = 16384;

  /** lexical states */
  final public static int YYINITIAL = 0;
  final public static int EXTERNAL = 8;
  final public static int PRE = 6;
  final public static int OFF = 4;
  final public static int NORMAL = 2;

  /**
   * YY_LEXSTATE[l] is the state in the DFA for the lexical state l
   * YY_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private final static int YY_LEXSTATE[] = { 
     0,  0,  1,  2,  3,  3,  4,  4,  5, 5
  };

  /** 
   * Translates characters to character classes
   */
  final private static String yycmap_packed = 
    "\11\0\1\3\1\1\2\0\1\2\22\0\1\3\1\24\1\0\1\14"+
    "\1\0\1\17\1\13\1\25\7\0\1\12\12\21\1\26\1\16\1\6"+
    "\1\27\1\10\1\0\1\32\6\22\24\20\1\5\1\23\1\11\1\0"+
    "\1\4\1\0\1\36\5\22\1\35\4\20\1\33\1\37\2\20\1\40"+
    "\3\20\1\34\6\20\1\30\1\0\1\31\54\0\1\7\12\0\1\7"+
    "\4\0\1\7\5\0\27\7\1\0\37\7\1\0\u013f\7\31\0\162\7"+
    "\4\0\14\7\16\0\5\7\11\0\1\7\213\0\1\7\13\0\1\7"+
    "\1\0\3\7\1\0\1\7\1\0\24\7\1\0\54\7\1\0\46\7"+
    "\1\0\5\7\4\0\202\7\10\0\105\7\1\0\46\7\2\0\2\7"+
    "\6\0\20\7\41\0\46\7\2\0\1\7\7\0\47\7\110\0\33\7"+
    "\5\0\3\7\56\0\32\7\5\0\13\7\25\0\12\15\4\0\2\7"+
    "\1\0\143\7\1\0\1\7\17\0\2\7\7\0\2\7\12\15\3\7"+
    "\2\0\1\7\20\0\1\7\1\0\36\7\35\0\3\7\60\0\46\7"+
    "\13\0\1\7\u0152\0\66\7\3\0\1\7\22\0\1\7\7\0\12\7"+
    "\4\0\12\15\25\0\10\7\2\0\2\7\2\0\26\7\1\0\7\7"+
    "\1\0\1\7\3\0\4\7\3\0\1\7\36\0\2\7\1\0\3\7"+
    "\4\0\12\15\2\7\23\0\6\7\4\0\2\7\2\0\26\7\1\0"+
    "\7\7\1\0\2\7\1\0\2\7\1\0\2\7\37\0\4\7\1\0"+
    "\1\7\7\0\12\15\2\0\3\7\20\0\11\7\1\0\3\7\1\0"+
    "\26\7\1\0\7\7\1\0\2\7\1\0\5\7\3\0\1\7\22\0"+
    "\1\7\17\0\2\7\4\0\12\15\25\0\10\7\2\0\2\7\2\0"+
    "\26\7\1\0\7\7\1\0\2\7\1\0\5\7\3\0\1\7\36\0"+
    "\2\7\1\0\3\7\4\0\12\15\1\0\1\7\21\0\1\7\1\0"+
    "\6\7\3\0\3\7\1\0\4\7\3\0\2\7\1\0\1\7\1\0"+
    "\2\7\3\0\2\7\3\0\3\7\3\0\10\7\1\0\3\7\55\0"+
    "\11\15\25\0\10\7\1\0\3\7\1\0\27\7\1\0\12\7\1\0"+
    "\5\7\46\0\2\7\4\0\12\15\25\0\10\7\1\0\3\7\1\0"+
    "\27\7\1\0\12\7\1\0\5\7\3\0\1\7\40\0\1\7\1\0"+
    "\2\7\4\0\12\15\25\0\10\7\1\0\3\7\1\0\27\7\1\0"+
    "\20\7\46\0\2\7\4\0\12\15\25\0\22\7\3\0\30\7\1\0"+
    "\11\7\1\0\1\7\2\0\7\7\72\0\60\7\1\0\2\7\14\0"+
    "\7\7\11\0\12\15\47\0\2\7\1\0\1\7\2\0\2\7\1\0"+
    "\1\7\2\0\1\7\6\0\4\7\1\0\7\7\1\0\3\7\1\0"+
    "\1\7\1\0\1\7\2\0\2\7\1\0\4\7\1\0\2\7\11\0"+
    "\1\7\2\0\5\7\1\0\1\7\11\0\12\15\2\0\2\7\42\0"+
    "\1\7\37\0\12\15\26\0\10\7\1\0\42\7\35\0\4\7\164\0"+
    "\42\7\1\0\5\7\1\0\2\7\25\0\12\15\6\0\6\7\112\0"+
    "\46\7\12\0\51\7\7\0\132\7\5\0\104\7\5\0\122\7\6\0"+
    "\7\7\1\0\77\7\1\0\1\7\1\0\4\7\2\0\7\7\1\0"+
    "\1\7\1\0\4\7\2\0\47\7\1\0\1\7\1\0\4\7\2\0"+
    "\37\7\1\0\1\7\1\0\4\7\2\0\7\7\1\0\1\7\1\0"+
    "\4\7\2\0\7\7\1\0\7\7\1\0\27\7\1\0\37\7\1\0"+
    "\1\7\1\0\4\7\2\0\7\7\1\0\47\7\1\0\23\7\16\0"+
    "\11\15\56\0\125\7\14\0\u026c\7\2\0\10\7\12\0\32\7\5\0"+
    "\113\7\25\0\15\7\1\0\4\7\16\0\22\7\16\0\22\7\16\0"+
    "\15\7\1\0\3\7\17\0\64\7\43\0\1\7\4\0\1\7\3\0"+
    "\12\15\46\0\12\15\6\0\130\7\10\0\51\7\127\0\35\7\51\0"+
    "\12\15\36\7\2\0\5\7\u038b\0\154\7\224\0\234\7\4\0\132\7"+
    "\6\0\26\7\2\0\6\7\2\0\46\7\2\0\6\7\2\0\10\7"+
    "\1\0\1\7\1\0\1\7\1\0\1\7\1\0\37\7\2\0\65\7"+
    "\1\0\7\7\1\0\1\7\3\0\3\7\1\0\7\7\3\0\4\7"+
    "\2\0\6\7\4\0\15\7\5\0\3\7\1\0\7\7\164\0\1\7"+
    "\15\0\1\7\202\0\1\7\4\0\1\7\2\0\12\7\1\0\1\7"+
    "\3\0\5\7\6\0\1\7\1\0\1\7\1\0\1\7\1\0\4\7"+
    "\1\0\3\7\1\0\7\7\3\0\3\7\5\0\5\7\u0ebb\0\2\7"+
    "\52\0\5\7\5\0\2\7\4\0\126\7\6\0\3\7\1\0\132\7"+
    "\1\0\4\7\5\0\50\7\4\0\136\7\21\0\30\7\70\0\20\7"+
    "\u0200\0\u19b6\7\112\0\u51a6\7\132\0\u048d\7\u0773\0\u2ba4\7\u215c\0\u012e\7"+
    "\2\0\73\7\225\0\7\7\14\0\5\7\5\0\1\7\1\0\12\7"+
    "\1\0\15\7\1\0\5\7\1\0\1\7\1\0\2\7\1\0\2\7"+
    "\1\0\154\7\41\0\u016b\7\22\0\100\7\2\0\66\7\50\0\14\7"+
    "\164\0\5\7\1\0\207\7\23\0\12\15\7\0\32\7\6\0\32\7"+
    "\13\0\131\7\3\0\6\7\2\0\6\7\2\0\6\7\2\0\3\7"+
    "\43\0";

  /** 
   * Translates characters to character classes
   */
  final private static char [] yycmap = yy_unpack_cmap(yycmap_packed);

  /** 
   * Translates a state to a row index in the transition table
   */
  final private static int yy_rowMap [] = { 
        0,    33,    66,    99,   132,   165,     0,     0,   198,     0, 
      231,   264,     0,     0,   297,   330,   363,   396,   429,   462, 
      495,   528,   561,   594,   627,   660,   693,     0,   726,   759, 
        0,     0,   792,   825,   858,   891,   924,   957,     0,   990, 
     1023,  1056,     0,  1089,  1122,  1155,  1188,  1221,  1254,     0, 
        0,  1287,   660,  1320,  1353,  1386,  1419,  1452,  1485,  1518, 
        0,     0,     0,     0,     0,     0,  1551,  1584,  1617,  1650, 
     1683,  1716,     0,     0,     0,  1749,  1782,  1815,     0,  1848, 
     1881,  1914,  1947,  1980,  2013,     0,     0,  2046,     0,  2079, 
     2112,  2145,  2178,  2211,  2244,  2277,     0,  2310,  2343,  2376, 
        0,  2409,  2442,  2475,     0,  2508
  };

  /** 
   * The packed transition table of the DFA (part 0)
   */
  final private static String yy_packed0 = 
    "\41\0\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\7\1\16\2\7\1\17\3\7\1\20\3\7\1\21"+
    "\1\7\1\22\1\23\1\24\1\25\1\26\1\27\7\7"+
    "\1\10\1\11\1\12\1\13\1\14\1\15\1\7\1\16"+
    "\2\7\1\17\3\7\1\20\3\7\1\21\1\30\1\22"+
    "\1\23\1\24\1\25\1\26\1\27\7\7\1\10\1\11"+
    "\1\12\1\31\1\7\1\15\1\7\1\16\2\7\1\17"+
    "\26\7\1\32\1\33\1\34\1\35\1\14\1\15\1\7"+
    "\1\16\2\7\1\17\7\7\1\21\16\7\3\12\1\7"+
    "\1\36\33\7\1\0\1\10\43\0\1\37\6\0\1\40"+
    "\33\0\1\41\46\0\1\42\16\0\1\43\1\0\1\44"+
    "\1\45\16\0\1\46\2\0\1\47\1\50\1\0\1\50"+
    "\10\0\6\50\4\0\1\51\61\0\1\52\41\0\1\53"+
    "\41\0\1\54\41\0\1\55\41\0\1\56\41\0\1\57"+
    "\6\0\1\60\1\0\22\60\1\61\14\60\4\0\1\62"+
    "\35\0\1\63\1\64\37\0\1\65\43\0\1\37\42\0"+
    "\1\66\41\0\1\67\10\0\1\67\1\0\1\67\10\0"+
    "\6\67\15\0\1\70\3\0\1\70\53\0\1\71\40\0"+
    "\1\72\43\0\1\73\22\0\2\74\13\0\1\74\21\0"+
    "\1\75\1\50\1\0\1\50\10\0\6\50\4\0\1\76"+
    "\61\0\1\77\42\0\1\100\41\0\1\101\41\0\1\102"+
    "\41\0\1\103\6\0\1\60\1\0\22\60\1\104\14\60"+
    "\1\105\1\0\22\105\1\106\14\105\1\0\1\63\51\0"+
    "\1\107\35\0\1\67\1\110\7\0\1\67\1\0\1\67"+
    "\10\0\6\67\15\0\1\70\1\111\2\0\1\70\35\0"+
    "\1\112\40\0\1\113\62\0\1\114\21\0\2\115\13\0"+
    "\1\115\34\0\1\116\6\0\1\60\1\117\1\120\21\60"+
    "\1\104\14\60\1\105\1\0\22\105\1\121\14\105\1\122"+
    "\1\117\1\123\21\122\1\124\14\122\7\0\1\125\10\0"+
    "\1\125\1\0\1\125\10\0\6\125\11\0\1\126\45\0"+
    "\1\127\43\0\2\130\13\0\1\130\3\0\1\131\1\132"+
    "\36\0\1\60\1\117\22\60\1\104\14\60\1\105\1\117"+
    "\1\133\21\105\1\134\14\105\1\122\1\0\22\122\1\135"+
    "\15\122\1\117\22\122\1\135\15\122\1\117\1\123\21\122"+
    "\1\136\14\122\7\0\1\125\1\137\7\0\1\125\1\0"+
    "\1\125\10\0\6\125\21\0\2\140\13\0\1\140\3\0"+
    "\1\131\37\0\1\105\1\117\22\105\1\121\15\105\1\141"+
    "\1\142\21\105\1\134\14\105\1\122\1\117\1\123\21\122"+
    "\1\143\15\122\1\141\1\144\21\122\1\143\14\122\11\0"+
    "\1\145\50\0\2\146\13\0\1\146\2\0\1\105\1\141"+
    "\22\105\1\121\14\105\1\122\1\141\1\144\21\122\1\147"+
    "\15\122\1\141\22\122\1\135\14\122\21\0\2\150\13\0"+
    "\1\150\2\0\1\122\1\151\1\152\21\122\1\147\14\122"+
    "\17\0\1\75\21\0\1\122\1\151\22\122\1\135\14\122";

  /** 
   * The transition table of the DFA
   */
  final private static int yytrans [] = yy_unpack();


  /* error codes */
  final private static int YY_UNKNOWN_ERROR = 0;
  final private static int YY_ILLEGAL_STATE = 1;
  final private static int YY_NO_MATCH = 2;
  final private static int YY_PUSHBACK_2BIG = 3;

  /* error messages for the codes above */
  final private static String YY_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Internal error: unknown state",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * YY_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private final static byte YY_ATTRIBUTE[] = {
     8,  0,  0,  0,  0,  0,  9,  9,  1,  9,  1,  1,  9,  9,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  9,  1,  1,  9,  9, 
     0,  0,  0,  0,  0,  0,  9,  0,  0,  1,  9,  0,  0,  0,  1,  0, 
     0,  9,  9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  9,  9,  9,  9, 
     9,  9,  0,  0,  0,  0,  0,  0,  9,  9,  9,  0,  0,  0,  9,  0, 
     0,  0,  0,  0,  0,  9,  9,  0,  9,  0,  0,  0,  0,  0,  0,  0, 
     9,  0,  0,  0,  9,  0,  0,  0,  9,  0
  };

  /** the input device */
  private java.io.Reader yy_reader;

  /** the current state of the DFA */
  private int yy_state;

  /** the current lexical state */
  private int yy_lexical_state = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char yy_buffer[] = new char[YY_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int yy_markedPos;

  /** the textposition at the last state to be included in yytext */
  private int yy_pushbackPos;

  /** the current text position in the buffer */
  private int yy_currentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int yy_startRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int yy_endRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn; 

  /** 
   * yy_atBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean yy_atBOL = true;

  /** yy_atEOF == true <=> the scanner is at the EOF */
  private boolean yy_atEOF;

  /* user code: */
	protected boolean em, strong, underline, center, table, row, cell, allowHtml, code, h1, h2, h3, color;
  protected int listLevel;
  protected boolean ordered;
	protected static Logger cat = Logger.getLogger( FormatLex.class );

  protected String virtualWiki;

	protected boolean exists( String topic ){
	  try{
	    return WikiBase.getInstance().exists( virtualWiki, topic );
	  }catch( Exception err ){
	    cat.error( err );
	  }
	  return false;
	}

  public void setVirtualWiki( String vWiki ){
    this.virtualWiki = vWiki;
  }



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public FormatLex(java.io.Reader in) {
  	yybegin( NORMAL );
    this.yy_reader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public FormatLex(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the split, compressed DFA transition table.
   *
   * @return the unpacked transition table
   */
  private static int [] yy_unpack() {
    int [] trans = new int[2541];
    int offset = 0;
    offset = yy_unpack(yy_packed0, offset, trans);
    return trans;
  }

  /** 
   * Unpacks the compressed DFA transition table.
   *
   * @param packed   the packed transition table
   * @return         the index of the last entry
   */
  private static int yy_unpack(String packed, int offset, int [] trans) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do trans[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] yy_unpack_cmap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1262) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   IOException  if any I/O-Error occurs
   */
  private boolean yy_refill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (yy_startRead > 0) {
      System.arraycopy(yy_buffer, yy_startRead, 
                       yy_buffer, 0, 
                       yy_endRead-yy_startRead);

      /* translate stored positions */
      yy_endRead-= yy_startRead;
      yy_currentPos-= yy_startRead;
      yy_markedPos-= yy_startRead;
      yy_pushbackPos-= yy_startRead;
      yy_startRead = 0;
    }

    /* is the buffer big enough? */
    if (yy_currentPos >= yy_buffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[yy_currentPos*2];
      System.arraycopy(yy_buffer, 0, newBuffer, 0, yy_buffer.length);
      yy_buffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = yy_reader.read(yy_buffer, yy_endRead, 
                                            yy_buffer.length-yy_endRead);

    if (numRead < 0) {
      return true;
    }
    else {
      yy_endRead+= numRead;  
      return false;
    }
  }


  /**
   * Closes the input stream.
   */
  final public void yyclose() throws java.io.IOException {
    yy_atEOF = true;            /* indicate end of file */
    yy_endRead = yy_startRead;  /* invalidate buffer    */

    if (yy_reader != null)
      yy_reader.close();
  }


  /**
   * Closes the current stream, and resets the
   * scanner to read from a new input stream.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>YY_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  final public void yyreset(java.io.Reader reader) throws java.io.IOException {
    yyclose();
    yy_reader = reader;
    yy_atBOL  = true;
    yy_atEOF  = false;
    yy_endRead = yy_startRead = 0;
    yy_currentPos = yy_markedPos = yy_pushbackPos = 0;
    yyline = yychar = yycolumn = 0;
    yy_lexical_state = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  final public int yystate() {
    return yy_lexical_state;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  final public void yybegin(int newState) {
    yy_lexical_state = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  final public String yytext() {
    return new String( yy_buffer, yy_startRead, yy_markedPos-yy_startRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  final public char yycharat(int pos) {
    return yy_buffer[yy_startRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  final public int yylength() {
    return yy_markedPos-yy_startRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void yy_ScanError(int errorCode) {
    String message;
    try {
      message = YY_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = YY_ERROR_MSG[YY_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  private void yypushback(int number)  {
    if ( number > yylength() )
      yy_ScanError(YY_PUSHBACK_2BIG);

    yy_markedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   IOException  if any I/O-Error occurs
   */
  public String yylex() throws java.io.IOException {
    int yy_input;
    int yy_action;

    // cached fields:
    int yy_currentPos_l;
    int yy_startRead_l;
    int yy_markedPos_l;
    int yy_endRead_l = yy_endRead;
    char [] yy_buffer_l = yy_buffer;
    char [] yycmap_l = yycmap;

    int [] yytrans_l = yytrans;
    int [] yy_rowMap_l = yy_rowMap;
    byte [] yy_attr_l = YY_ATTRIBUTE;

    while (true) {
      yy_markedPos_l = yy_markedPos;

      if (yy_markedPos_l > yy_startRead) {
        switch (yy_buffer_l[yy_markedPos_l-1]) {
        case '\n':
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yy_atBOL = true;
          break;
        case '\r': 
          if (yy_markedPos_l < yy_endRead_l)
            yy_atBOL = yy_buffer_l[yy_markedPos_l] != '\n';
          else if (yy_atEOF)
            yy_atBOL = false;
          else {
            boolean eof = yy_refill();
            yy_markedPos_l = yy_markedPos;
            yy_buffer_l = yy_buffer;
            if (eof) 
              yy_atBOL = false;
            else 
              yy_atBOL = yy_buffer_l[yy_markedPos_l] != '\n';
          }
          break;
        default:
          yy_atBOL = false;
        }
      }
      yy_action = -1;

      yy_startRead_l = yy_currentPos_l = yy_currentPos = 
                       yy_startRead = yy_markedPos_l;

      if (yy_atBOL)
        yy_state = YY_LEXSTATE[yy_lexical_state+1];
      else
        yy_state = YY_LEXSTATE[yy_lexical_state];


      yy_forAction: {
        while (true) {

          if (yy_currentPos_l < yy_endRead_l)
            yy_input = yy_buffer_l[yy_currentPos_l++];
          else if (yy_atEOF) {
            yy_input = YYEOF;
            break yy_forAction;
          }
          else {
            // store back cached positions
            yy_currentPos  = yy_currentPos_l;
            yy_markedPos   = yy_markedPos_l;
            boolean eof = yy_refill();
            // get translated positions and possibly new buffer
            yy_currentPos_l  = yy_currentPos;
            yy_markedPos_l   = yy_markedPos;
            yy_buffer_l      = yy_buffer;
            yy_endRead_l     = yy_endRead;
            if (eof) {
              yy_input = YYEOF;
              break yy_forAction;
            }
            else {
              yy_input = yy_buffer_l[yy_currentPos_l++];
            }
          }
          int yy_next = yytrans_l[ yy_rowMap_l[yy_state] + yycmap_l[yy_input] ];
          if (yy_next == -1) break yy_forAction;
          yy_state = yy_next;

          int yy_attributes = yy_attr_l[yy_state];
          if ( (yy_attributes & 1) == 1 ) {
            yy_action = yy_state; 
            yy_markedPos_l = yy_currentPos_l; 
            if ( (yy_attributes & 8) == 8 ) break yy_forAction;
          }

        }
      }

      // store back cached position
      yy_markedPos = yy_markedPos_l;

      switch (yy_action) {

        case 88: 
          { 
  cat.debug( "@@@@{newline} entering PRE" );
  yybegin( PRE );
  return yytext();
 }
        case 107: break;
        case 30: 
          { 
  cat.debug( "Format off" );
  yybegin( OFF );
  return "__";
 }
        case 108: break;
        case 14: 
          { 
  return "&amp;";
 }
        case 109: break;
        case 13: 
          { 
  return "&gt;";
 }
        case 110: break;
        case 12: 
          { 
  return "&lt;";
 }
        case 111: break;
        case 6: 
        case 10: 
        case 11: 
        case 15: 
        case 16: 
        case 17: 
        case 18: 
        case 19: 
        case 20: 
        case 21: 
        case 22: 
        case 23: 
        case 24: 
        case 28: 
        case 29: 
          { 
  cat.debug( ". (" + yytext() + ")" );
  return yytext();
 }
        case 112: break;
        case 46: 
          { 
  return "<br>";
 }
        case 113: break;
        case 8: 
        case 9: 
          { 
  cat.debug( "{whitespace}" );
  return yytext();
 }
        case 114: break;
        case 72: 
          { 
  return yytext();
 }
        case 115: break;
        case 49: 
          { 
  cat.debug( "Format on" );
  yybegin( NORMAL );
  return "__";
 }
        case 116: break;
        case 96: 
          { 
  cat.debug("!!...!!");
  return "<h2>" + yytext().substring(2, yytext().substring(2).indexOf('!')+2) + "</h2>";
 }
        case 117: break;
        case 104: 
          { 
  cat.debug("!!!...!!!");
  return "<h1>" + yytext().substring(3, yytext().substring(3).indexOf('!')+3) + "</h1>";
 }
        case 118: break;
        case 62: 
          { 
  cat.debug( "'''" );
  if( strong ){
    strong = false;
    return( "</strong>" );
  }
  else{
    strong = true;
    return( "<strong>" );
  }
 }
        case 119: break;
        case 78: 
          { 
  cat.debug("!...!");
  return "<h3>" + yytext().substring(1,yytext().substring(1).indexOf('!')+1) + "</h3>";
 }
        case 120: break;
        case 63: 
          { 
  cat.debug( "===" );
  if( underline ){
    underline = false;
    return( "</u>" );
  }
  else{
    underline = true;
    return( "<u>" );
  }
 }
        case 121: break;
        case 7: 
          { 
  cat.debug( "{newline}" );
  if( h1 ){
    h1 = false;
    return( "</h1>" );
  }
  if( h2 ){
    h2 = false;
    return( "</h2>" );
  }
  if( h3 ){
    h3 = false;
    return( "</h3>" );
  }
  return yytext();
 }
        case 122: break;
        case 50: 
          { 
  cat.debug( "{newline}x2 leaving pre" );
	yybegin( NORMAL );
  return yytext();
 }
        case 123: break;
        case 60: 
          { 
  cat.debug( "color start" );
  
  StringBuffer sb = new StringBuffer() ;
  if( color ){
    sb.append( "</font>" );
  }  
  color = true;
  sb.append( "<font color=\"")
    .append( yytext().substring( 1,yytext().length()-1) )
    .append( "\">" );
  
  return sb.toString();
 }
        case 124: break;
        case 85: 
          { 
  cat.debug( "external" );
  yybegin( EXTERNAL );
  return yytext();
 }
        case 125: break;
        case 38: 
          { 
  if( color ){
    cat.debug( "color end" );
  
    color = false ;
    return( "</font>" );
  }
  else {
    return yytext();
  }
  
 }
        case 126: break;
        case 25: 
        case 26: 
        case 27: 
          { 
  cat.debug( "PRE {whitespace}" );
  return yytext();
 }
        case 127: break;
        case 100: 
          { 
  cat.debug( "external end");
  yybegin( NORMAL );
  return yytext();
 }
        case 128: break;
        case 61: 
          { 
  cat.debug( "escaped double backslash" );
  return "\\__";
 }
        case 129: break;
        case 41: 
          { 
  cat.debug( "''" );
  if( em ){
    em = false;
    return( "</em>" );
  }
  else{
    em = true;
    return( "<em>" );
  }
 }
        case 130: break;
        case 42: 
          { 
  cat.debug( "::" );
  if( center ){
    center = false;
    return( "</div>" );
  }
  else{
    center = true;
    return( "<div align=\"center\">" );
  }
 }
        case 131: break;
        case 86: 
          { 
  return "&amp;amp;";
 }
        case 132: break;
        case 74: 
          { 
  return "&amp;gt;";
 }
        case 133: break;
        case 73: 
          { 
  return "&amp;lt;";
 }
        case 134: break;
        case 65: 
          { 
  return "</code>";
 }
        case 135: break;
        case 64: 
          { 
  return "<code>";
 }
        case 136: break;
        case 31: 
          { 
  return "&nbsp;";
 }
        case 137: break;
        default: 
          if (yy_input == YYEOF && yy_startRead == yy_currentPos) {
            yy_atEOF = true;
              { 	if( strong ){
	  strong = false;
	  return( "</strong>" );
	}
	if( em ){
	  em = false;
	  return( "</em>" );
	}
	return null;
 }
          } 
          else {
            yy_ScanError(YY_NO_MATCH);
          }
      }
    }
  }


}
