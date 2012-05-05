package org.dbsynctool;

import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

public class Query {

	/**
	 * Source database.
	 */
	private Database sourceDb;
	/**
	 * Source statement to run.
	 */
	private String sourceStatement;
	/**
	 * Results of the sourceDb statement. This object will be set running feedSourceQuery from SqlRunner.
	 */
	private CachedRowSet sourceRowSet;

	/**
	 * Target databases. The key is used to linked them with their statements.
	 */
	private Map<Integer, Database> targets;
	private Map<Integer, String> targetStatements;

	public Query() {
		targets = new HashMap<Integer, Database>();
		targetStatements = new HashMap<Integer, String>();
	}

	/**
	 * @return the sourceDb
	 */
	public Database getSourceDatabase() {
		return sourceDb;
	}

	/**
	 * @return the target
	 */
	public Database getTargetDatabase(int key) {
		return targets.get(key);
	}

	/**
	 * @return the sourceStatement
	 */
	public String getSourceStatement() {
		return sourceStatement;
	}

	/**
	 * @return the targetStatement
	 */
	public String getTargetStatement(int key) {
		return targetStatements.get(key);
	}

	/**
	 * @param sourceDb the sourceDb to set
	 */
	public void setSource(Database source) {
		this.sourceDb = source;
	}

	/**
	 * @param target the target to set
	 */
	public void addTarget(Integer key, Database db, String statement) {
		targets.put(key, db);
		targetStatements.put(key, statement);
	}

	/**
	 * Remove a target statement using its key.
	 * @param key
	 */
	public void removeTarget(Integer key) {
		targetStatements.remove(key);
	}

	/**
	 * @param sourceStatement the sourceStatement to set
	 */
	public void setSourceStatement(String sourceStatement) {
		this.sourceStatement = sourceStatement;
	}

	/**
	 * @return the targets
	 */
	public Map<Integer, Database> getTargets() {
		return targets;
	}

	public CachedRowSet getSourceSetRow() {
		return sourceRowSet;
	}

	/**
	 * Set the sourceDb database set of rows. 
	 * @param sourceRowSet
	 */
	public void setSourceSetRow(CachedRowSet sourceRowSet) {
		this.sourceRowSet = sourceRowSet;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QUERY FROM ");
		sb.append(sourceDb);
		sb.append(" (");
		sb.append(sourceStatement);
		sb.append(") TO\n");
		for (Map.Entry<Integer, Database> target : targets.entrySet()) {
			sb.append("\t");
			sb.append(target.getValue());
			sb.append(" -> ");
			sb.append(targetStatements.get(target.getKey()));
			sb.append("\n");
		}
		return sb.toString();
	}
}
