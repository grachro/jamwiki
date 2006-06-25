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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jmwiki.servlets;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jmwiki.utils.JSPUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public abstract class JMController extends HttpServlet {

	private static final Logger logger = Logger.getLogger(JMController.class);
	public static final String PARAMETER_TITLE = "title";
	public static final String PARAMETER_TOPIC = "topic";
	public static final String PARAMETER_VIRTUAL_WIKI = "virtualWiki";

	/**
	 *
	 */
	protected void error(HttpServletRequest request, HttpServletResponse response, Exception err) {
		request.setAttribute("exception", err);
		request.setAttribute(JMController.PARAMETER_TITLE, "Error");
		logger.error(err.getMessage(), err);
		if (err instanceof WikiServletException) {
			request.setAttribute("javax.servlet.jsp.jspException", err);
		}
		dispatch("/WEB-INF/jsp/servlet-error.jsp", request, response);
	}

	/**
	 *
	 */
	protected void dispatch(String destination, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getting dispatcher for " + destination + ", current URL: " + request.getRequestURL());
		RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
		if (dispatcher == null) {
			logger.error("No dispatcher available for " + destination + " (request=" + request +
				", response=" + response + ")");
			return;
		}
		try {
			dispatcher.forward(request, response);
		} catch (Exception e) {
			logger.error("Dispatch error", e);
			try {
				dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/servlet-error.jsp");
				dispatcher.forward(request, response);
			} catch (Exception e1) {
				logger.error("Error within dispatch error", e1);
			}
		}
	}

	/**
	 *
	 */
	protected void include(String destination, HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
		if (dispatcher == null) {
			logger.info("No dispatcher available for " + destination + " (request=" + request +
				", response=" + response + ")");
			return;
		}
		try {
			dispatcher.include(request, response);
		} catch (Exception e) {
			log("Dispatch error: " + e.getMessage(), e);
			e.printStackTrace();
			logger.error(e);
			try {
				dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/servlet-error.jsp");
				dispatcher.forward(request, response);
			} catch (Exception e1) {
				logger.error("Error within dispatch error", e1);
			}
		}
	}

	/**
	 *
	 */
	protected void redirect(String destination, HttpServletResponse response) {
		String url = response.encodeRedirectURL(destination);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 *
	 */
	public static void buildLayout(HttpServletRequest request, ModelAndView next) throws Exception {
		String virtualWiki = JMController.getVirtualWikiFromURI(request);
		if (virtualWiki == null) {
			throw new Exception("Invalid virtual wiki");
		}
		String topic = JMController.getTopicFromRequest(request);
		if (topic == null) {
			topic = JMController.getTopicFromURI(request);
		}
		next.addObject(PARAMETER_TOPIC, topic);
		// build the layout contents
		String leftMenu = WikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			JMController.getMessage("specialpages.leftMenu", request.getLocale())
		);
		next.addObject("leftMenu", leftMenu);
		String topArea = WikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			JMController.getMessage("specialpages.topArea", request.getLocale())
		);
		next.addObject("topArea", topArea);
		String bottomArea = WikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			JMController.getMessage("specialpages.bottomArea", request.getLocale())
		);
		next.addObject("bottomArea", bottomArea);
		String styleSheet = WikiServlet.getCachedRawContent(
			request.getContextPath(),
			virtualWiki,
			JMController.getMessage("specialpages.stylesheet", request.getLocale())
		);
		next.addObject("StyleSheet", styleSheet);
		next.addObject(PARAMETER_VIRTUAL_WIKI, virtualWiki);
	}

	/**
	 * Get messages for the given locale
	 * @param locale locale
	 * @return
	 */
	public static String getMessage(String key, Locale locale) {
		ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources", locale);
		return messages.getString(key);
	}

	/**
	 *
	 */
	public static String getTopicFromURI(HttpServletRequest request) throws Exception {
		String uri = request.getRequestURI().trim();
		if (uri == null || uri.length() <= 0) {
			throw new Exception("URI string is empty");
		}
		int slashIndex = uri.lastIndexOf('/');
		if (slashIndex == -1) {
			throw new Exception("No topic in URL: " + uri);
		}
		String topic = uri.substring(slashIndex + 1);
		topic = JSPUtils.decodeURL(topic);
		logger.info("Retrieved topic from URI as: " + topic);
		return topic;
	}

	/**
	 *
	 */
	public static String getTopicFromRequest(HttpServletRequest request) throws Exception {
		String topic = request.getParameter(JMController.PARAMETER_TOPIC);
		if (topic == null) {
			topic = (String)request.getAttribute(JMController.PARAMETER_TOPIC);
		}
		if (topic == null) return null;
		topic = JSPUtils.decodeURL(topic);
		return topic;
	}

	/**
	 *
	 */
	public static String getVirtualWikiFromURI(HttpServletRequest request) {
		String uri = request.getRequestURI().trim();
		String contextPath = request.getContextPath().trim();
		String virtualWiki = null;
		if ((uri == null || uri.length() <= 0) || (contextPath == null || contextPath.length() <= 0)) {
			return null;
		}
		uri = uri.substring(contextPath.length() + 1);
		int slashIndex = uri.indexOf('/');
		if (slashIndex == -1) {
			return null;
		}
		virtualWiki = uri.substring(0, slashIndex);
		logger.info("Retrieved virtual wiki from URI as: " + virtualWiki);
		return virtualWiki;
	}

	/**
	 *
	 */
	protected static boolean isAction(HttpServletRequest request, String key, String constant) {
		String action = request.getParameter(WikiServlet.PARAMETER_ACTION);
		if (action == null || action.length() == 0) {
			return false;
		}
		if (key != null &&  action.equals(JMController.getMessage(key, request.getLocale()))) {
			return true;
		}
		if (constant != null && action.equals(constant)) {
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	protected static boolean isTopic(HttpServletRequest request, String value) {
		try {
			String topic = JMController.getTopicFromURI(request);
			if (topic == null || topic.length() == 0) {
				return false;
			}
			if (value != null &&  topic.equals(value)) {
				return true;
			}
		} catch (Exception e) {}
		return false;
	}
}
