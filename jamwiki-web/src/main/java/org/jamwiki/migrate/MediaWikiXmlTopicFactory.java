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

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The purpose of this class is to import a MediaWiki XML file into JAMWiki.
 */
public class MediaWikiXmlTopicFactory extends DefaultHandler implements Migrator {

	private static final WikiLogger logger = WikiLogger.getLogger(MediaWikiXmlTopicFactory.class.getName());
	private static final int MEDIAWIKI_MAIN_NAMESPACE_ID = 0;
	private static final int MEDIAWIKI_FILE_NAMESPACE_ID = 6;
	private static final int MEDIAWIKI_TEMPLATE_NAMESPACE_ID = 10;
	private static final int MEDIAWIKI_CATEGORY_NAMESPACE_ID = 14;
	private static final String MEDIAWIKI_ELEMENT_NAMESPACE = "namespace";
	private static final String MEDIAWIKI_ELEMENT_TOPIC = "page";
	private static final String MEDIAWIKI_ELEMENT_TOPIC_CONTENT = "text";
	private static final String MEDIAWIKI_ELEMENT_TOPIC_NAME = "title";
	private static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION = "revision";
	private static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION_COMMENT = "comment";
	private static final String MEDIAWIKI_ELEMENT_TOPIC_VERSION_EDIT_DATE = "timestamp";
	// the Mediawiki XML file uses ISO 8601 format for dates
	private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/** This map holds the current tag's attribute names and values.  It is cleared after an end-element is called and thus fails for nested elements. */
	private Map<String, String> currentAttributeMap = new HashMap<String, String>();
	/** This buffer holds the content of the current element during parsing.  It will be flushed after an end-element tag is reached. */
	private StringBuffer currentElementBuffer = new StringBuffer();
	private Topic currentTopic = new Topic();
	private TopicVersion currentTopicVersion = new TopicVersion();
	private Map<Date, TopicVersion> currentTopicVersions = new TreeMap<Date, TopicVersion>();
	private final WikiUser user;
	private final String authorDisplay;
	private final Map<Integer, String> namespaces = new HashMap<Integer, String>();
	private Map<Topic, List<TopicVersion>> parsedTopics = new HashMap<Topic, List<TopicVersion>>();
	private int previousTopicContentLength = 0;

	/**
	 *
	 */
	public MediaWikiXmlTopicFactory(WikiUser user, String authorDisplay) {
		super();
		this.authorDisplay = authorDisplay;
		this.user = user;
	}

	/**
	 *
	 */
	public Map<Topic, List<TopicVersion>> importFromFile(File file) throws MigrationException {
		this.importWikiXml(file);
		return this.parsedTopics;
	}

	/**
	 *
	 */
	public void exportToFile(File file, Map<Topic, List<TopicVersion>> data) throws MigrationException {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	private void importWikiXml(File file) throws MigrationException {
		// For big file parsing
		System.setProperty("entityExpansionLimit", "1000000");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, this);
		} catch (ParserConfigurationException e) {
			throw new MigrationException(e);
		} catch (IOException e) {
			throw new MigrationException(e);
		} catch (SAXException e) {
			throw new MigrationException(e);
		}
	}

	/**
	 * Convert MediaWiki namespace to JAMWiki namespace
	 *
	 * @param mediaWikiNamespaceId
	 * @return
	 */
	private String findJamwikiNamespace(String mediaWikiNamespace) {
		String ret = "";
		if (StringUtils.equals(namespaces.get(MEDIAWIKI_FILE_NAMESPACE_ID), mediaWikiNamespace)) {
			ret = NamespaceHandler.NAMESPACE_IMAGE;
		} else if (StringUtils.equals(namespaces.get(MEDIAWIKI_CATEGORY_NAMESPACE_ID), mediaWikiNamespace)) {
			ret = NamespaceHandler.NAMESPACE_CATEGORY;
		} else if (StringUtils.equals(namespaces.get(MEDIAWIKI_TEMPLATE_NAMESPACE_ID), mediaWikiNamespace)) {
			ret = NamespaceHandler.NAMESPACE_TEMPLATE;
		}
		return ret;
	}

	/**
	 * Convert the Wikipedia article namespace (if any) to a JAMWiki article namespace.
	 */
	private String convertArticleNameFromWikipediaToJAMWiki(String fullName) {
		String ret = fullName;
		int pos = fullName.indexOf(':');
		if (pos > 0) {
			String namespace = fullName.substring(0, pos);
			String title = fullName.substring(pos+1);
			String jamwikiNamespace = findJamwikiNamespace(namespace);
			if (jamwikiNamespace.length() > 0) {
				// matching JAMWiki namespace found
				ret = jamwikiNamespace + ":" + title;
			}
		}
		return ret;
	}

	/**
	 * Convert all namespaces names from MediaWiki to JAMWiki local representation.
	 */
	private String convertNamespaces(String text) {
		String ret = text;
		// convert all namespaces names from MediaWiki to JAMWiki local representation
		ret = Pattern.compile("\\[\\[" + namespaces.get(MEDIAWIKI_CATEGORY_NAMESPACE_ID) + "\\:", Pattern.CASE_INSENSITIVE).matcher(ret).replaceAll("[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");
		ret = Pattern.compile("\\[\\[" + namespaces.get(MEDIAWIKI_FILE_NAMESPACE_ID) + "\\:", Pattern.CASE_INSENSITIVE).matcher(ret).replaceAll("[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");
		return ret;
	}

	/**
	 *
	 */
	private Timestamp parseEditTimestamp(String timestamp) {
		try {
			Date date = DateUtils.parseDate(timestamp, new String[]{ISO_8601_DATE_FORMAT});
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			// FIXME - this should be handled somehow
			return new Timestamp(System.currentTimeMillis());
		}
	}

	//===========================================================
	// SAX DocumentHandler methods
	//===========================================================

	/**
	 * start of xml-tag
	 *
	 * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
	 *  if Namespace processing is not being performed.
	 * @param localName The local name (without prefix), or the empty string if Namespace processing
	 *  is not being performed.
	 * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
	 * @param attributes The attributes attached to the element. If there are no attributes, it shall be an
	 *  empty Attributes object.
	 */
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		this.currentElementBuffer = new StringBuffer();
		this.currentAttributeMap = new HashMap<String, String>();
		String key;
		if (attrs != null) {
			// populate the attribute map
			for (int i = 0; i < attrs.getLength(); i++) {
				key = attrs.getQName(i);
				if (!StringUtils.isBlank(key)) {
					this.currentAttributeMap.put(key, attrs.getValue(i));
				}
			}
		}
		if (MEDIAWIKI_ELEMENT_TOPIC_VERSION.equals(qName)) {
			this.currentTopicVersion = new TopicVersion();
			this.currentTopicVersion.setAuthorDisplay(this.authorDisplay);
			if (this.user != null) {
				this.currentTopicVersion.setAuthorId(this.user.getUserId());
			}
		} else if (MEDIAWIKI_ELEMENT_TOPIC.equals(qName)) {
			this.currentTopic = new Topic();
			this.currentTopicVersions = new TreeMap<Date, TopicVersion>();
		}
	}

	/**
	 * end of xml-tag
	 *
	 * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
	 *  if Namespace processing is not being performed.
	 * @param localName The local name (without prefix), or the empty string if Namespace processing
	 *  is not being performed.
	 * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (StringUtils.equals(MEDIAWIKI_ELEMENT_NAMESPACE, qName)) {
			namespaces.put(NumberUtils.toInt(this.currentAttributeMap.get("key")), currentElementBuffer.toString().trim());
		} else if (MEDIAWIKI_ELEMENT_TOPIC_NAME.equals(qName)) {
			String topicName = currentElementBuffer.toString().trim();
			topicName = convertArticleNameFromWikipediaToJAMWiki(topicName);
			WikiLink wikiLink = LinkUtil.parseWikiLink(topicName);
			currentTopic.setTopicType(WikiUtil.findTopicTypeForNamespace(wikiLink.getNamespace()));
			currentTopic.setName(topicName);
		} else if (MEDIAWIKI_ELEMENT_TOPIC_CONTENT.equals(qName)) {
			String topicContent = currentElementBuffer.toString().trim();
			topicContent = convertNamespaces(topicContent);
			currentTopic.setTopicContent(topicContent);
			currentTopicVersion.setVersionContent(topicContent);
			currentTopicVersion.setCharactersChanged(StringUtils.length(topicContent) - previousTopicContentLength);
			previousTopicContentLength = StringUtils.length(topicContent);
		} else if (MEDIAWIKI_ELEMENT_TOPIC_VERSION_COMMENT.equals(qName)) {
			this.currentTopicVersion.setEditComment(currentElementBuffer.toString().trim());
		} else if (MEDIAWIKI_ELEMENT_TOPIC_VERSION_EDIT_DATE.equals(qName)) {
			this.currentTopicVersion.setEditDate(this.parseEditTimestamp(currentElementBuffer.toString().trim()));
		} else if (MEDIAWIKI_ELEMENT_TOPIC_VERSION.equals(qName)) {
			this.currentTopicVersions.put(currentTopicVersion.getEditDate(), currentTopicVersion);
		} else if (MEDIAWIKI_ELEMENT_TOPIC.equals(qName)) {
			List<TopicVersion> currentTopicVersionList = new ArrayList<TopicVersion>();
			// topic versions are stored in a tree map to allow sorting... convert to a list
			for (TopicVersion topicVersion : this.currentTopicVersions.values()) {
				currentTopicVersionList.add(topicVersion);
			}
			this.parsedTopics.put(currentTopic, currentTopicVersionList);
		}
	}

	/**
	 * When the parser encounters plain text (not XML elements), it calls this method
	 * which accumulates them in a string buffer
	 */
	public void characters(char buf[], int offset, int len) throws SAXException {
		currentElementBuffer.append(buf, offset, len);
	}
}