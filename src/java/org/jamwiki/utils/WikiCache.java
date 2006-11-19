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
package org.jamwiki.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 *
 */
public class WikiCache {

	private static WikiLogger logger = WikiLogger.getLogger(WikiCache.class.getName());
	private static WikiCacheMap[] caches = new WikiCacheMap[3];
	private static CacheManager cacheManager = CacheManager.create();
	private static Cache parserCache = null;

	// FIXME - make configurable
	/** Maximum number of objects that can be stored in each cache instance. */
	public static final int CACHE_SIZE = 5000;
	/** Time to live (in seconds) for cached objects. */
	public static final int CACHE_MAX_AGE = 300;
	/** Time to live (in seconds) since the last accessed time for cached objects. */
	public static final int CACHE_IDLE_AGE = 300;
	private static final String CACHE_PARSER = "cacheParser";
	public static final int CACHE_TYPE_TOPIC_CONTENT = 0;
	public static final int CACHE_TYPE_TOPIC_NAME = 1;
	public static final int CACHE_TYPE_USER_LOGIN = 2;
	public static final int CACHE_TYPE_PARSER = 3;

	static {
		WikiCache.reset();
	}

	/**
	 *
	 */
	public static void addToCache(int cacheType, Object key, Object value) {
		if (cacheType == CACHE_TYPE_PARSER) {
			WikiCache.parserCache.put(new Element(key, value));
		} else {
			WikiCacheMap cache = WikiCache.caches[cacheType];
			cache.put(key, value);
		}
	}

	/**
	 *
	 */
	public static void addToCache(int cacheType, String virtualWiki, String topicName, String value) {
		String key = key(virtualWiki, topicName);
		WikiCache.addToCache(cacheType, key, value);
	}

	/**
	 *
	 */
	public static boolean isCached(int cacheType, Object key) {
		if (cacheType == CACHE_TYPE_PARSER) {
			return WikiCache.parserCache.isKeyInCache(key);
		} else {
			WikiCacheMap cache = WikiCache.caches[cacheType];
			return cache.containsKey(key);
		}
	}

	/**
	 *
	 */
	public static boolean isCached(int cacheType, String virtualWiki, String topicName) {
		String key = key(virtualWiki, topicName);
		return WikiCache.isCached(cacheType, key);
	}

	/**
	 *
	 */
	private static String key(String virtualWiki, String topicName) {
		return virtualWiki + "-" + topicName;
	}

	/**
	 *
	 */
	public static void removeFromCache(int cacheType, Object key) {
		if (cacheType == CACHE_TYPE_PARSER) {
			WikiCache.parserCache.remove(key);
		} else {
			WikiCacheMap cache = WikiCache.caches[cacheType];
			cache.remove(key);
		}
	}

	/**
	 *
	 */
	public static void removeFromCache(int cacheType, String virtualWiki, String topicName) {
		String key = key(virtualWiki, topicName);
		WikiCache.removeFromCache(cacheType, key);
	}

	/**
	 *
	 */
	public static void reset() {
		// FIXME - make the cache sizes configurable in some way
		WikiCache.caches[CACHE_TYPE_TOPIC_CONTENT] = new WikiCacheMap(100);
		WikiCache.caches[CACHE_TYPE_TOPIC_NAME] = new WikiCacheMap(2000);
		WikiCache.caches[CACHE_TYPE_USER_LOGIN] = new WikiCacheMap(2000);
		// FIXME - work in progress
		if (WikiCache.cacheManager.cacheExists(CACHE_PARSER)) {
			WikiCache.cacheManager.removeCache(CACHE_PARSER);
		}
		WikiCache.parserCache = new Cache(CACHE_PARSER, CACHE_SIZE, true, false, CACHE_MAX_AGE, CACHE_IDLE_AGE);
		WikiCache.cacheManager.addCache(WikiCache.parserCache);
	}

	/**
	 *
	 */
	public static Object retrieveFromCache(int cacheType, Object key) {
		if (cacheType == CACHE_TYPE_PARSER) {
			Element element = WikiCache.parserCache.get(key);
			return (element == null) ? null : element.getObjectValue();
		} else {
			WikiCacheMap cache = WikiCache.caches[cacheType];
			return cache.get(key);
		}
	}

	/**
	 *
	 */
	public static Object retrieveFromCache(int cacheType, String virtualWiki, String topicName) {
		String key = key(virtualWiki, topicName);
		return WikiCache.retrieveFromCache(cacheType, key);
	}
}
