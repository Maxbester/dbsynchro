package org.dbsynctool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;


public class Database {
	
	private final Logger log = Logger.getLogger(Database.class.getName());

	private String name;
	private String url;
	private String login;
	private String password;
	private String driver;
	private Connection connection;

	/**
	 * @param name
	 * @param url
	 * @param login
	 * @param password
	 * @param driver
	 */
	public Database(String name, String url, String login, String password,
			String driver) {
		super();
		this.name = name;
		this.url = url;
		this.login = login;
		this.password = password;
		this.driver = driver;
	}

    public Connection connect() throws SQLException {
    	if (connection == null) {
    		boolean isTrace =  log.isTraceEnabled();
    		if(isTrace) {
    			log.trace("Trying to connect to "+url+"...");
    		}
	        try {
				Class.forName(driver).newInstance();
				connection = DriverManager.getConnection(url, login, password);
				if(isTrace) {
					log.trace("Connection established.");
				}
			} catch (ClassNotFoundException e) {
				log.error("Cannot find the driver: "+e.getMessage());
			} catch (InstantiationException e) {
				log.error("Problem with the driver: "+e.getMessage());
			} catch (IllegalAccessException e) {
				log.error("Problem with the driver: "+e.getMessage());
			}
    	}
        return connection;
    }

    public boolean isConnected() {
    	boolean result = false;
    	try {
			if (connection != null && !connection.isClosed()) {
				result = true;
			}
		} catch (SQLException e) {}
    	return result;
    }

	public void disconnect() {
		if (connection != null) { 
			try {
				connection.close();
				if (log.isTraceEnabled()) {
					log.trace("Disconnected from "+url);
				}
				connection = null;
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

    /**
     * Execute a SQL statement such as INSERT, UPDATE or DELETE (Data Manipulation Language) 
     * or an SQL statement that return nothing
     * @param statement
     * @return the row count for insert, update or delete, else 0
     * @throws SQLException 
     */
    public int simpleStatement(String statement) throws SQLException {
    	int res = 0;
    	if (isConnected() && statement != null && !statement.startsWith("SELECT") && !statement.startsWith("select")) {
	        PreparedStatement insert;
			insert = connect().prepareStatement(statement);
	        res = insert.executeUpdate();
	        if (log.isInfoEnabled()) {
	        	log.info(name+" -> "+statement);
	        }
	        insert.close();
    	}
        return res;
    }

    /**
     * Execute a SELECT statement and return a single ResultSet object.
     * @param statement
     * @return
     * @throws SQLException 
     */
    public ResultSet selectStatement(String statement) throws SQLException {
    	ResultSet rs = null;
    	if (isConnected() && statement != null && (statement.startsWith("SELECT") || statement.startsWith("select"))) {
	    	Statement stat = connect().createStatement();
	    	rs = stat.executeQuery(statement);
	        if (log.isInfoEnabled()) {
	        	log.info(name+" -> "+statement);
	        }
    	}
        return rs;
    }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" - ");
		sb.append(login);
		sb.append("@");
		sb.append(url);
		return sb.toString();
	}
}