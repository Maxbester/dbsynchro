import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class Controler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConfReader cr = new ConfReader();
			System.out.println(cr.getSourceServer());
			System.out.println(cr.getTargetServer());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
