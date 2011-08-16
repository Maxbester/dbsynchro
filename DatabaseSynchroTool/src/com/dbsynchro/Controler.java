package com.dbsynchro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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

import com.dbsynchro.readers.ConfReader;
import com.dbsynchro.readers.SqlReader;
import com.dbsynchro.runners.SqlRunner;
import com.dbsynchro.util.Email;

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
			// turn over 3Mb (5 files max)
			handler = new FileHandler("DbSynchro-%g.log", 3145728, 5, true);

			handler.setFormatter(new Formatter() {

				private SimpleDateFormat format	= new SimpleDateFormat("dd/MM/yy HH:mm:SS");

				@Override
				public String format(LogRecord record) {
					return "[" + format.format(new Date(record.getMillis())) + "][" + record.getLoggerName() + "] "
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
		} catch (MessagingException e1) {
			e1.printStackTrace();
			log.warning("EMAIL not sent: "+e1.getMessage());
		}
		log.severe("-- END of the program");
	}

	public static void test1() throws MessagingException {
		Email email = null;
		String emailContent = new Date() + "\n\n";
		
		ConfReader cr = null;

		// reading of the configuration
		try {
			cr = new ConfReader(handler, configFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			System.exit(-1);
		} catch (ParseException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			System.exit(-1);
		} catch (SAXException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			System.exit(-1);
		}

		log.config(cr.getServers().toString());

		if (cr.isEmail()) {
			email = cr.getEmail();
			log.config(email.toString());
		}

		//reading of the statements
		SqlReader sr = null;
		try {
			sr = new SqlReader(handler, statsFile);
		} catch (ParseException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			if (email != null) 
				email.send(emailContent+e.toString());
			System.exit(-1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.severe("ERROR: "+e.getMessage());
			if (email != null) 
				email.send(emailContent+e.toString());
			System.exit(-1);
		}

		log.info("Number of local statements: "+sr.getsQueries().size());

		log.info("Number of distant statements: "+sr.getdQueries().size());
			
		SqlRunner srunner = new SqlRunner(handler, sr.getsQueries(), sr.getdQueries(), cr.getServers());
		emailContent += srunner.getEmailContent() + "\n";
		
		if (email != null && emailContent.length() > 40) {
			email.send(emailContent);
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