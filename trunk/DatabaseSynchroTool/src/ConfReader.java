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
	private Server targetServer;
	
	/**
	 * Loads config.xml, parses it and create the sourceServer and the targetServer
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
		
		NodeList nodes = root.getElementsByTagName("server");
		Map<String, Element> servers = new HashMap<String, Element>();
		
		if (nodes.getLength() != 2) {
			System.out.println("ERROR: Two servers must be specified in the configuration file: config.xml. One for the source and one for the target.");
			throw new MalformedInputException(-1);
		}
		
		for (int i = 0 ; i < nodes.getLength() ; i++) {
			servers.put(((Element) nodes.item(i)).getAttribute("type"), (Element) nodes.item(i));
		}
		
		Element source = servers.get("source");
		Element target = servers.get("target");
		
		if (source == null) {
			System.out.println("ERROR: The source has not been specified in the configuration file: config.xml");
			throw new MalformedInputException(0);
		}
		
		if (target == null) {
			System.out.println("ERROR: The target has not been specified in the configuration file: config.xml");
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
			System.out.println("ERROR: Have a look at the source in the configuration file: config.xml");
			throw new MalformedInputException(1);
		}
		
		name = (Element) source.getElementsByTagName("name").item(0);
		url = (Element) source.getElementsByTagName("url").item(0);
		login = (Element) source.getElementsByTagName("login").item(0);
		password = (Element) source.getElementsByTagName("password").item(0);
		driver = (Element) source.getElementsByTagName("driver").item(0);
		
		sourceServer = new Server(name.getTextContent(), url.getTextContent(), login.getTextContent(), password.getTextContent(), driver.getTextContent());
		
		if (target.getElementsByTagName("name").getLength() != 1 || target.getElementsByTagName("url").getLength() != 1 ||
				target.getElementsByTagName("login").getLength() != 1 || target.getElementsByTagName("password").getLength() != 1 ||
				target.getElementsByTagName("driver").getLength() != 1) {
			System.out.println("ERROR: Have a look at the target in the configuration file: config.xml");
			throw new MalformedInputException(1);
		}
		
		name = (Element) target.getElementsByTagName("name").item(0);
		url = (Element) target.getElementsByTagName("url").item(0);
		login = (Element) target.getElementsByTagName("login").item(0);
		password = (Element) target.getElementsByTagName("password").item(0);
		driver = (Element) target.getElementsByTagName("driver").item(0);
		
		targetServer = new Server(name.getTextContent(), url.getTextContent(), login.getTextContent(), password.getTextContent(), driver.getTextContent());
		
		System.out.println("Config file reading OK");
	}

	
	public Server getSourceServer() {
		return sourceServer;
	}

	
	public Server getTargetServer() {
		return targetServer;
	}

}
