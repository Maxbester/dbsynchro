import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author Maxime Buisson
 *
 */
public class Email {
	
	private final String from;
	private final String smtp;
	private final String port;
	private final String subject;
	private Set<String> recipients;

	public Email(String from, String smtp, String port, String subject) {
		this.from = from;
		this.smtp = smtp;
		this.port = port;
		this.subject = subject;
		this.recipients = new HashSet<String>();
	}

	public String getFrom() {
		return from;
	}

	public String getSmtp() {
		return smtp;
	}

	public String getPort() {
		return port;
	}

	public String getSubject() {
		return subject;
	}

	public Set<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}
	
	public void addRecipient(String r) {
		this.recipients.add(r);
	}
	
	@Override
	public String toString() {
		String res;
		res = "\n("+smtp+":"+port+") - from "+from+" to "+recipients+", subject: "+subject+".";
		return res;
	}

	/**
	 * Sends an email using the configuration fields.
	 * @param content
	 * @throws MessagingException
	 */
	public void send(String content) throws MessagingException {
		//Set the host smtp address
	     Properties props = new Properties();
	     props.put("mail.smtp.host", smtp);
	     props.put("mail.protocol.port", port);
	     
	     // create some properties and get the default Session
	     Session session = Session.getDefaultInstance(props, null);
	     session.setDebug(false);
	     
	     // create a message
	     Message msg = new MimeMessage(session);
	     
	     // set the from and to address
	     InternetAddress addressFrom = new InternetAddress(from);
	     msg.setFrom(addressFrom);
	     
	     InternetAddress[] addressTo = new InternetAddress[recipients.size()];

	     for (int i = 0 ; i < recipients.size() ; i++) {
	         addressTo[i] = new InternetAddress((String)recipients.toArray()[0]);
	         i++;
	     }
	     msg.setRecipients(Message.RecipientType.TO, addressTo);
	     
	     // Setting the Subject and Content Type
	     msg.setSubject(subject);
	     msg.setContent(content, "text/plain");
	     Transport.send(msg);
	}
}
