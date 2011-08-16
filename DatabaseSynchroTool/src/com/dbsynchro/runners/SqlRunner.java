package com.dbsynchro.runners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

import com.dbsynchro.connection.Server;
import com.dbsynchro.util.Query;
import com.google.common.collect.Multimap;

/**
 * 
 * @author Maxime Buisson
 *
 */
public class SqlRunner {
	
	private final Logger log = Logger.getLogger("SqlRunner");
	private String emailContent;

	/**
	 * Runs the input queries. The results are stocked in specific fields in the Server objects.
	 * @param email 
	 * @param logger
	 * @param list
	 * @param multimap
	 * @param servers
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	public SqlRunner(Handler logHandler, List<Query> sourceQueries, Multimap<Integer, Query> distantQueries, List<Server> servers) {
		log.addHandler(logHandler);
		log.setLevel(logHandler.getLevel());
		log.info(" -- Run SQL statements");
		emailContent = "";
		ResultSet sourceStatement;
		// index of source queries
		int i = 0;

		// for each source query
		for (Query q : sourceQueries) {
			Server sourceServer = null;
			
			// we select the good server in the list of servers
			for (Server s : servers) {
				if (s.getName().equalsIgnoreCase(q.getServerName())) {
					sourceServer = s;
					break;
				}
			}			
			
			// if source server we picked up does not exist we go to the next source server
			if (sourceServer == null) {
				log.info("This source server does not exist.");
				continue;
			}
			
			log.config(sourceServer.getName()+": "+q.getStatement().replaceAll("[\t\n\r]", " "));

			// we loop over the results of the statement
			try {
				// we connect to the server database
				sourceServer.connect();
				// we run the statement
				sourceStatement = sourceServer.selectStatement(q.getStatement());
				while (sourceStatement.next()) {
					Server distantServer = null;
					// for each distant query corresponding to the current source server (thanks to 'i')
					for (Query distantQuery : distantQueries.get(i)) {
						// we select the good server in the list of servers
						for (Server s : servers) {
							if (s.getName().equalsIgnoreCase(distantQuery.getServerName())) {
								distantServer = s;
								break;
							}
						}
						if (distantServer != null) {
							log.config("Affected rows: "+runDistantQuery(distantQuery.getStatement(), sourceStatement, distantServer));
						} else {
							log.warning("WARNING: a distant server was not found. Check both configuration and statements files.");
							emailContent += "WARNING: a distant server was not found. Check both configuration and statements files.\n";
						}
					}
				}
			} catch (SQLException e) {
				log.severe("ERROR: "+e.getMessage());
				e.printStackTrace();
				emailContent += "ERROR: "+e.getMessage()+"\n";
			}
			sourceServer.disconnect();
			i++;
		}
		log.info("End of queries execution.");
	}
	
	private int runDistantQuery(String query, ResultSet sourceStatement, Server distantServer) {
		if (sourceStatement == null || query == null) {
			log.warning("WARNING: distant query or source statement null.");
			return 0;
		}
		int open = 0;
		int close = 0;
		String temp = query;
		// we replace the square bracket by the good values
		try {
			while (temp.contains("[")) {
				open = temp.indexOf("[");
				close = temp.indexOf("]");
				int column = Integer.parseInt(temp.substring(open+1, close));
					query = query.replace(temp.substring(open, close+1), sourceStatement.getString(column));
				temp = temp.substring(close+1);
			}
			distantServer.connect();
			close = distantServer.simpleStatement(query);
			log.config(distantServer.getName()+": "+query.replaceAll("[\t\n\r]", " "));
			distantServer.disconnect();
		} catch (SQLException e) {
			log.severe("ERROR: "+e.getMessage());
			e.printStackTrace();
			emailContent += "ERROR: "+e.getMessage()+"\n";
		}

		// return number of affected rows.
		return close;
	}
	
	public String getEmailContent() {
		return emailContent;
	}
}
