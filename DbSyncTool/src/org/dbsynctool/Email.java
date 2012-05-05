package org.dbsynctool;

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
	private final SmtpConfig smtpConfig;
	private final String subject;
	private Set<String> recipients;

	public Email(String from, Set<String> recipients, String smtp, String port, String subject) {
		this.from = from;
		this.recipients = recipients;
		this.smtpConfig = new SmtpConfig(smtp, port);
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public String getSmtpAddress() {
		return smtpConfig.getAddress();
	}

	public String getPort() {
		return smtpConfig.getPort();
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
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(smtpConfig);
		sb.append(") - from ");
		sb.append(from);
		sb.append(" to ");
		for (String s : recipients) {
			sb.append(s);
			sb.append(" ");
		}
		sb.append(" - subject: ");
		sb.append(subject);
		sb.append(".");
		return sb.toString();
	}

	/**
	 * Sends an email using the configuration fields.
	 * @param content
	 * @throws MessagingException
	 */
	public void send(String content) throws MessagingException {
		//Set the host smtp address
	     Properties props = new Properties();
	     props.put("mail.smtp.host", smtpConfig.getAddress());
	     props.put("mail.protocol.port", smtpConfig.getPort());

	     boolean auth = smtpConfig.hasAuth();
	     if (auth) {
	    	 props.put("mail.smtp.auth", "true");
	     }

	     // create some properties and get the default Session
	     Session session = Session.getDefaultInstance(props);
	     session.setDebug(false);

	     // create a message
	     MimeMessage message = new MimeMessage(session);
	     // set the from and to address
	     message.setFrom(new InternetAddress(from));

	     InternetAddress[] addressTo = new InternetAddress[recipients.size()];

	     for (int i = 0 ; i < recipients.size() ; i++) {
	         addressTo[i] = new InternetAddress((String)recipients.toArray()[0]);
	         i++;
	     }
	     message.setRecipients(Message.RecipientType.TO, addressTo);

	     // Setting the Subject and Content Type
	     message.setSubject(subject);
	     message.setText(content);

	     message.saveChanges();

	     Transport tr = session.getTransport("smtp");
	     if (auth) {
	    	 tr.connect(smtpConfig.getAddress(), smtpConfig.getLogin(), smtpConfig.getPassword());
	     }
	     tr.sendMessage(message,message.getAllRecipients());

	     tr.close();
	}
}
