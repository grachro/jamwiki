/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.parser.jflex;

import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.utils.WikiLogger;

/**
 * Abstract class that is extended by the JFlex lexers.  This class primarily
 * contains utility methods useful during parsing.
 */
public abstract class JFlexLexer {

	private static final WikiLogger logger = WikiLogger.getLogger(JFlexLexer.class.getName());

	/** Member variable used to keep track of the state history for the lexer. */
	protected Stack states = new Stack();
	/** Parser configuration information. */
	protected ParserInput parserInput = null;
	/** Parser parsing results. */
	protected ParserOutput parserOutput = null;
	/** Parser mode, which provides input to the parser about what steps to take. */
	protected int mode = JFlexParser.MODE_LAYOUT;
	/** Stack of currently parsed tag content. */
	private Stack tagStack = new Stack();

	/**
	 * Utility method used to indicate whether HTML tags are allowed in wiki syntax
	 * or not.
	 */
	protected boolean allowHTML() {
		return Environment.getBooleanValue(Environment.PROP_PARSER_ALLOW_HTML);
	}

	/**
	 * Utility method used to indicate whether Javascript is allowed in wiki syntax
	 * or not.
	 */
	protected boolean allowJavascript() {
		return Environment.getBooleanValue(Environment.PROP_PARSER_ALLOW_JAVASCRIPT);
	}

	/**
	 * Utility method used to indicate whether templates are allowed in wiki syntax
	 * or not.
	 */
	protected boolean allowTemplates() {
		return Environment.getBooleanValue(Environment.PROP_PARSER_ALLOW_TEMPLATES);
	}

	/**
	 * Append content to the current tag in the tag stack.
	 */
	protected void append(String content) {
		JFlexTagItem currentTag = (JFlexTagItem)this.tagStack.peek();
		currentTag.getTagContent().append(content);
	}

	/**
	 * Begin a new parser state and store the old state onto the stack.
	 *
	 * @param state The new parsing state that is being entered.
	 */
	protected void beginState(int state) {
		// store current state
		Integer current = new Integer(yystate());
		states.push(current);
		// switch to new state
		yybegin(state);
	}

	/**
	 * End processing of a parser state and switch to the previous parser state.
	 */
	protected void endState() {
		// revert to previous state
		if (states.empty()) {
			logger.warning("Attempt to call endState for an empty stack with text: " + yytext());
			return;
		}
		int next = ((Integer)states.pop()).intValue();
		yybegin(next);
	}

	/**
	 * This method is used to set the ParserOutput field, which is used to retrieve
	 * parsed information from the parser.
	 *
	 * @return Parsed information generated by the parser
	 */
	public ParserOutput getParserOutput() {
		return this.parserOutput;
	}

	/**
	 * Initialize the parser settings.  This functionality should be done
	 * from the constructor, but since JFlex generates code it is not possible
	 * to modify the constructor parameters.
	 *
	 * @param parserInput The ParserInput object containing parser parameters
	 *  required for successful parsing.
	 * @param parserOutput The current parsed document.  When parsing is done
	 *  in multiple stages that output values are also built in stages.
	 * @param mode The parser mode to use when parsing.  Mode affects what
	 *  type of parsing actions are taken when processing raw text.
	 */
	public final void init(ParserInput parserInput, ParserOutput parserOutput, int mode) {
		this.parserInput = parserInput;
		this.parserOutput = parserOutput;
		this.mode = mode;
		this.tagStack.push(new JFlexTagItem(JFlexTagItem.ROOT_TAG));
	}

	/**
	 * Peek at the current tag from the lexer stack and see if it matches
	 * the given tag type.
	 */
	protected JFlexTagItem peekTag() {
		return (JFlexTagItem)this.tagStack.peek();
	}

	/**
	 * Pop the most recent HTML tag from the lexer stack.
	 */
	protected JFlexTagItem popTag(String tagType) {
		if (this.tagStack.size() <= 1) {
			throw new IllegalStateException("popTag called on an empty tag stack or on the root stack element");
		}
		// verify that the tag being closed is the tag that is currently open.  if not
		// there are two options - first is that the user entered unbalanced HTML such
		// as "<u><strong>text</u></strong>" and it should be re-balanced, and second
		// is that this is just a random close tag such as "<div>text</div></div>" and
		// it should be escaped without modifying the tag stack.
		if (!this.peekTag().getTagType().equals(tagType)) {
			// check to see if a close tag override was previously set, which happens
			// from the inner tag of unbalanced HTML.  Example: "<u><strong>text</u></strong>"
			// would set a close tag override when the "</u>" is parsed to indicate that
			// the "</strong>" should actually be parsed as a "</u>".
			if (StringUtils.equals(this.peekTag().getTagType(), this.peekTag().getCloseTagOverride())) {
				return this.popTag(this.peekTag().getCloseTagOverride());
			}
			// check to see if the parent tag matches the current close tag.  if so then
			// this is unbalanced HTML of the form "<u><strong>text</u></strong>" and
			// it should be parsed as "<u><strong>text</strong></u>".
			JFlexTagItem parent = null;
			if (this.tagStack.size() > 2) {
				parent = (JFlexTagItem)this.tagStack.get(this.tagStack.size() - 2);
			}
			if (parent != null && parent.getTagType().equals(tagType)) {
				parent.setCloseTagOverride(tagType);
				return this.popTag(this.peekTag().getTagType());
			}
			// if the above checks fail then this is an attempt to pop a tag that is not
			// currently open, so append the escaped close tag to the current tag
			// content without modifying the tag stack.
			JFlexTagItem currentTag = (JFlexTagItem)this.tagStack.peek();
			currentTag.getTagContent().append("&lt;/" + tagType + "&gt;");
			return null;
		}
		JFlexTagItem currentTag = (JFlexTagItem)this.tagStack.pop();
		JFlexTagItem previousTag = (JFlexTagItem)this.tagStack.peek();
		previousTag.getTagContent().append(currentTag.toHtml());
		return currentTag;
	}

	/**
	 * Pop all tags off of the stack and return a string representation.
	 */
	protected String popAllTags() {
		// pop the stack down to (but not including) the root tag
		while (this.tagStack.size() > 1) {
			JFlexTagItem currentTag = (JFlexTagItem)this.tagStack.peek();
			this.popTag(currentTag.getTagType());
		}
		// now pop the root tag
		JFlexTagItem currentTag = (JFlexTagItem)this.tagStack.pop();
		return (this.mode > JFlexParser.MODE_MINIMAL) ? currentTag.toHtml().trim() : currentTag.toHtml();
	}

	/**
	 * Push a new HTML tag onto the lexer stack.
	 */
	protected void pushTag(String tagType, String tagAttributes) {
		JFlexTagItem tag = new JFlexTagItem(tagType);
		tag.setTagAttributes(tagAttributes);
		this.tagStack.push(tag);
	}

	/**
	 * Utility method used when parsing list tags to determine the current
	 * list nesting level.
	 */
	protected int currentListDepth() {
		int depth = 0;
		int currentPos = this.tagStack.size() - 1;
		while (currentPos >= 0) {
			JFlexTagItem tag = (JFlexTagItem)this.tagStack.get(currentPos);
			if (!StringUtils.equals(tag.getTagType(), "li") && !StringUtils.equals(tag.getTagType(), "dd") && !StringUtils.equals(tag.getTagType(), "dt")) {
				break;
			}
			// move back in the stack two since each list item has a parent list type
			currentPos -= 2;
			depth++;
		}
		return depth;
	}

	/**
	 *
	 */
	protected String calculateListItemType(char wikiSyntax) {
		if (wikiSyntax == '*' || wikiSyntax == '#') {
			return "li";
		}
		if (wikiSyntax == ';') {
			return "dt";
		}
		if (wikiSyntax == ':') {
			return "dd";
		}
		throw new IllegalArgumentException("Unrecognized wiki syntax: " + wikiSyntax);
	}

	/**
	 *
	 */
	protected String calculateListType(char wikiSyntax) {
		if (wikiSyntax == ';' || wikiSyntax == ':') {
			return "dl";
		}
		if (wikiSyntax == '#') {
			return "ol";
		}
		if (wikiSyntax == '*') {
			return "ul";
		}
		throw new IllegalArgumentException("Unrecognized wiki syntax: " + wikiSyntax);
	}

	/**
	 *
	 */
	protected void processListStack(String wikiSyntax) {
		int previousDepth = this.currentListDepth();
		int currentDepth = wikiSyntax.length();
		String tagType;
		// if list was previously open to a greater depth, close the old list down to the
		// current depth.
		int tagsToPop = (previousDepth - currentDepth);
		if (tagsToPop > 0) {
			this.popListTags(tagsToPop);
			previousDepth -= tagsToPop;
		}
		// now look for differences in the current list stacks.  for example, if
		// the previous list was "::;" and the current list is "###" then there are
		// some lists that must be closed.
		for (int i=0; i < previousDepth; i++) {
			// get the tagType for the root list ("ul", "dl", etc, NOT "li")
			int tagPos = this.tagStack.size() - ((previousDepth - i) * 2);
			tagType = ((JFlexTagItem)this.tagStack.get(tagPos)).getTagType();
			if (tagType.equals(this.calculateListType(wikiSyntax.charAt(i)))) {
				continue;
			}
			// if the above test did not match, then the stack needs to be popped
			// to this point.
			tagsToPop = (previousDepth - i);
			this.popListTags(tagsToPop);
			previousDepth -= tagsToPop;
			break;
		}
		if (previousDepth == 0) {
			// if no list is open, open one
			this.pushTag(this.calculateListType(wikiSyntax.charAt(0)), null);
			// add the new list item to the stack
			this.pushTag(this.calculateListItemType(wikiSyntax.charAt(0)), null);
		} else if (previousDepth == currentDepth) {
			// pop the previous list item
			tagType = ((JFlexTagItem)this.tagStack.peek()).getTagType();
			popTag(tagType);
			// add the new list item to the stack
			this.pushTag(this.calculateListItemType(wikiSyntax.charAt(previousDepth - 1)), null);
		}
		// if the new list has additional elements, push them onto the stack
		int counterStart = (previousDepth > 1) ? previousDepth : 1;
		for (int i=counterStart; i < wikiSyntax.length(); i++) {
			String previousTagType = ((JFlexTagItem)this.tagStack.peek()).getTagType();
			// handle a weird corner case.  if a "dt" is open and there are
			// sub-lists, close the dt and open a "dd" for the sub-list
			if (previousTagType.equals("dt")) {
				this.popTag("dt");
				if (!this.calculateListType(wikiSyntax.charAt(i)).equals("dl")) {
					this.popTag("dl");
					this.pushTag("dl", null);
				}
				this.pushTag("dd", null);
			}
			this.pushTag(this.calculateListType(wikiSyntax.charAt(i)), null);
			this.pushTag(this.calculateListItemType(wikiSyntax.charAt(i)), null);
		}
	}

	/**
	 *
	 */
	protected void popListTags(int depth) {
		if (depth < 0) {
			throw new IllegalArgumentException("Cannot pop a negative number: " + depth);
		}
		String tagType;
		for (int i=0; i < depth; i++) {
			// pop twice since lists have a list tag and a list item tag ("<ul><li></li></ul>")
			tagType = ((JFlexTagItem)this.tagStack.peek()).getTagType();
			popTag(tagType);
			tagType = ((JFlexTagItem)this.tagStack.peek()).getTagType();
			popTag(tagType);
		}
	}

	/**
	 * Take Wiki text of the form "|" or "| style='foo' |" and convert to
	 * and HTML <td> or <th> tag.
	 *
	 * @param text The text to be parsed.
	 * @param tag The HTML tag text, either "td" or "th".
	 * @param markup The Wiki markup for the tag, either "|", "|+" or "!"
	 */
	protected void parseTableCell(String text, String tagType, String markup) {
		if (text == null) {
			throw new IllegalArgumentException("No text specified while parsing table cell");
		}
		text = text.trim();
		String tagAttributes = null;
		int pos = StringUtils.indexOfAnyBut(text, markup);
		if (pos != -1) {
			text = text.substring(pos);
			pos = text.indexOf("|");
			if (pos != -1) {
				text = text.substring(0, pos);
			}
			tagAttributes = JFlexParserUtil.validateHtmlTagAttributes(text.trim());
		}
		this.pushTag(tagType, tagAttributes);
	}

	/**
	 * Make sure any open table tags that need to be closed are closed.
	 */
	protected void processTableStack() {
		String previousTagType = this.peekTag().getTagType();
		if (!previousTagType.equals("caption") && !previousTagType.equals("th") && !previousTagType.equals("td")) {
			// no table cell was open, so nothing to close
			return;
		}
		// pop the previous tag
		this.popTag(previousTagType);
	}

	/**
	 * Handle parsing of bold, italic, and bolditalic tags.
	 *
	 * @param tagType The tag type being parsed - either "i", "b", or <code>null</code>
	 *  if a bolditalic tag is being parsed.
	 */
	protected void processBoldItalic(String tagType) {
		if (tagType == null) {
			// bold-italic
			if (this.peekTag().getTagType().equals("i")) {
				// italic tag already opened
				this.processBoldItalic("i");
				this.processBoldItalic("b");
			} else {
				// standard bold-italic processing
				this.processBoldItalic("b");
				this.processBoldItalic("i");
			}
			return;
		}
		// bold or italic
		if (this.peekTag().getTagType().equals(tagType)) {
			// tag was open, close it
			this.popTag(tagType);
			return;
		}
		// TODO - make this more generic and implement it globally
		if (tagType.equals("b") && this.peekTag().getTagType().equals("i")) {
			// since Mediawiki syntax unfortunately chose to use the same character
			// for bold and italic ('' and '''), see if the syntax is of the form
			// '''''bold''' then italic'', in which case the current stack contains
			// "b" followed by "i" when it should be the reverse.
			int stackLength = this.tagStack.size();
			if (stackLength > 2) {
				JFlexTagItem grandparent = (JFlexTagItem)this.tagStack.get(stackLength - 2);
				if (grandparent.getTagType().equals("b")) {
					// swap the tag types and close the current tag
					grandparent.changeTagType("i");
					this.peekTag().changeTagType("b");
					this.popTag(tagType);
					return;
				}
			}
		}
		// push the new tag onto the stack
		this.pushTag(tagType, null);
	}

	/**
	 * JFlex internal method used to change the lexer state values.
	 */
	public abstract void yybegin(int newState);

	/**
	 * JFlex internal method used to parse the next token.
	 */
	public abstract String yylex() throws Exception;

	/**
	 * JFlex internal method used to retrieve the current lexer state value.
	 */
	public abstract int yystate();

	/**
	 * JFlex internal method used to retrieve the current text matched by the
	 * yylex() method.
	 */
	public abstract String yytext();
}
