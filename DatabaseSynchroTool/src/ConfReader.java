import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
	
	private Server sourceServer;
	private Server distantServer;
	private Email email;
	
	/**
	 * Loads config.xml, parses it and create the sourceServer and the distantServer
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public ConfReader (String file) throws ParserConfigurationException, IOException, SAXException {
		System.out.println("\n-- Reading of the configuration ("+file+")");
		
		xml = new File(file);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = factory.newDocumentBuilder();
		Document document = constructor.parse(xml);
		
		Element root = document.getDocumentElement();

		emailReader(root, file);

		serversReader(root, file);
		
		System.out.println("Config file reading OK");
	}
	
	/**
	 * This method is in charge with collecting all information referring to the servers.
	 * @param root The root Element
	 * @throws MalformedInputException Thrown when the config file is mal formed.
	 */
	private void serversReader(Element root, String file) throws MalformedInputException {
		// store server nodes
		NodeList nodes = root.getElementsByTagName("server");
		
		// the key represents the type of the server (source or target)
		Map<String, Element> servers = new HashMap<String, Element>();
		
		if (nodes.getLength() != 2) {
			System.out.println("ERROR: Two servers must be specified in the configuration file: "+file+". One for the source and one for the target.");
			throw new MalformedInputException(-1);
		}
		
		for (int i = 0 ; i < nodes.getLength() ; i++) {
			servers.put(((Element) nodes.item(i)).getAttribute("type"), (Element) nodes.item(i));
		}
		
		Element source = servers.get("source");
		Element distant = servers.get("target");
		
		if (source == null) {
			System.out.println("ERROR: The source has not been specified in the configuration file: "+file+".");
			throw new MalformedInputException(0);
		}
		
		if (distant == null) {
			System.out.println("ERROR: The target has not been specified in the configuration file: "+file+".");
			throw new MalformedInputException(0);
		}
		
		Element name;
		Element url;
		Element login;
		Element password;
		Element driver;
		
		if (source.getElementsByTagName("name").getLength() != 1 || source.getElementsByTagName("url").getLength() != 1 ||
				source.getElementsByTagName("login").getLength() != 1 || source.getElementsByTagName("password").getLength() != 1 ||
				source.getElementsByTagName("driver").getLength() != 1) {
			System.out.println("ERROR: Have a look at the source in the configuration file: "+file+".");
			throw new MalformedInputException(1);
		}
		
		name = (Element) source.getElementsByTagName("name").item(0);
		url = (Element) source.getElementsByTagName("url").item(0);
		login = (Element) source.getElementsByTagName("login").item(0);
		password = (Element) source.getElementsByTagName("password").item(0);
		driver = (Element) source.getElementsByTagName("driver").item(0);
		
		sourceServer = new Server(name.getTextContent(), url.getTextContent(), login.getTextContent(), password.getTextContent(), driver.getTextContent());
		
		if (distant.getElementsByTagName("name").getLength() != 1 || distant.getElementsByTagName("url").getLength() != 1 ||
				distant.getElementsByTagName("login").getLength() != 1 || distant.getElementsByTagName("password").getLength() != 1 ||
				distant.getElementsByTagName("driver").getLength() != 1) {
			System.out.println("ERROR: Have a look at the target in the configuration file: "+file+".");
			throw new MalformedInputException(1);
		}
		
		name = (Element) distant.getElementsByTagName("name").item(0);
		url = (Element) distant.getElementsByTagName("url").item(0);
		login = (Element) distant.getElementsByTagName("login").item(0);
		password = (Element) distant.getElementsByTagName("password").item(0);
		driver = (Element) distant.getElementsByTagName("driver").item(0);
		
		distantServer = new Server(name.getTextContent(), url.getTextContent(), login.getTextContent(), password.getTextContent(), driver.getTextContent());		
	}

	private void emailReader(Element root, String file) throws MalformedInputException {
		NodeList nodes = root.getElementsByTagName("email");
		
		if (nodes.getLength() > 1) {
			System.out.println("ERROR: Only one email must be specified in the configuration file: "+file+".");
			throw new MalformedInputException(0);
		}
		
		// no email
		if (nodes.getLength() == 0) {
			return;
		}
		
		Element email = (Element) nodes.item(0);
		
		if (email.getElementsByTagName("from").getLength() != 1 || email.getElementsByTagName("smtp").getLength() != 1 ||
				 email.getElementsByTagName("subject").getLength() != 1) {
			System.out.println("ERROR: Have a look at the email conf in the configuration file: "+file+".");
			throw new MalformedInputException(1);
		}
		
		Element from = (Element) email.getElementsByTagName("from").item(0);
		Element smtp = (Element) email.getElementsByTagName("smtp").item(0);
		String port = smtp.getAttribute("port");
		Element subject = (Element) email.getElementsByTagName("subject").item(0);
		
		this.email = new Email(from.getTextContent(), smtp.getTextContent(), port, subject.getTextContent());

		if (email.getElementsByTagName("recipient") != null) {
			for (int i = 0 ; i < email.getElementsByTagName("recipient").getLength() ; i++) {
				this.email.addRecipient(email.getElementsByTagName("recipient").item(i).getTextContent());
			}			
		}
		else {
			System.out.println("WARNING: no email recipients in the configuration file: "+file+". No email will be sent.");
		}
	}

	/**
	 * Return the source server information
	 * @return
	 */
	public Server getSourceServer() {
		return sourceServer;
	}

	/**
	 * Return the source server information
	 * @return
	 */
	public Server getDistantServer() {
		return distantServer;
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
