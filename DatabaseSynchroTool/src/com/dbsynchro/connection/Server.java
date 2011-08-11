package com.dbsynchro.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.dbsynchro.Controler;

/**
 * Defines the information about a database server and method to modify its content. 
 * 
 * @author Maxime Buisson
 *
 */
public class Server {
	
	private String name;
	private String url;
	private String login;
	private String password;
	private String driver;
	private Connection connection;
	private ResultSet rs;
	private final Logger log = Logger.getLogger(Server.class.getName());

	public Server(String name, String url, String login, String password, String driver) {
		this.name = name;
		this.url = url;
		this.login = login;
		this.password = password;
		this.driver = driver;
		log.addHandler(Controler.getHandler());
		log.setLevel(Controler.getLevel());
	}

    public Connection connect() throws InstantiationException, IllegalAccessException, SQLException {
    	if (connection == null) {
	        log.finer("Connection to: "+this.url);
	        log.finer("Trying to connect...");
	        try {
				Class.forName(driver).newInstance();
			} catch (ClassNotFoundException e) {
				log.severe("ERROR: Can not find the driver: "+e.getMessage());
				e.printStackTrace();
			}
			try {
				connection = DriverManager.getConnection(url, login, password);
				log.finer("Connection established.");
			}
	        catch (SQLException e){
	        	log.severe( "ERROR: Driver loaded, but cannot connect to db: "+e.getMessage());
	        	log.warning("/!\\ Connection NOT established. /!\\");
	        	throw new SQLException();
	         }
    	}
        return connection;
    }
    
    /**
     * Execute a SQL statement such as INSERT, UPDATE or DELETE (Data Manipulation Language) 
     * or an SQL statement that return nothing
     * @param statement
     * @return the row count for insert, update or delete, else 0
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public int simpleStatement (String statement) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    	if (statement == null)
    		return 0;
        PreparedStatement insert = connect().prepareStatement(statement);
        int res = insert.executeUpdate();
        insert.close();
        return res;
    }
    
    /**
     * Executes the given SQL statement, which returns a single ResultSet object.
     * @param statement
     * @return
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public ResultSet selectStatement (String statement) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Statement stat = connect().createStatement();
        rs = stat.executeQuery(statement);
        return rs;
    }

	public String getName() {
		return name;
	}


	public String getUrl() {
		return url;
	}


	public String getLogin() {
		return login;
	}

	
	public String getDriver() {
		return driver;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	
	@Override
	public String toString() {
		String res;
		res = "Name:\t"+name+"\n";
		res += "\t\t\tUrl:\t"+url+"\n";
		res += "\t\t\tLogin:\t"+login+"\n";
		res += "\t\t\tPwd:\t*************\n";
		res += "\t\t\tDriver:\t"+driver;
		return res;
	}

	
	public void disconnect() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (rs != null) {
			rs.close();
		}
		connect().close();
		log.finer("Disconnected from: "+this.url);
		connection = null;
	}
}
