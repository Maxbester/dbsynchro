import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Runs the program.
 * 
 * @author Maxime Buisson
 *
 */
public class Controler {
	
	private static String configFile;
	private static String statsFile;
	
	private static Logger log = Logger.getLogger("log");

	/**
	 * The launcher of the program. You should run the program like: "java -jar DbSynchroTool.jar -c config.xml -s stats.sql"
	 * @param args 
	 * @throws MessagingException 
	 */
	public static void main(String[] args) {
		
		if(!checkParameters(args)) {
			return;
		}
		
		System.out.println("Database Synchro Tool - "+ new Date());
		
		try {
			test();
		} catch (MessagingException e1) {
			System.err.println("EMAIL not sent: "+e1.getMessage());
		}
		System.out.println("End of the program - "+ new Date());
	}

	public static void test() throws MessagingException {
		Email email = null;
		
		ConfReader cr = null;

			// reading of the configuration
			try {
				cr = new ConfReader(configFile);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}

			System.out.println(cr.getSourceServer());
			
			System.out.println("\n");

			System.out.println(cr.getDistantServer());

			if (cr.isEmail()) {
				email = cr.getEmail();
				System.out.println(email);
			}

			//reading of the statements
			SqlReader sr = null;
			try {
				sr = new SqlReader(statsFile);
			} catch (MalformedInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				email.send(e.getMessage());
				e.printStackTrace();
			}

			System.out.println("Number of local statements: "+sr.getsQueries().size());
			System.out.println("Local statements: "+sr.getsQueries());

			System.out.println("Number of distant statements: "+sr.getdQueries().size());
			System.out.println("Distant statements: "+sr.getdQueries());
			
			try {
				new SqlRunner(sr.getsQueries(), sr.getdQueries(), cr.getSourceServer(), cr.getDistantServer());
			} catch (ClassNotFoundException e) {
				email.send(e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				email.send(e.getMessage());
				e.printStackTrace();
			}
	}
	
	/**
	 * Checks the input parameters that are given to the main program.
	 * @param args
	 * @return
	 */
	public static boolean checkParameters (String[] args) {
		if (args.length < 4) {
			System.err.println("\nWrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.config("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			System.out.println("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
					"List of commands:\n" +
					"\t-c, --config FILE | DIRECTORY\n" +
					"\t\tThe location of the xml file which contains the database configuration of both local and distant servers.\n\n" +
					"\t-q, --queries FILE | DIRECTORY\n" +
					"\t\tThe location of the sql file which contains SQL queries to run.\n");
			return false;
		}

		boolean config = false;
		boolean stats = false;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("-c") || args[i].contains("--config")) {
				config = true;
				configFile = args[i+1];
			}
			if (args[i].contains("-q") || args[i].contains("--queries")) {
				stats = true;
				statsFile = args[i+1];
			}
		}
		
		if (!config || !stats) {
			System.err.println("\nWrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.config("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			System.out.println("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
					"List of commands:\n" +
					"\t-c, --config FILE | DIRECTORY\n" +
					"\t\tThe location of the xml file which contains the database configuration of both local and distant servers.\n\n" +
					"\t-q, --queries FILE | DIRECTORY\n" +
					"\t\tThe location of the sql file which contains SQL queries to run.\n");
			return false;
		}
		return true;
	}
}
