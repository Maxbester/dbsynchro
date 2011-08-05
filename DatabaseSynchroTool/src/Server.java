import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	
	
	public Server(String name, String url, String login, String password, String driver) {
		this.name = name;
		this.url = url;
		this.login = login;
		this.password = password;
		this.driver = driver;
	}

    protected Connection connect() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        System.out.println("Connection to: "+this.login+"@"+this.url);
        Class.forName(driver);
        conn = DriverManager.getConnection("jdbc:mysql:"+url, login, password);
        return conn;
    }
    
    /**
     * Execute a SQL statement such as INSERT, UPDATE or DELETE (Data Manipulation Language) 
     * or an SQL statement that return nothing
     * @param statement
     * @return the row count for insert, update or delete, else 0
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int simpleStatement (String statement) throws SQLException, ClassNotFoundException {
    	if (statement == null)
    		return 0;
        Connection conn = this.connect();
        PreparedStatement insert = conn.prepareStatement(statement);
        int res = insert.executeUpdate();
        insert.close();
        conn.close();
        return res;
    }
    
    /**
     * Executes the given SQL statement, which returns a single ResultSet object.
     * @param statement
     * @return
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public List<ResultSet> selectStatement (String statement) throws ClassNotFoundException, SQLException {
        List<ResultSet> res = new ArrayList<ResultSet>();
        Connection conn = connect();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(statement);
        while (rs.next()) {
            res.add(rs);
        }
        rs.close();
        conn.close();
        return res;
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
		res += "Url:\t"+url+"\n";
		res += "Login:\t"+login+"\n";
		res += "Pwd:\t*************\n";
		res += "Driver:\t"+driver;
		return res;
	}

	
}
