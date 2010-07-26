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
 *
 * Based on code generated by Agitar build: Agitator Version 1.0.2.000071 (Build date: Jan 12, 2007) [1.0.2.000071]
 */
package org.jamwiki.utils;

import org.jamwiki.JAMWikiUnitTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class LinkUtilTest extends JAMWikiUnitTest {

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam() throws Throwable {
		String result = LinkUtil.appendQueryParam("", " ", "testLinkUtilValue");
		assertSame("result", "", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam1() throws Throwable {
		String result = LinkUtil.appendQueryParam("testLinkUtilQuery", "testLinkUtilParam", "");
		assertEquals("result", "?testLinkUtilQuery&amp;testLinkUtilParam=", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam2() throws Throwable {
		String result = LinkUtil.appendQueryParam("", "testLinkUtilParam", "testLinkUtilValue");
		assertEquals("result", "?testLinkUtilParam=testLinkUtilValue", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam3() throws Throwable {
		String result = LinkUtil.appendQueryParam("testLinkUtilQuery", "", "testLinkUtilValue");
		assertEquals("result", "?testLinkUtilQuery", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam4() throws Throwable {
		String result = LinkUtil.appendQueryParam("?", "testLinkUtilParam", "testLinkUtilValue");
		assertEquals("result", "?&amp;testLinkUtilParam=testLinkUtilValue", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam5() throws Throwable {
		String result = LinkUtil.appendQueryParam("testLinkUtilQuery", "testLinkUtilParam", "testLinkUtilValue");
		assertEquals("result", "?testLinkUtilQuery&amp;testLinkUtilParam=testLinkUtilValue", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam6() throws Throwable {
		String result = LinkUtil.appendQueryParam("", "testLinkUtilParam", " ");
		assertEquals("result", "?testLinkUtilParam=", result);
	}

	/**
	 *
	 */
	@Test
	public void testAppendQueryParam7() throws Throwable {
		String result = LinkUtil.appendQueryParam(null, "", "testLinkUtilValue");
		assertNull("result", result);
	}

	/**
	 *
	 */
	@Test
	public void testbuildTopicUrl() throws Throwable {
		String result = LinkUtil.buildTopicUrl("testLinkUtilContext", "testLinkUtilVirtualWiki", "", true);
		assertNull("result", result);
	}

	/**
	 *
	 */
	@Test
	public void testParseWikiLink() throws Throwable {
		WikiLink result = LinkUtil.parseWikiLink("en", "testLinkUtilRaw");
		assertEquals("result.getArticle()", "testLinkUtilRaw", result.getArticle());
	}

	/**
	 *
	 */
	@Test
	public void testParseWikiLink1() throws Throwable {
		WikiLink result = LinkUtil.parseWikiLink(null, "");
		assertNull("result.getArticle()", result.getArticle());
	}

	/**
	 *
	 */
	@Test(expected=NullPointerException.class)
	public void testInterWikiThrowsNullPointerException() throws Throwable {
		LinkUtil.interwiki(null);
	}
}

