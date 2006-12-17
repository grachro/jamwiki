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
package org.jamwiki.servlets;

import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 */
public class JAMWikiLocaleInterceptor extends LocaleChangeInterceptor {

	private static final WikiLogger logger = WikiLogger.getLogger(JAMWikiServlet.class.getName());

	/**
	 *
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if (locale == null) {
			locale = this.setUserLocale(request, response);
		}
		if (locale != null) {
			LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
			if (resolver != null) {
				resolver.setLocale(request, response, locale);
			}
		}
		return super.preHandle(request, response, handler);
	}

	/**
	 *
	 */
	private Locale retrieveUserLocale(HttpServletRequest request) throws ServletException {
		try {
			WikiUser user = Utilities.currentUser(request);
			if (user != null) {
				return Utilities.buildLocale(user.getDefaultLocale());
			}
			return null;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 *
	 */
	private Locale setUserLocale(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Locale locale = this.retrieveUserLocale(request);
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		return locale;
	}
}