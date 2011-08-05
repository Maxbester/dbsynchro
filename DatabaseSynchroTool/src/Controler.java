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
	
	private static String configFile;
	private static String statsFile;

	/**
	 * The launcher of the program. You should run the program like: "java -jar DbSynchroTool.jar -c config.xml -s stats.sql"
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		if(!checkParameters(args)) {
			return;
		}
		
		System.out.println("Database Synchro Tool - "+ new Date());
		
		try {
			// reading of the configuration
			ConfReader cr = new ConfReader(configFile);

			System.out.println(cr.getSourceServer());
			
			System.out.println("\n");

			System.out.println(cr.getTargetServer());
			
//			cr.getTargetServer().connect();
//			ResultSet res = cr.getTargetServer().selectStatement("select * from items limit 50;");
//	        while (res.next()) {
//	        	System.out.println(res.getString("name"));
//	        }
//			cr.getTargetServer().disconnect();


			//reading of the statements
			SqlReader sr = new SqlReader(statsFile);

			System.out.println("Number of statements: "+sr.getStatements().size());
//			System.out.println("Statements: "+sr.getStatements());

			System.out.println("Number of local statements: "+sr.getSourceStatements().size());
			System.out.println("Local statements: "+sr.getSourceStatements());

			System.out.println("Number of distant statements: "+sr.getTargetStatements().size());
			System.out.println("Distant statements: "+sr.getTargetStatements());
			
			System.out.println("End of the program - "+ new Date());

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (MalformedInputException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkParameters (String[] args) {
		if (args.length < 4) {
			System.err.println("\nWrong use of this program. You must specify the location of the config file and the location of the statements file.");
			System.out.println("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
					"List of commands:\n" +
					"\t-c, --config FILE | DIRECTORY\n" +
					"\t\tThe location of the xml file which contains the database configuration of both local and distant servers.\n\n" +
					"\t-s, --statements FILE | DIRECTORY\n" +
					"\t\tThe location of the sql file which contains the sql statements to run.\n");
			return false;
		}

		boolean config = false;
		boolean stats = false;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("-c") || args[i].contains("--config")) {
				config = true;
				configFile = args[i+1];
			}
			if (args[i].contains("-s") || args[i].contains("--statements")) {
				stats = true;
				statsFile = args[i+1];
			}
		}
		
		if (!config || !stats) {
			System.err.println("\nWrong use of this program. You must specify the location of the config file and the location of the statements file.");
			System.out.println("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
					"List of commands:\n" +
					"\t-c, --config FILE | DIRECTORY\n" +
					"\t\tThe location of the xml file which contains the database configuration of both local and distant servers.\n\n" +
					"\t-s, --statements FILE | DIRECTORY\n" +
					"\t\tThe location of the sql file which contains the sql statements to run.\n");
			return false;
		}
		return true;
	}
}
