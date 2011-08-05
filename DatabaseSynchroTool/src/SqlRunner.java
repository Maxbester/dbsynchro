import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SqlRunner {
	
	public SqlRunner(Map<Integer, String> sourceStatements,	List<String> targetStatements, Server sourceServer, Server targetServer) throws ClassNotFoundException, SQLException {

		// connection to the local server
		sourceServer.connect();

		for (String target : targetStatements) {
			// we clear the memory for each statetemts
			List<ResultSet> res = new ArrayList<ResultSet>();
			
			// we run the local statement needed for the current distant statement
			for (int i = 0 ; i < countInternStat(target) ; i++) {
				res.add(sourceServer.selectStatement("select "+sourceStatements.get(i)+" from "+getTable(sourceStatements.get(i))));
			}

			for (int i = 0 ; i < res.size() ; i++) {
				target = target.replace("\\["+i+"\\]", res.get(i).getString(getColumn(sourceStatements.get(i))));
			}
			
			// connection to the distant server
			targetServer.connect();
			targetServer.simpleStatement(target);
			targetServer.disconnect();
			
		}
		sourceServer.disconnect();
	}
	
	private int countInternStat(String s) {
		return s.replaceAll("\\[", "").length();
	}
	
	/**
	 * Get the table name of string that looks like: "product.id". Product corresponds to the name of the table and id to the name of the column.
	 * @param s
	 * @return
	 */
	private String getTable(String s) {
		if (!s.contains("."))
			return s;
		return s.substring(0,s.indexOf(".")-1);
	}
	
	/**
	 * Get the column name of string that looks like: "product.id". Product corresponds to the name of the table and id to the name of the column.
	 * @param s
	 * @return
	 */
	private String getColumn(String s) {
		if (!s.contains("."))
			return s;
		return s.substring(s.indexOf(".")+1);
	}
}
