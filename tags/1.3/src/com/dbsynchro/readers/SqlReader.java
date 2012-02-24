package com.dbsynchro.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.dbsynchro.util.Query;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Reads the statements in "statements.sql".
 * 
 * @author Maxime Buisson
 *
 */
public class SqlReader {
	
	private final File sqlFile;
	private List<Query> sQueries;
	private Multimap<Integer, Query> dQueries;
	private final Logger log = Logger.getLogger("SqlReader");
	
	/**
	 * Analyzes the query file. The results are stocked in sQueries and dQueries.
	 * @param level 
	 * @param statsFile The path and name of the file that contains the queries. 
	 * @throws FileNotFoundException
	 * @throws MalformedInputException
	 */
	public SqlReader(Handler logHandler, String statsFile) throws FileNotFoundException, ParseException {
		log.addHandler(logHandler);
		log.setLevel(logHandler.getLevel());
		log.info(" -- Reading of the statements ("+statsFile+")");

		sqlFile = new File(statsFile);
		Scanner scanner = new Scanner(sqlFile);
		sQueries = new ArrayList<Query>();
		dQueries = ArrayListMultimap.create();
		
		int size = 0;
		
		int i=0;

		while (true) {
			// text starts with s or d followed by : then contains any character and finishes with ;
			String s = scanner.findWithinHorizon(Pattern.compile("[sd][:][^;]+[;]"),0);
			
			if (s == null)
				break;

			size += s.length();
			
			// if the scanner is a source server
			if (s.startsWith("s:")) {
				i = source(i, s);
			// if the scanner is a distant server
			} else if (s.startsWith("d:")) {
				distant(i, s);
			} else {
				log.severe("ERROR: the query "+s+" in the file ("+statsFile+") is malformed.");
				scanner.close();
				throw new ParseException("ERROR: a query in the file ("+statsFile+") is malformed.", size);
			}
		}
		log.config(" -- SQL file reading OK");
		scanner.close();
	}
	
	private int source(int i, String s) {
		s = s.replaceAll("s:", "");
		String name = s.substring(0, s.indexOf(":"));
		String q = s.substring(s.indexOf(name)+name.length()+1);
		sQueries.add(new Query(name, q));
		i = sQueries.size();
		log.finest("SOURCE: name="+name+", query="+q);
		return i;
	}
	
	private void distant(int i, String s) {
		s = s.replaceAll("d:", "");
		String name = s.substring(0, s.indexOf(":"));
		String q = s.substring(s.indexOf(name)+name.length()+1);
		dQueries.put(i-1, new Query(name, q));
		log.finest("DISTANT: name="+name+", query="+q);
	}

	/**
	 * Returns the queries that will be executed on the distant database.
	 * @return
	 */
	public Multimap<Integer, Query> getdQueries() {
		return dQueries;
	}

	/**
	 * Returns the queries that will be executed on the source database.
	 * @return
	 */
	public List<Query> getsQueries() {
		return sQueries;
	}
}
