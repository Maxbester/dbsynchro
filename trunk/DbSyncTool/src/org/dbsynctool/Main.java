package org.dbsynctool;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

public class Main {

	private String configFile;
	private String statsFile;
	private ConfReader confReader;
	private SqlReader sqlReader;
	private SqlRunner sqlRunner;
	private Set<Database> databases;
	private List<Query> queries;

	private static final Logger log = Logger.getLogger(Main.class.getName());

	public Main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException {

		if(!checkParameters(args)) {
			return;
		}

		confReader = new ConfReader(configFile);

		confReader.read();

		databases = confReader.getDatabases();
		queries = new ArrayList<Query>();

		sqlReader = new SqlReader(statsFile, databases, queries);

		sqlReader.read();

		sqlRunner = new SqlRunner(queries);

		sqlRunner.runQueries();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		BasicConfigurator.configure();
		PropertyConfigurator.configure("log.properties");
		try {
			new Main(args);
		} catch (ParserConfigurationException e) {
			log.error(e);
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (ParseException e) {
			log.error(e);
		}

	}

	/**
	 * Checks the input parameters that are given to the main program.
	 * @param args
	 * @return
	 */
	private boolean checkParameters (String[] args) {
		if (args.length < 3) {
			log.error("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.error("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
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
			log.error("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.error("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
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
