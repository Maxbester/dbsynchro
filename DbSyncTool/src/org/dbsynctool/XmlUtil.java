package org.dbsynctool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 *
 * This class is composed of static methods to validate and to read an XML file.
 * 
 * @author Maxime Buisson
 *
 */
public class XmlUtil {

	private static Logger log = Logger.getLogger(XmlUtil.class.getName());

	private static final String XML_SCHEMA_DEFINITION = "http://www.w3.org/2001/XMLSchema";

	/**
	 * 
	 * Read both XML file and XML schema file to determine if either or not the XML file respects
	 * the definition.
	 * 
	 * @param xsdFile XML schema definition.
	 * @param xmlFile XML configuration file.
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean validate(String xsdFile, String xmlFile) throws SAXException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("Starting to validate the XML configuration file...");
		}
		boolean result = false;

		try {
			File schemaLocation = new File(xsdFile);
			final SchemaFactory factory = SchemaFactory.newInstance(XML_SCHEMA_DEFINITION);
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();

			Source source = new StreamSource(xmlFile);

			try {
				validator.validate(source);
				result = true;
				if (log.isInfoEnabled()) {
					log.info(xmlFile + " is valid.\n");
				}
			} catch (SAXException e) {
				log.error(xmlFile + " is not valid because "+e.getMessage()+"\n");
			} catch (IOException e) {
				log.error("Cannot read the XML file", e);
				throw new IOException("Cannot read the XML file", e);
			}

		} catch (SAXException e) {
			log.warn("There is a problem with the XML schema file. So you cannot be sure your XML file is valid.", e);
			throw new SAXException("There is a problem with the XML schema file. So you cannot be sure your XML file is valid.");
		}

		return result;
	}

	/**
	 * Read the XML configuration file in parameter and return it as a Config object.
	 * 
	 * @param xmlFile
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static Config readConfig(String xmlFile) throws FileNotFoundException {

		// create the xstream object
		XStream xstream = new XStream(new StaxDriver());

		// map the config tag with the Config class
		xstream.alias("config", Config.class);
		// map the database tag with the Database class
		xstream.alias("database", Database.class);
		// map the email tag with the Email class
		xstream.alias("email", Email.class);
		// map the smtp tag with Smtp class
		xstream.alias("smtp", Smtp.class);

		// define that the databases are stored in an implicit collection
		xstream.addImplicitCollection(Config.class, "databases", "database", Database.class);
		// define that the email recipients are stored in an implicit collection
		xstream.addImplicitCollection(Email.class, "recipients", "recipient", String.class);

		// define the xml file content
		StringBuilder xmlContent = new StringBuilder();
		// scanner to read the file
		Scanner sc = null;
		try {
			// create the scanner
			sc = new Scanner(new FileInputStream(xmlFile));
			try {
				// while the file has an other line
				while (sc.hasNextLine()){
					// add this line to the string builder
					xmlContent.append(sc.nextLine());
					xmlContent.append("\n");
				}
			} finally{
				// close the scanner
				sc.close();
			}
		} catch (FileNotFoundException e) {
			log.fatal("Cannot find the XML configuration file "+xmlFile, e);
			throw new FileNotFoundException("Cannot find the XML configuration file "+xmlFile);
		}
		// map the XML file into a Config object
		Config config = (Config) xstream.fromXML(xmlContent.toString());
		return config;
	}
}
