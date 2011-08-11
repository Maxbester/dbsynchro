package com.dbsynchro.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.dbsynchro.Controler;
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
	private final Logger log = Logger.getLogger(SqlReader.class.getName());
	
	/**
	 * Analyzes the query file. The results are stocked in sQueries and dQueries.
	 * @param level 
	 * @param statsFile The path and name of the file that contains the queries. 
	 * @throws FileNotFoundException
	 * @throws MalformedInputException
	 */
	public SqlReader(String statsFile) throws FileNotFoundException, MalformedInputException {
		log.addHandler(Controler.getHandler());
		log.setLevel(Controler.getLevel());
		log.info(" -- Reading of the statements ("+statsFile+")");

		sqlFile = new File(statsFile);
		Scanner scanner = new Scanner(sqlFile);
		scanner.useDelimiter(Pattern.compile(";"));
		sQueries = new ArrayList<Query>();
		dQueries = ArrayListMultimap.create();
		
		int i=0;
		while (scanner.hasNext()) {
			String s = scanner.next();
			
			// if there is a problem whit the scanner we continue
			// (could change 3 to a higher value)
			if (s.length() < 3) {
				continue;
			}
			
			// clean all lines
			if (s.contains("\n")) {
				s = s.replaceAll("\\n", " ");
			}
			// remove all spaces at the beginning of the scanner
			while (s.startsWith(" ")) {
				s = s.substring(1);
			}
			
			// if the scanner is a source server
			if (s.startsWith("s:")) {
				i = source(i, s);
			// if the scanner is a distant server
			} else if (s.startsWith("d:")) {
				distant(i, s);
			// if the scanner is a comment
			} else if (s.startsWith("--")) {
				log.finest("COMMENT");
				if (s.indexOf("s:") != -1) {
					i = source(i, s.substring(s.indexOf("s:")));
				} else if (s.indexOf("d:") != -1) {
					distant(i, s.substring(s.indexOf("d:")));
				}
				else {
					log.warning("WARNING: NOTHING");
				}
			} else {
				log.severe("ERROR: the query "+s+" in the file ("+statsFile+") is malformed.");
				throw new MalformedInputException(3);
			}
		}
		log.config("SQL file reading OK");
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
