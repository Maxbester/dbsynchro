package com.dbsynchro.readers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dbsynchro.connection.Server;
import com.dbsynchro.util.Email;

/**
 * Reads the configuration in "config.xml".
 * 
 * @author Maxime Buisson
 *
 */
public class ConfReader {
	
	private final File xml;

	private List<Server> servers;
	private Email email;
	private final Logger log = Logger.getLogger(ConfReader.class.getName());
	private Handler logHandler;
	
	/**
	 * Loads config.xml, parses it and create the sourceServer and the distantServer
	 * @param log 
	 * @param file
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public ConfReader (Handler logHandler, String file) throws ParserConfigurationException, IOException, SAXException {
		log.addHandler(logHandler);
		log.setLevel(logHandler.getLevel());
		this.logHandler = logHandler;

		log.info(" -- Reading of the configuration ("+file+")");
		
		xml = new File(file);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = factory.newDocumentBuilder();
		Document document = constructor.parse(xml);
		
		Element root = document.getDocumentElement();
		
		emailReader(root, file);
		
		servers = new ArrayList<Server>();

		serversReader(root, file);
		
		this.log.config(" -- Config file reading OK");
	}
	
	/**
	 * This method is in charge with collecting all information referring to the servers.
	 * @param root The root Element
	 * @throws MalformedInputException Thrown when the config file is mal formed.
	 */
	private void serversReader(Element root, String file) throws MalformedInputException {
		// store server nodes
		NodeList nodes = root.getElementsByTagName("server");

		List<Element> servers;
		
		if (nodes.getLength() < 2) {
			log.severe("ERROR: At least two servers must be specified in the configuration file: "+file+". One for the source and one for the target.");
			throw new MalformedInputException(-1);
		}
		
		servers = new ArrayList<Element>();
		
		for (int i = 0 ; i < nodes.getLength() ; i++) {
			servers.add((Element) nodes.item(i));
		}
		
		Element name;
		Element url;
		Element login;
		Element password;
		Element driver;
		
		for (Element serv : servers) {
			if (serv.getElementsByTagName("name").getLength() != 1 || serv.getElementsByTagName("url").getLength() != 1 ||
					serv.getElementsByTagName("login").getLength() != 1 || serv.getElementsByTagName("password").getLength() != 1 ||
					serv.getElementsByTagName("driver").getLength() != 1) {
				log.severe("ERROR: Have a look at the servers definition in the configuration file: "+file+".");
				throw new MalformedInputException(1);
			}
			
			name = (Element) serv.getElementsByTagName("name").item(0);
			url = (Element) serv.getElementsByTagName("url").item(0);
			login = (Element) serv.getElementsByTagName("login").item(0);
			password = (Element) serv.getElementsByTagName("password").item(0);
			driver = (Element) serv.getElementsByTagName("driver").item(0);
			
			this.servers.add(new Server(logHandler, name.getTextContent(), url.getTextContent(), login.getTextContent(), password.getTextContent(), driver.getTextContent()));
		}		
	}

	private void emailReader(Element root, String file) throws MalformedInputException {
		NodeList nodes = root.getElementsByTagName("email");
		
		if (nodes.getLength() > 1) {
			log.severe("ERROR: Only one email must be specified in the configuration file: "+file+".");
			throw new MalformedInputException(0);
		}
		
		// no email
		if (nodes.getLength() == 0) {
			return;
		}
		
		Element email = (Element) nodes.item(0);
		
		if (email.getElementsByTagName("from").getLength() != 1 || email.getElementsByTagName("smtp").getLength() != 1 ||
				 email.getElementsByTagName("subject").getLength() != 1) {
			log.severe("ERROR: Have a look at the email conf in the configuration file: "+file+".");
			throw new MalformedInputException(1);
		}
		
		Element from = (Element) email.getElementsByTagName("from").item(0);
		Element smtp = (Element) email.getElementsByTagName("smtp").item(0);
		String port = smtp.getAttribute("port");
		Element subject = (Element) email.getElementsByTagName("subject").item(0);
		
		this.email = new Email(logHandler, from.getTextContent(), smtp.getTextContent(), port, subject.getTextContent());

		if (email.getElementsByTagName("recipient") != null) {
			for (int i = 0 ; i < email.getElementsByTagName("recipient").getLength() ; i++) {
				this.email.addRecipient(email.getElementsByTagName("recipient").item(i).getTextContent());
			}			
		}
		else {
			log.warning("WARNING: no email recipients in the configuration file: "+file+". No email will be sent.");
		}
	}

	/**
	 * Return a list of servers information
	 * @return
	 */
	public List<Server> getServers() {
		return servers;
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
