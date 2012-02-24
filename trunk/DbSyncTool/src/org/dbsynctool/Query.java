package org.dbsynctool;

import java.util.HashMap;
import java.util.Map;

public class Query {
	
	private Database source;
	private Map<Integer, Database> targets;

	private String sourceStatement;
	private Map<Integer, String> targetStatements;

	public Query() {
		targets = new HashMap<Integer, Database>();
		targetStatements = new HashMap<Integer, String>();
	}

	/**
	 * @return the source
	 */
	public Database getSourceDatabase() {
		return source;
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
	 * @param source the source to set
	 */
	public void setSource(Database source) {
		this.source = source;
	}

	/**
	 * @param target the target to set
	 */
	public void addTarget(Integer key, Database db, String statement) {
		targets.put(key, db);
		targetStatements.put(key, statement);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QUERY FROM ");
		sb.append(source);
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
