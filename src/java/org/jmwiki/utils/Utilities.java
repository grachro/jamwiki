/**
 *
 */
package org.jmwiki.utils;

import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.jmwiki.Environment;
import org.jmwiki.WikiBase;
import org.jmwiki.persistency.file.FileHandler;

/**
 *
 */
public class Utilities {

	private static final Logger logger = Logger.getLogger(Utilities.class);
	private static final int STATE_NO_ENTITY = 0;
	private static final int STATE_AMPERSAND = 1;
	private static final int STATE_AMPERSAND_HASH = 2;

	/**
	 *
	 */
	public static String buildInternalLink(String context, String virtualWiki, String page) {
		String url = context;
		// context never ends with a "/" per servlet specification
		url += "/";
		// get the virtual wiki, which should have been set by the parent servlet
		url += JSPUtils.encodeURL(virtualWiki);
		url += "/";
		url += JSPUtils.encodeURL(page);
		return url;
	}

	/**
	 * Returns true if the given collection of strings contains the given string where the case
	 * of either is ignored
	 * @param collection collection of {@link String}s
	 * @param string string to find
	 * @return true if the string is in the collection with no regard to case
	 */
	public static boolean containsStringIgnoreCase(Collection collection, String string) {
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			String s = (String) iterator.next();
			if (s.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Convert the filename-friendly date format back to a Java date
	 */
	public static Date convertFileFriendlyDate(String filename) {
		Calendar cal = Calendar.getInstance();
		int position = filename.lastIndexOf(FileHandler.EXT) + FileHandler.EXT.length();
		if (position != -1) {
			// plus one to get rid of the point before the date.
			filename = filename.substring(position + 1);
		} else {
			logger.error("Didn't found Extension " + FileHandler.EXT + " in " + filename);
			return null;
		}
		StringTokenizer tokens = new StringTokenizer(filename, ".");
		cal.set(
			Integer.parseInt(tokens.nextToken()),
			Integer.parseInt(tokens.nextToken()) - 1,
			Integer.parseInt(tokens.nextToken()),
			Integer.parseInt(tokens.nextToken()),
			Integer.parseInt(tokens.nextToken()),
			Integer.parseInt(tokens.nextToken())
		);
		return cal.getTime();
	}

	/**
	 *
	 */
	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 *
	 */
	public static Cookie createUsernameCookie(String username) {
		Cookie c = new Cookie("username", username);
		c.setMaxAge(Environment.getIntValue(Environment.PROP_BASE_COOKIE_EXPIRE));
		return c;
	}

	/**
	 * Converts back file name encoded by encodeSafeFileName().
	 */
	public static String decodeSafeFileName(String name) {
		return JSPUtils.decodeURL(name, "utf-8");
	}

	/**
	 *
	 */
	public static String dir() {
		return Environment.getValue(Environment.PROP_FILE_HOME_DIR) + System.getProperty("file.separator");
	}

	/**
	 *
	 */
	public static boolean emailAvailable() {
		String smtpHost = Environment.getValue(Environment.PROP_EMAIL_SMTP_HOST);
		return (smtpHost != null && smtpHost.length() > 0);
	}

	/**
	 * Converts arbitrary string into string usable as file name.
	 */
	public static String encodeSafeFileName(String name) {
		StringTokenizer st = new StringTokenizer(name,"%"+File.separator,true);
		StringBuffer sb = new StringBuffer(name.length());
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (File.separator.equals(token) || token.equals("%")) {
				sb.append(token);
			} else {
				sb.append(JSPUtils.encodeURL(token, "utf-8"));
			}
		}
		return sb.toString();
	}

	/**
	 * Returns any trailing . , ; : characters on the given string
	 * @param text
	 * @return empty string if none are found
	 */
	public static String extractTrailingPunctuation(String text) {
		StringBuffer buffer = new StringBuffer();
		for (int i = text.length() - 1; i >= 0; i--) {
			char c = text.charAt(i);
			if (c == '.' || c == ';' || c == ',' || c == ':') {
				buffer.append(c);
			} else {
				break;
			}
		}
		if (buffer.length() == 0) return "";
		buffer = buffer.reverse();
		return buffer.toString();
	}

	/**
	 * Convert Java date to a file-friendly date
	 */
	public static String fileFriendlyDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return String.valueOf(cal.get(Calendar.YEAR)) + "." +
			padTensWithZero(cal.get(Calendar.MONTH) + 1) + "." +
			padTensWithZero(cal.get(Calendar.DATE)) + "." +
			padTensWithZero(cal.get(Calendar.HOUR_OF_DAY)) + "." +
			padTensWithZero(cal.get(Calendar.MINUTE)) + "." +
			padTensWithZero(cal.get(Calendar.SECOND));
	}

	/**
	 * Localised
	 */
	public static String formatDate(Date date) {
		return DateFormat.getDateInstance().format(date);
	}

	/**
	 *
	 */
	public static String formatDateTime(Date date) {
		return DateFormat.getDateTimeInstance().format(date);
	}

	/**
	 *
	 */
	private static String generateNewAdminPassword() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 5; i++) {
			int n = (int) (Math.random() * 26 + 65);
			buffer.append((char) n);
		}
		String value = buffer.toString();
		return value;
	}

	/**
	 *
	 */
	public static String getUserFromRequest(HttpServletRequest request) {
		if (request.getRemoteUser() != null) {
			return request.getRemoteUser();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies == null) return null;
		if (cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("username")) {
					return cookies[i].getValue();
				}
			}
		}
		return null;
	}

	/**
	 *
	 */
	public static boolean isAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String value = (String) session.getAttribute("admin");
		return "true".equals(value);
	}

	/**
	 * Utility method for determining if a String is null or empty.
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.length() == 0);
	}

	/**
	 *
	 */
	public static boolean isFirstUse() {
		if (Environment.getBooleanValue(Environment.PROP_BASE_FIRST_USE)) {
			logger.info("First use of JMWiki, creating admin password");
			try {
				Encryption.setEncryptedProperty(Environment.PROP_BASE_ADMIN_PASSWORD, generateNewAdminPassword());
				Environment.setBooleanValue(Environment.PROP_BASE_FIRST_USE, false);
				Environment.saveProperties();
			} catch (Exception e) {
				logger.error(e);
			}
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	protected static String padTensWithZero(int n) {
		if (n < 10) {
			return "0" + n;
		} else {
			return String.valueOf(n);
		}
	}

	/**
	 * Use standard factory to create DocumentBuilder and parse a file
	 */
	public static Document parseDocumentFromFile(String fileName) throws Exception {
		File file = new File(fileName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(file);
	}

	/**
	 * Parse DOM document from XML in input stream
	 * @param xmlIn
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document parseDocumentFromInputStream(InputStream xmlIn) throws IOException,
		SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(xmlIn);
	}

	/**
	 * Read a file from the file system
	 * @param file The file to read
	 * @return a Stringbuffer with the content of this file or an empty StringBuffer, if an error has occured
	 */
	public static StringBuffer readFile(File file) {
		char[] buf = new char[1024];
		StringBuffer content = new StringBuffer((int)file.length());
		try {
			Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),Environment.getValue(Environment.PROP_FILE_ENCODING)));
			int numread = 0;
			while((numread=in.read(buf))!=-1) {
				content.append(buf,0,numread);
			}
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return content;
	}

	/**
	 * Read a file and return its contents as a String.
	 */
	public static String readFile(String filename) throws Exception {
		StringBuffer output = new StringBuffer();
		InputStreamReader reader = null;
		try {
			File file = new File(filename);
			if (file.exists()) {
				// file passed in as full path
				reader = new FileReader(file);
			} else {
				// look for file in resource directories
				Class[] parameterTypes = null;
				Method method = Thread.class.getMethod("getContextClassLoader", parameterTypes);
				Object[] args = null;
				ClassLoader loader = (ClassLoader)method.invoke(Thread.currentThread(), args);
				InputStream stream = loader.getResourceAsStream(filename);
				if (stream == null) {
					throw new FileNotFoundException("File " + filename + " is not available for reading");
				}
				reader = new InputStreamReader(stream);
			}
			char[] buf = new char[4096];
			int c;
			while ((c = reader.read(buf, 0, buf.length)) != -1) {
				output.append(buf, 0, c);
			}
			return output.toString();
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {}
		}
	}

	/**
	 *
	 */
	public static String relativeDirIfNecessary(String path) {
		if (path.length() <= 2) {
			return path;
		}
		if (!path.startsWith("/") && !(Character.isLetter(path.charAt(0)) && path.charAt(1) == ':')) {
			return new File(Utilities.dir(), path).getAbsolutePath();
		}
		return path;
	}

	/**
	 * Replaces occurences of the find string with the replace string in the given text
	 * @param text
	 * @param find
	 * @param replace
	 * @return the altered string
	 */
	public static String replaceString(String text, String find, String replace) {
		StringBuffer buffer = new StringBuffer(text);
		int i = 0;
		while ((i = buffer.indexOf(find, i)) != -1) {
			buffer.replace(i, i + find.length(), replace);
			i = i + replace.length();
		}
		return buffer.toString();
	}

	/**
	 *
	 */
	public static String sep() {
		return System.getProperty("file.separator");
	}

	/**
	 * Converts CamelCase to seperate words.
	 */
	public static String separateWords(String text) {
		// Do not try to separateWords if there are spaces in the text.
		if (text.indexOf(" ") != -1) return text ;
		// Allocate enough space for the new string, plus
		// magic number (5) for a guess at the likely max
		// number of words.
		StringBuffer sb = new StringBuffer(text.length() + 5) ;
		int offset = 0 ;  // points to the start of each word.
		// Loop through the CamelCase text, at each capital letter
		// concatenate the previous word text to the result
		// and append a space.
		// The first character is assumed to be a "capital".
		for (int i=1; i < text.length(); i++) {
			if ((text.charAt(i) >= 'A') && (text.charAt(i) <= 'Z')) {
				// Append the current word and a trailing space.
				sb.append (text.substring(offset, i)).append (" ") ;
				// Start of the next word.
				offset = i ;
			}
		}
		// Append the last word.
		sb.append (text.substring (offset)) ;
		return sb.toString () ;
	}

	/**
	 *
	 */
	public static void unzip(File zipFileToOpen, File outputDirectory) {
		Enumeration entries;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(zipFileToOpen);
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					logger.debug("Extracting directory: " + entry.getName());
					// This is not robust, just for demonstration purposes.
					File file = new File(outputDirectory, entry.getName());
					file.mkdir();
				}
			}
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (!entry.isDirectory()) {
					logger.debug("Extracting file: " + entry.getName());
					File outputFile = new File(outputDirectory, entry.getName());
					copyInputStream(zipFile.getInputStream(entry),
					new BufferedOutputStream(new FileOutputStream(outputFile)));
				}
			}
			zipFile.close();
		} catch (IOException ioe) {
			logger.error("Unzipping error: " + ioe);
			ioe.printStackTrace();
			return;
		}
	}

	/**
	 * The directory to place attachments in. This is either an absolute path if the admin setting for "upload directory"
	 * starts with a "/" or a drive letter, or it is a relative path.
	 * @param virtualWiki
	 * @param name
	 * @return
	 */
	public static File uploadPath(String virtualWiki, String name) {
		String path = Environment.getValue(Environment.PROP_ATTACH_UPLOAD_DIR);
		String dir = Utilities.relativeDirIfNecessary(path);
		if (virtualWiki == null || "".equals(virtualWiki)) {
			virtualWiki = WikiBase.DEFAULT_VWIKI;
		}
		File baseDir = new File(dir, virtualWiki);
		baseDir.mkdirs();
		dir = baseDir.getAbsolutePath();
		File uploadedFile = new File(dir, name);
		return uploadedFile;
	}
}
