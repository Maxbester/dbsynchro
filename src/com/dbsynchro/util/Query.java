package com.dbsynchro.util;

public class Query {
	private String statement;
	private String serverName;
	
	public Query(String name, String q) {
		serverName = name;
		statement = q;
	}

	public String getStatement() {
		return statement;
	}

	public String getServerName() {
		return serverName;
	}
	
	public void setStatement(String q) {
		statement = q;
	}
	
	@Override
	public String toString() {
		return serverName + " - " + statement;
	}
}
