/**
 *
 */
package org.jmwiki;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import org.apache.log4j.Logger;
import org.jmwiki.utils.Encryption;

/**
 *
 */
public class WikiMailAuthenticator extends Authenticator {

	private static final Logger logger = Logger.getLogger(WikiMailAuthenticator.class);

	/**
	 *
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication(
			Environment.getValue(Environment.PROP_EMAIL_SMTP_USERNAME),
			Encryption.getEncryptedProperty(Environment.PROP_EMAIL_SMTP_PASSWORD)
		);
		logger.debug("Authenticating with: " + passwordAuthentication);
		return passwordAuthentication;
	}
}
