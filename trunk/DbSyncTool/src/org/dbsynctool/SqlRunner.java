package org.dbsynctool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

/**
 * 
 * Run the SQL statements.
 * 
 * @author Maxime Buisson
 *
 */
public class SqlRunner {

	private static final Logger log = Logger.getLogger(SqlRunner.class.getName());
	private String emailContent;
	private List<Query> queries;

	/**
	 * Constructor
	 * @param queries
	 */
	public SqlRunner(List<Query> queries) {
		this.queries = queries;
	}

	/**
	 * Run all distant and source queries.
	 */
	public void runQueries() {
		log.info("Running SQL statements");

		if (queries == null) {
			log.error("Cannot run the queries because the list of queries is null.");
			return;
		}

		log.info("Number of source queries: "+queries.size());

		int changedRows = 0;

		feedAllSourceQueries();

		for (Query q : queries) {
			CachedRowSet sourceRS = null;
			try {
				sourceRS = q.getSourceSetRow();

				for (Map.Entry<Integer, Database> target : q.getTargets().entrySet()) {
					Database targetDb = target.getValue();
					int targetNumber = target.getKey();
					String targetStatement = q.getTargetStatement(targetNumber);

					targetDb.connect();
					while (sourceRS.next()) {
						changedRows += runDistantQuery(targetStatement, sourceRS, targetDb);
					}
					targetDb.disconnect();
				}
			} catch (SQLException e) {
				log.error("Cannot connect to the database: "+e.getMessage());
			} finally {
				try {
					sourceRS.close();
				} catch (SQLException e) {
					log.debug("Cannot close the ResultSet",e);
				}
			}
		}
		log.info("Affected rows: "+changedRows);
		log.info("SQL statements finished\n");
	}

	/**
	 * Run the select source statements and store them into the query list. This way, it will be easier to run the queries later.
	 */
	public void feedAllSourceQueries() {
		// cannot run query if the list is null
		if (queries == null) {
			return;
		}

		// select each query from the list
		for (Query q : queries) {
			Database sourceDb = q.getSourceDatabase();
			CachedRowSet sourceRS = null;
			try {
				// connect
				sourceDb.connect();
				sourceRS = sourceDb.selectStatement(q.getSourceStatement());
				q.setSourceSetRow(sourceRS);
			} catch (SQLException e) {
				log.error("Cannot connect to the database: "+e.getMessage());
			} finally {
				sourceDb.disconnect();
			}
		}
	}

	/**
	 * Run only the source statement in parameter.
	 * @param q
	 */
	public void feedSourceQuery(Query q) {
		// cannot run query if the list is null
		if (q == null) {
			return;
		}

		Database sourceDb = q.getSourceDatabase();
		CachedRowSet sourceRS = null;
		try {
			// connect
			sourceDb.connect();
			sourceRS = sourceDb.selectStatement(q.getSourceStatement());
			q.setSourceSetRow(sourceRS);
		} catch (SQLException e) {
			log.error("Cannot connect to the database: "+e.getMessage());
		} finally {
			sourceDb.disconnect();
		}
	}

	public int runDistantQuery(String targetStatement, ResultSet sourceRs, Database targetDb) {
		if (sourceRs == null || targetStatement == null || targetDb == null) {
			log.warn("Distant query or source statement null.");
			return 0;
		}

		int open = 0;
		int close = 0;
		int nbAffectedRows = 0;
		String temp = targetStatement;

		// we replace the square bracket by the good values
		try {
			while (temp.contains("[")) {
				open = temp.indexOf("[");
				close = temp.indexOf("]");
				int column = Integer.parseInt(temp.substring(open+1, close));

				String colValue = "NULL";
				// check that the value in the SQL Table is not null
				if (sourceRs.getString(column) != null) {
					colValue = sourceRs.getString(column);
				}

				targetStatement = targetStatement.replace(temp.substring(open, close+1), colValue);

				temp = (temp.substring(close+1) == null ? "" : temp.substring(close+1));
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			emailContent += "ERROR: "+e.getMessage()+"\n";
		}

		try {
			nbAffectedRows = targetDb.simpleStatement(targetStatement);
		} catch (SQLException e) {
			log.error(e.getMessage());
			emailContent += "ERROR: "+e.getMessage()+"\n";
		}

		// return number of affected rows.
		return nbAffectedRows;
	}

	/**
	 * Remove a query from the list of queries to run.
	 * 
	 * @param query
	 */
	public void removeQuery(Query query) {
		queries.remove(query);
	}

	public String getEmailContent() {
		return emailContent;
	}
}
