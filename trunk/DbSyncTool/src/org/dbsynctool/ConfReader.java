package org.dbsynctool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads the configuration in "config.xml".
 * 
 * @author Maxime Buisson
 *
 */
public class ConfReader {
	
	private final File xml;

	private List<Database> databases;
	private Email email;

	private final Logger log = Logger.getLogger(ConfReader.class.getName());
	
	/**
	 * Loads config.xml, parses it and create the sourceServer and the distantServer
	 * @param log 
	 * @param file
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public ConfReader(String file) {
		xml = new File(file);
		databases = new ArrayList<Database>();
	}

	public void read() throws ParserConfigurationException, SAXException, IOException, ParseException {
		if(log.isDebugEnabled()) {
			log.debug("Reading the configuration ("+xml.getName()+")");
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = factory.newDocumentBuilder();
		Document document = constructor.parse(xml);
		
		Element root = document.getDocumentElement();
		
		emailReader(root, xml.getName());

		serversReader(root, xml.getName());

		if(log.isInfoEnabled()) {
			log.info("Config file reading OK\n");
		}
	}
	
	/**
	 * This method is in charge with collecting all information referring to the servers.
	 * @param root The root Element
	 * @throws MalformedInputException Thrown when the config file is mal formed.
	 * @throws ParseException 
	 */
	private void serversReader(Element root, String file) throws ParseException {
		// store database nodes
		NodeList nodes = root.getElementsByTagName("database");

		List<Element> databaseList = null;
		
		if (nodes.getLength() < 2) {
			log.error("ERROR: At least two servers must be specified in the configuration file: "+file+". One for the source and one for the target.");
			throw new ParseException("ERROR: At least two servers must be specified in the configuration file: "+file+". One for the source and one for the target.", 0);
		}
		
		databaseList = new ArrayList<Element>();
		
		for (int i = 0 ; i < nodes.getLength() ; i++) {
			databaseList.add((Element) nodes.item(i));
		}
		
		Element name = null;
		Element url = null;
		Element login = null;
		Element password = null;
		Element driver = null;
		
		for (Element serv : databaseList) {
			if (serv.getElementsByTagName("name").getLength() != 1 || serv.getElementsByTagName("url").getLength() != 1 ||
					serv.getElementsByTagName("login").getLength() != 1 || serv.getElementsByTagName("password").getLength() != 1 ||
					serv.getElementsByTagName("driver").getLength() != 1) {
				log.error("ERROR: Have a look at the servers definition in the configuration file: "+file+".");
				throw new ParseException("ERROR: Have a look at the servers definition in the configuration file: "+file+".", 0);
			}
			
			name = (Element) serv.getElementsByTagName("name").item(0);
			url = (Element) serv.getElementsByTagName("url").item(0);
			login = (Element) serv.getElementsByTagName("login").item(0);
			password = (Element) serv.getElementsByTagName("password").item(0);
			driver = (Element) serv.getElementsByTagName("driver").item(0);
			
			Database db = new Database(name.getTextContent(), url.getTextContent(), login.getTextContent(),
					password.getTextContent(), driver.getTextContent());
			databases.add(db);
		}		
	}

	private void emailReader(Element root, String file) throws ParseException {
		NodeList nodes = root.getElementsByTagName("email");
		
		if (nodes.getLength() > 1) {
			log.error("ERROR: Only one email must be specified in the configuration file: "+file+".");
			throw new ParseException("ERROR: Only one email must be specified in the configuration file: "+file+".", 0);
		}
		
		// no email
		if (nodes.getLength() == 0) {
			return;
		}
		
		Element emailXml = (Element) nodes.item(0);
		
		if (emailXml.getElementsByTagName("from").getLength() != 1 || emailXml.getElementsByTagName("smtp").getLength() != 1 ||
				emailXml.getElementsByTagName("subject").getLength() != 1) {
			log.error("ERROR: Have a look at the email conf in the configuration file: "+file+".");
			throw new ParseException("ERROR: Have a look at the email conf in the configuration file: "+file+".", 0);
		}
		
		Element from = (Element) emailXml.getElementsByTagName("from").item(0);
		Element smtp = (Element) emailXml.getElementsByTagName("smtp").item(0);
		String port = smtp.getAttribute("port");
		Element subject = (Element) emailXml.getElementsByTagName("subject").item(0);

		Set<String> recipients = new HashSet<String>();

		if (emailXml.getElementsByTagName("recipient") != null) {
			for (int i = 0 ; i < emailXml.getElementsByTagName("recipient").getLength() ; i++) {
				recipients.add(emailXml.getElementsByTagName("recipient").item(i).getTextContent());
			}			
		}
		else {
			log.warn("WARNING: no email recipients in the configuration file: "+file+". No email will be sent.");
		}

		email = new Email(from.getTextContent(), recipients, smtp.getTextContent(), port, subject.getTextContent());
	}

	/**
	 * Return a list of servers information
	 * @return
	 */
	public List<Database> getDatabases() {
		return databases;
	}
	
	/**
	 * Return true if an email configuration has been set.
	 * @return
	 */
	public boolean isEmail() {
		return (email == null ? false : true);
	}

	public Email getEmail() {
		return email;
	}
}
