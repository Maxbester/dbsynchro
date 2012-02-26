package org.dbsynctool;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SqlReader {

	private static final Logger log = Logger.getLogger(SqlReader.class.getName());

	private final File sqlFile;

	private Set<Database> databases;
	private List<Query> queries;

	public SqlReader(String statsFile, Set<Database> databases, List<Query> queries) {
		this.databases = databases;
		this.queries = queries;
		sqlFile = new File(statsFile);
	}

	public void read() throws FileNotFoundException, ParseException {
		String fileName = sqlFile.getName();
		if(log.isDebugEnabled()) {
			log.debug("Reading the statement file ("+fileName+")");
		}
		Scanner scanner = new Scanner(sqlFile);

		int size = 0;
		int key = 0;

		Query query = null;

		// text starts with s or d followed by : then contains any character and finishes with ;
		String s = scanner.findWithinHorizon(Pattern.compile("[sd][:][^;]+[;]"),0);

		while (s != null) {

			size += s.length();
			
			boolean source = false;

			if (s.startsWith("s:")) {
				source = true;
				s = s.replaceAll("s:", "");
			} else if (s.startsWith("d:")) {
				s = s.replaceAll("d:", "");
			} else {
				scanner.close();
				throw new ParseException("The query "+s+" in the file ("+fileName+") is malformed.", size);
			}

			String dbName = s.substring(0, s.indexOf(":"));
			String statement = s.substring(s.indexOf(dbName)+dbName.length()+1);

			Database db = findByName(dbName);

			if (source) {
				query = new Query();
				key = 0;
				query.setSource(db);
				query.setSourceStatement(statement);
				if (log.isDebugEnabled()) {
					log.debug("Source: "+db+" -> "+statement);
				}
				queries.add(query);
				if (log.isTraceEnabled()) {
					log.trace(query);
				}
			} else {
				query.addTarget(key, db, statement);
				if (log.isDebugEnabled()) {
					log.debug("Target: "+db+" -> "+statement);
				}
				key++;
			}

			s = scanner.findWithinHorizon(Pattern.compile("[sd][:][^;]+[;]"),0);
		}
		log.info("SQL file reading OK\n");
		scanner.close();
	}


	private Database findByName(String name) {
		for (Database db : databases) {
			if (name.equals(db.getName())) {
				return db;
			}
		}
		return null;
	}
}
