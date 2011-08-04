import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfReader {
	
	private static final File xml = new File("config.xml");
	
	private Server source;
	private Server target;
	
	public ConfReader () throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = factory.newDocumentBuilder();
		Document document = constructor.parse(xml);
		
		List<Element> source = new ArrayList<Element>();
		
		Element root = document.getDocumentElement();
		
		NodeList list = root.getElementsByTagName("source");
		
		for(int i=0 ; i < list.getLength() ; i++) {
			Element e = (Element)list.item(i);
			if(e.hasAttribute("href"))
				source.add(e);
		}

	}

}
