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
	
	private final Logger log = Logger.getLogger(SqlRunner.class.getName());

	/**
	 * Runs the input queries. The results are stocked in specific fields in the Server objects.
	 * @param logger
	 * @param list
	 * @param multimap
	 * @param servers
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public SqlRunner(Handler logHandler, List<Query> sourceQueries, Multimap<Integer, Query> distantQueries, List<Server> servers) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		log.addHandler(logHandler);
		log.setLevel(logHandler.getLevel());
		log.info(" -- Run SQL statements");
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
			if (sourceServer == null)
				continue;
			
			// we connect to the server database
			sourceServer.connect();

			// we run the statement
			sourceStatement = sourceServer.selectStatement(q.getStatement());

			log.config("Source query: "+q.getStatement());

			// we loop over the results of the statement
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
					log.config("Affected rows: "+runDistantQuery(distantQuery.getStatement(), sourceStatement, distantServer));
				}
			}
			sourceServer.disconnect();
			i++;
		}
		log.info("Queries executed successfuly.");
	}
	
	private int runDistantQuery(String query, ResultSet sourceStatement, Server distantServer) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		int open = 0;
		int close = 0;
		String temp = query;
		// we replace the square bracket by the good values
		while (temp.contains("[")) {
			open = temp.indexOf("[");
			close = temp.indexOf("]");
			int column = Integer.parseInt(temp.substring(open+1, close));
			query = query.replace(temp.substring(open, close+1), sourceStatement.getString(column));
			temp = temp.substring(close+1);
		}

		distantServer.connect();
		log.config("Distant query: "+query);
		close = distantServer.simpleStatement(query);
		distantServer.disconnect();
		// return number of affected rows.
		return close;
	}
}