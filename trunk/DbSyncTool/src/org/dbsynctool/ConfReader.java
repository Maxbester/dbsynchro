package org.dbsynctool;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
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

		if(log.isInfoEnabled()) {
			log.info("Config file reading OK\n");
		}
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
