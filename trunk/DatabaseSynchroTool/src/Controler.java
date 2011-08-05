import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Runs the program.
 * 
 * @author Maxime Buisson
 *
 */
public class Controler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Database Synchro Tool - "+ new Date());
		try {
			ConfReader cr = new ConfReader();

			System.out.println(cr.getSourceServer());
			
			System.out.println("\n");

			System.out.println(cr.getTargetServer());

			SqlReader sr = new SqlReader();

			System.out.println("Number of statements: "+sr.getStatements().size());

			System.out.println("Number of local statements: "+sr.getSourceStatements().size());


			System.out.println("Number of distant statements: "+sr.getTargetStatements().size());

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (MalformedInputException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
