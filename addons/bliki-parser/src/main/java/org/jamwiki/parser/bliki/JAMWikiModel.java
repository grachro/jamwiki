package org.jamwiki.parser.bliki;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.filter.WikipediaParser;
import info.bliki.wiki.model.AbstractWikiModel;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.tags.WPATag;
import info.bliki.wiki.tags.util.TagStack;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.jflex.WikiHeadingTag;
import org.jamwiki.parser.jflex.WikiSignatureTag;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;

/**
 * An IWikiModel model implementation for JAMWiki
 * 
 */
public class JAMWikiModel extends AbstractWikiModel {
	// see: JFlexParser.MODE_MINIMAL
	protected static final int MODE_MINIMAL = 3;

	private static final WikiLogger logger = WikiLogger.getLogger(WikiHeadingTag.class.getName());

	protected String fContextPath;

	protected ParserInput fParserInput;

	protected ParserOutput fParserOutput;

	static {

		TagNode.addAllowedAttribute("style");
	}

	public JAMWikiModel(ParserInput parserInput, ParserOutput document, String contextPath) {
		super(Configuration.DEFAULT_CONFIGURATION);

		fParserInput = parserInput;
		fParserOutput = document;
		fContextPath = contextPath;
	}

	public void parseInternalImageLink(String imageNamespace, String name) {
		// see JAMHTMLConverter#imageNodeToText() for the real HTML conversion
		// routine!!!
		ImageFormat imageFormat = ImageFormat.getImageFormat(name, imageNamespace);

		int pxWidth = imageFormat.getWidth();
		String caption = imageFormat.getCaption();
		TagNode divTagNode = new TagNode("div");
		divTagNode.addAttribute("id", "image", false);
		// divTagNode.addAttribute("href", hrefImageLink, false);
		// divTagNode.addAttribute("src", srcImageLink, false);
		divTagNode.addObjectAttribute("wikiobject", imageFormat);
		if (pxWidth != -1) {
			divTagNode.addAttribute("style", "width:" + pxWidth + "px", false);
		}
		pushNode(divTagNode);

		if (caption != null && caption.length() > 0) {

			TagNode captionTagNode = new TagNode("div");
			String clazzValue = "caption";
			String type = imageFormat.getType();
			if (type != null) {
				clazzValue = type + clazzValue;
			}
			captionTagNode.addAttribute("class", clazzValue, false);

			TagStack localStack = WikipediaParser.parseRecursive(caption, this, true, true);
			captionTagNode.addChildren(localStack.getNodeList());
			String altAttribute = captionTagNode.getBodyString();
			imageFormat.setAlt(altAttribute);
			pushNode(captionTagNode);
			// WikipediaParser.parseRecursive(caption, this);
			popNode();
		}

		popNode(); // div

	}

	public void appendSignature(Appendable writer, int numberOfTildes) throws IOException {
		WikiSignatureTag parserTag;
		switch (numberOfTildes) {
		case 3:
			parserTag = new WikiSignatureTag();
			writer.append(parserTag.parse(fParserInput, fParserOutput, MODE_MINIMAL, "~~~"));
			break;
		case 4:
			parserTag = new WikiSignatureTag();
			writer.append(parserTag.parse(fParserInput, fParserOutput, MODE_MINIMAL, "~~~~"));
			break;
		case 5:
			parserTag = new WikiSignatureTag();
			writer.append(parserTag.parse(fParserInput, fParserOutput, MODE_MINIMAL, "~~~~~"));
			break;
		}
	}

	public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass, boolean parseRecursive) {
		try {
			WikiLink wikiLink;
			if (hashSection != null) {
				wikiLink = LinkUtil.parseWikiLink(topic + "#" + hashSection);
			} else {
				wikiLink = LinkUtil.parseWikiLink(topic);
			}
			String destination = wikiLink.getDestination();
			String virtualWiki = fParserInput.getVirtualWiki();
			String section = wikiLink.getSection();
			String query = wikiLink.getQuery();
			String href = buildTopicUrlNoEdit(fContextPath, virtualWiki, destination, section, query);
			String style = "";
			if (StringUtils.isBlank(topic) && !StringUtils.isBlank(section)) {
				// do not check existence for section links
			} else {
				if (!LinkUtil.isExistingArticle(virtualWiki, topic)) {
					style = "edit";
					href = LinkUtil.buildEditLinkUrl(fContextPath, virtualWiki, topic, query, -1);
				}
			}
			WPATag aTagNode = new WPATag();
			aTagNode.addAttribute("href", href, true);
			aTagNode.addAttribute("class", style, true);
			aTagNode.addObjectAttribute("wikilink", topic);

			pushNode(aTagNode);
			if (parseRecursive) {
				WikipediaParser.parseRecursive(topicDescription.trim(), this, false, true);
			} else {
				aTagNode.addChild(new ContentToken(topicDescription));
			}
			popNode();
		} catch (DataAccessException e1) {
			e1.printStackTrace();
			append(new ContentToken(topicDescription));
		}
	}

	/**
	 * Build a URL to the topic page for a given topic. This method does NOT
	 * verify if the topic exists or if it is a "Special:" page, simply returning
	 * the URL for the topic and virtual wiki.
	 * 
	 * @param context
	 *          The servlet context path. If this value is <code>null</code> then
	 *          the resulting URL will NOT include context path, which breaks HTML
	 *          links but is useful for servlet redirection URLs.
	 * @param virtualWiki
	 *          The virtual wiki for the link that is being created.
	 * @param topicName
	 *          The name of the topic for which a link is being built.
	 * @param section
	 *          The section of the page (#section) for which a link is being
	 *          built.
	 * @param queryString
	 *          Query string parameters to append to the link.
	 * @throws Exception
	 *           Thrown if any error occurs while builing the link URL.
	 */
	private static String buildTopicUrlNoEdit(String context, String virtualWiki, String topicName, String section, String queryString) {
		// TODO same as LinkUtil#buildTopicUrlNoEdit()
		if (StringUtils.isBlank(topicName) && !StringUtils.isBlank(section)) {
			return "#" + Utilities.encodeAndEscapeTopicName(section);
		}
		StringBuffer url = new StringBuffer();
		if (context != null) {
			url.append(context);
		}
		// context never ends with a "/" per servlet specification
		url.append('/');
		// get the virtual wiki, which should have been set by the parent servlet
		url.append(Utilities.encodeAndEscapeTopicName(virtualWiki));
		url.append('/');
		url.append(Utilities.encodeAndEscapeTopicName(topicName));
		if (!StringUtils.isBlank(queryString)) {
			if (queryString.charAt(0) != '?') {
				url.append('?');
			}
			url.append(queryString);
		}
		if (!StringUtils.isBlank(section)) {
			if (section.charAt(0) != '#') {
				url.append('#');
			}
			url.append(Utilities.encodeAndEscapeTopicName(section));
		}
		return url.toString();
	}

	public void addCategory(String categoryName, String sortKey) {
		fParserOutput.addCategory(getCategoryNamespace() + NamespaceHandler.NAMESPACE_SEPARATOR + categoryName, sortKey);
	}

	public void addLink(String topic) {
		fParserOutput.addLink(topic);
	}

	public void addTemplate(String template) {
		fParserOutput.addTemplate(template);
	}

	public String getRawWikiContent(String namespace, String topicName, Map map) {
		String result = super.getRawWikiContent(namespace, topicName, map);
		if (result != null) {
			return result;
		}
		try {
			topicName = topicName.replaceAll("_", " ");
			Topic topic = WikiBase.getDataHandler().lookupTopic(fParserInput.getVirtualWiki(), namespace + ':' + topicName, false, null);
			if (topic == null) {
				return null;
			}
			return topic.getTopicContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void buildEditLinkUrl(int section) {
		if (fParserInput.getAllowSectionEdit()) {
			TagNode divTagNode = new TagNode("div");
			divTagNode.addAttribute("style", "font-size:90%;float:right;margin-left:5px;", false);
			divTagNode.addChild(new ContentToken("["));
			append(divTagNode);

			String url = "";
			try {
				url = LinkUtil.buildEditLinkUrl(fParserInput.getContext(), fParserInput.getVirtualWiki(), fParserInput.getTopicName(),
						null, section);
			} catch (Exception e) {
				logger.severe("Failure while building link for topic " + fParserInput.getVirtualWiki() + " / "
						+ fParserInput.getTopicName(), e);
			}
			TagNode aTagNode = new TagNode("a");
			aTagNode.addAttribute("href", url, false);
			aTagNode.addChild(new ContentToken(Utilities.formatMessage("common.sectionedit", fParserInput.getLocale())));
			divTagNode.addChild(aTagNode);
			divTagNode.addChild(new ContentToken("]"));
		}
	}

	public boolean parseBBCodes() {
		return false;
	}

	public boolean replaceColon() {
		return false;
	}

	public String getCategoryNamespace() {
		return NamespaceHandler.NAMESPACE_CATEGORY;
	}

	public String getImageNamespace() {
		return NamespaceHandler.NAMESPACE_IMAGE;
	}

	public String getTemplateNamespace() {
		return NamespaceHandler.NAMESPACE_TEMPLATE;
	}

	public Set<String> getLinks() {
		return null;
	}

	public void appendInterWikiLink(String namespace, String title, String linkText) {
		// no interwiki link parsing
		return;
	}

	public boolean isTemplateTopic() {
		String topicName = fParserInput.getTopicName();
		int index = topicName.indexOf(':');
		if (index > 0) {
			String namespace = topicName.substring(0, index);
			if (isTemplateNamespace(namespace)) {
				return true;
			}
		}
		return false;
	}

	public boolean isMathtranRenderer() {
		return true;
	}

	public String parseTemplates(String rawWikiText, boolean parseOnlySignature) {
		if (rawWikiText == null) {
			return "";
		}
		if (!parseOnlySignature) {
			initialize();
		}
		StringBuilder buf = new StringBuilder(rawWikiText.length() + rawWikiText.length() / 10);
		try {
			TemplateParser.parse(rawWikiText, this, buf, parseOnlySignature, true);
		} catch (Exception ioe) {
			ioe.printStackTrace();
			buf.append("<span class=\"error\">TemplateParser exception: " + ioe.getClass().getSimpleName() + "</span>");
		}
		return buf.toString();
	}

}
