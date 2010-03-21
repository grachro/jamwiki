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
package org.jamwiki.migrate;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.jamwiki.model.Namespace;

/**
 * Constants needed for Mediawiki import/export functionality.
 */
public final class MediaWikiConstants {

	public static final int MEDIAWIKI_MEDIA_NAMESPACE_ID = -2;
	public static final int MEDIAWIKI_SPECIAL_NAMESPACE_ID = -1;
	public static final int MEDIAWIKI_MAIN_NAMESPACE_ID = 0;
	public static final int MEDIAWIKI_TALK_NAMESPACE_ID = 1;
	public static final int MEDIAWIKI_USER_NAMESPACE_ID = 2;
	public static final int MEDIAWIKI_USER_TALK_NAMESPACE_ID = 3;
	public static final int MEDIAWIKI_SITE_CUSTOM_NAMESPACE_ID = 4;
	public static final int MEDIAWIKI_SITE_CUSTOM_TALK_NAMESPACE_ID = 5;
	public static final int MEDIAWIKI_FILE_NAMESPACE_ID = 6;
	public static final int MEDIAWIKI_FILE_TALK_NAMESPACE_ID = 7;
	public static final int MEDIAWIKI_MEDIAWIKI_NAMESPACE_ID = 8;
	public static final int MEDIAWIKI_MEDIAWIKI_TALK_NAMESPACE_ID = 9;
	public static final int MEDIAWIKI_TEMPLATE_NAMESPACE_ID = 10;
	public static final int MEDIAWIKI_TEMPLATE_TALK_NAMESPACE_ID = 11;
	public static final int MEDIAWIKI_HELP_NAMESPACE_ID = 12;
	public static final int MEDIAWIKI_HELP_TALK_NAMESPACE_ID = 13;
	public static final int MEDIAWIKI_CATEGORY_NAMESPACE_ID = 14;
	public static final int MEDIAWIKI_CATEGORY_TALK_NAMESPACE_ID = 15;
	static final String MEDIAWIKI_ELEMENT_NAMESPACE = "namespace";
	static final String MEDIAWIKI_ELEMENT_TOPIC = "page";
	static final String MEDIAWIKI_ELEMENT_TOPIC_CONTENT = "text";
	static final String MEDIAWIKI_ELEMENT_TOPIC_NAME = "title";
	static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION = "revision";
	static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION_COMMENT = "comment";
	static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION_EDIT_DATE = "timestamp";
	static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION_IP = "ip";
	static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION_USERNAME = "username";
	// the Mediawiki XML file uses ISO 8601 format for dates
	static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	static final Map<Integer, String> MEDIAWIKI_NAMESPACE_MAP = new TreeMap<Integer, String>();
	static {
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_MEDIA_NAMESPACE_ID, "Media");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_SPECIAL_NAMESPACE_ID, "Special");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_MAIN_NAMESPACE_ID, "");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_TALK_NAMESPACE_ID, "Talk");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_USER_NAMESPACE_ID, "User");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_USER_TALK_NAMESPACE_ID, "User talk");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_FILE_NAMESPACE_ID, "File");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_FILE_TALK_NAMESPACE_ID, "File talk");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_MEDIAWIKI_NAMESPACE_ID, "Mediawiki");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_MEDIAWIKI_TALK_NAMESPACE_ID, "Mediawiki talk");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_TEMPLATE_NAMESPACE_ID, "Template");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_TEMPLATE_TALK_NAMESPACE_ID, "Template talk");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_HELP_NAMESPACE_ID, "Help");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_HELP_TALK_NAMESPACE_ID, "Help talk");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_CATEGORY_NAMESPACE_ID, "Category");
		MEDIAWIKI_NAMESPACE_MAP.put(MEDIAWIKI_CATEGORY_TALK_NAMESPACE_ID, "Category talk");
	}

	/**
	 * Private constructor to prevent any instance of this class from ever being instantiated.
	 */
	private MediaWikiConstants() {
	}
}
