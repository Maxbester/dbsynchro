package org.dbsynctool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Reads the configuration in "config.xml".
 * 
 * @author Maxime Buisson
 *
 */
public class ConfReader {
	
	private final File xml;

	private Config config;

	private static final Logger log = Logger.getLogger(ConfReader.class.getName());
	
	/**
	 * Loads config.xml, parses it and create the sourceServer and the distantServer
	 * 
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
	}

	/**
	 * Read the XML configuration file to retrieve information about the available databases and
	 * the notification email.
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 */
	public void read() throws ParserConfigurationException, SAXException, IOException, ParseException {

		if (xml == null) {
			log.error("The XML file to read does not exist.");
			throw new IOException("The XML file to read does not exist.");
		}

		String fileName = xml.getName();

		if(log.isDebugEnabled()) {
			log.debug("Reading the configuration ("+fileName+")");
		}

		/*
		 * The validate method says if either or not the XML file respects the XML schema
		 * defined in the validation.xsd file. If the XML configuration file is unvalid
		 * it is not necessary to continue the program. 
		 */
		if (!XmlUtil.validate("validation.xsd", fileName)) {
			log.error("The XML configuration file is unvalid according to the definition of the XML schema.");
			throw new SAXException("The XML configuration file is unvalid according to the definition of the XML schema.");
		}

		config = XmlUtil.readConfig(fileName);

		/*
		 * This part is not necessary anymore since the XML file is parsed with the XStream API.
		 */
		/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = factory.newDocumentBuilder();
		Document document = constructor.parse(xml);
		
		Element root = document.getDocumentElement();
		
		emailReader(root, xml.getName());

		serversReader(root, xml.getName());*/

		if(log.isInfoEnabled()) {
			log.info("Config file reading OK\n");
		}
	}
	
	/**
	 * This method is in charge with collecting all information referring to the servers.
	 * 
	 * Deprecated: Replaced by a mapping between the Dabatase class and the configuration XML file
	 * thanks to the XStream library.
	 * 
	 * @param root The root Element
	 * @throws MalformedInputException Thrown when the config file is mal formed.
	 * @throws ParseException
	 * @deprecated 
	 */
	private void serversReader(Element root, String file) throws ParseException {
		// store database nodes
		/*NodeList nodes = root.getElementsByTagName("database");

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
		}*/
	}

	/**
	 * 
	 * Deprecated: Replaced by a mapping between the Email class and the configuration XML file
	 * thanks to the XStream library.
	 * 
	 * @param root
	 * @param file
	 * @throws ParseException
	 * @deprecated
	 */
	private void emailReader(Element root, String file) throws ParseException {
		/*NodeList nodes = root.getElementsByTagName("email");
		
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
		*/
	}

	/**
	 * Return a set of available databases.
	 * @return
	 */
	public Set<Database> getDatabases() {
		return config.getDatabases();
	}

	/**
	 * Return true if an email configuration has been set.
	 * @return
	 */
	public boolean isEmail() {
		return (config.getEmail() == null ? false : true);
	}

	/**
	 * Return email configuration information.
	 * @return
	 */
	public Email getEmail() {
		return config.getEmail();
	}
}
