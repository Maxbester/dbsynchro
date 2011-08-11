import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
	
	private static Level level;
	
	private static final Logger log = Logger.getLogger(Controler.class.getName());
	
	private static Handler handler;

	/**
	 * The launcher of the program. You should run the program like: "java -jar DbSynchroTool.jar -c config.xml -s stats.sql"
	 * @param args 
	 * @throws MessagingException 
	 */
	public static void main(String[] args) {

		try {
			handler = new FileHandler("DbSynchro-%g.log", 5242880, 5, true);

			handler.setFormatter(new Formatter() {

				private SimpleDateFormat format	= new SimpleDateFormat("dd-MMM-yyyy HH:mm:SSS");

				@Override
				public String format(LogRecord record) {
					return "[" + format.format(new Date(record.getMillis())) + "] [" + record.getLoggerName() + "] "
							+ record.getMessage() + "\n";
				}
			});
		} catch (SecurityException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}		
		
		if(!checkParameters(args)) {
			return;
		}
		
		handler.setLevel(level);
		log.addHandler(handler);
		log.setLevel(level);

		log.severe("------------------------- START Database Synchro Tool -------------------------");
		
		try {
			test1();
//			test2();
		} catch (MessagingException e1) {
			e1.printStackTrace();
			log.warning("EMAIL not sent: "+e1.getMessage());
		}
		log.severe("-- END of the program");
	}
	
	public static void test2() throws MessagingException {
		Email email = null;
		
		ConfReader cr = null;

		// reading of the configuration
		try {
			cr = new ConfReader(configFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
		}

		log.config(cr.getServers().toString());

		if (cr.isEmail()) {
			email = cr.getEmail();
			log.config(email.toString());
		}

		//reading of the statements
		try {
			new SqlReader(statsFile);
		} catch (MalformedInputException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			if (email != null) 
				email.send(e.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			if (email != null) 
				email.send(e.toString());
		}
	}

	public static void test1() throws MessagingException {
		Email email = null;
		
		ConfReader cr = null;

		// reading of the configuration
		try {
			cr = new ConfReader(configFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
		}

		log.config(cr.getServers().toString());

		if (cr.isEmail()) {
			email = cr.getEmail();
			log.config(email.toString());
		}

		//reading of the statements
		SqlReader sr = null;
		try {
			sr = new SqlReader(statsFile);
		} catch (MalformedInputException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			if (email != null) 
				email.send(e.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			if (email != null) 
				email.send(e.toString());
		}

			log.info("Number of local statements: "+sr.getsQueries().size());
			log.config("Local statements: "+sr.getsQueries());

			log.info("Number of distant statements: "+sr.getdQueries().size());
			log.config("Distant statements: "+sr.getdQueries());
			
			try {
				new SqlRunner(sr.getsQueries(), sr.getdQueries(), cr.getServers());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				log.severe("ERROR: "+e.getMessage());
				if (email != null) 
					email.send(e.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				log.severe("ERROR: "+e.getMessage());
				if (email != null) 
					email.send(e.toString());
			} catch (InstantiationException e) {
				e.printStackTrace();
				log.severe("ERROR: "+e.getMessage());
				if (email != null) 
					email.send(e.toString());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				log.severe("ERROR: "+e.getMessage());
				if (email != null) 
					email.send(e.toString());
			}
	}
	
	/**
	 * Checks the input parameters that are given to the main program.
	 * @param args
	 * @return
	 */
	public static boolean checkParameters (String[] args) {
		if (args.length < 4) {
			log.severe("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.severe("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.severe("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
					"List of commands:\n" +
					"\t-c, --config FILE | DIRECTORY\n" +
					"\t\tThe location of the xml file which contains the database configuration of both local and distant servers.\n\n" +
					"\t-q, --queries FILE | DIRECTORY\n" +
					"\t\tThe location of the sql file which contains SQL queries to run.\n" +
					"\t-l, --logger LEVEL\n" +
					"\t\tThe level of the logger, by default WARNING (SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, OFF).\n");
			return false;
		}

		boolean config = false;
		boolean stats = false;
		boolean logger = false;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("-c") || args[i].contains("--config")) {
				config = true;
				configFile = args[i+1];
			}
			if (args[i].contains("-q") || args[i].contains("--queries")) {
				stats = true;
				statsFile = args[i+1];
			}
			if (args[i].contains("-l") || args[i].contains("--logger")) {
				logger = true;
				level = Level.parse(args[i+1].toUpperCase());
			}
		}
		
		if (!config || !stats) {
			log.severe("\nWrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.severe("Wrong use of this program. You must specify the location of the config file and the location of the statements file.");
			log.severe("For information about DatabaseSynchroTool, visit: http://code.google.com/p/dbsynchro/\n" +
					"List of commands:\n" +
					"\t-c, --config FILE | DIRECTORY\n" +
					"\t\tThe location of the xml file which contains the database configuration of both local and distant servers.\n\n" +
					"\t-q, --queries FILE | DIRECTORY\n" +
					"\t\tThe location of the sql file which contains SQL queries to run.\n" +
					"\t-l, --logger LEVEL\n" +
					"\t\tThe level of the logger, by default WARNING (SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, OFF).\n");
			return false;
		}
		if (logger == false) {
			level = Level.WARNING;	
		}
		return true;
	}

	public static Handler getHandler() {
		return handler;
	}

	public static Level getLevel() {
		return level;
	}
}
