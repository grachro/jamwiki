package org.vqwiki.parser;import java.io.IOException;import java.io.Reader;import java.io.StringReader;import java.util.List;import org.apache.log4j.Logger;import org.vqwiki.Environment;import org.vqwiki.parser.*;/** * Parser used to implement MediaWiki syntax. */public class MediaWikiParser extends AbstractParser {	private static final Logger logger = Logger.getLogger(MediaWikiParser.class);	private static final String PARSER_BUNDLE = "mediaWikiParser";	private static final String PARSER_VERSION = "1.0";	private static final String PARSER_NAME = "MediaWiki Parser";	private TableOfContents toc = new TableOfContents();	/**	 *	 */	public MediaWikiParser() {		super(new ParserInfo(PARSER_NAME, PARSER_VERSION, PARSER_BUNDLE));	}	/**	 * Utility method for executing a lexer parse.	 * FIXME - this is copy & pasted here and in VQWikiParser	 */	 protected StringBuffer lex(Lexer lexer) throws IOException {		StringBuffer contents = new StringBuffer();		while (true) {			String line = lexer.yylex();			if (line == null) {				break;			}			contents.append(line);		}		return contents;	}	/**	 * Parse text for online display.	 */	public String parseHTML(String rawtext, String virtualwiki) throws IOException {		StringBuffer contents = new StringBuffer();		Reader raw = new StringReader(rawtext.toString());		contents = this.parseSyntax(raw, virtualwiki);		raw = new StringReader(contents.toString());		contents = this.parseParagraphs(raw, virtualwiki);		contents = TableOfContents.addTableOfContents(this.toc, contents);		return contents.toString();	}	/**	 * Parse raw text and return something suitable for export	 */	public String parseExportHTML(String rawtext, String virtualWiki) throws IOException {		// this won't work yet - need to allow parsing differently if		// external specified to syntax lexer		StringBuffer contents = new StringBuffer();		Reader raw = new StringReader(rawtext.toString());		contents = this.parseSyntax(raw, virtualWiki);		raw = new StringReader(contents.toString());		contents = this.parseParagraphs(raw, virtualWiki);		contents = TableOfContents.addTableOfContents(this.toc, contents);		return contents.toString();	}	/**	 *	 */	private StringBuffer parseSyntax(Reader raw, String virtualWiki) throws IOException {		MediaWikiSyntax lexer = new MediaWikiSyntax(raw);		lexer.setVirtualWiki(virtualWiki);		lexer.setTOC(this.toc);		return this.lex(lexer);	}	/**	 *	 */	private StringBuffer parseParagraphs(Reader raw, String virtualWiki) throws IOException {		MediaWikiHTML lexer = new MediaWikiHTML(raw);		lexer.setVirtualWiki(virtualWiki);		return this.lex(lexer);	}}