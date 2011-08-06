import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Multimap;

/**
 * 
 * @author Maxime Buisson
 *
 */
public class SqlRunner {

	/**
	 * Runs the input queries. The results are stocked in specific fields in the Server objects.
	 * @param source
	 * @param distant
	 * @param sourceServer
	 * @param targetServer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SqlRunner(List<String> source, Multimap<Integer, String> distant, Server sourceServer, Server targetServer) throws ClassNotFoundException, SQLException {
		System.out.println("\n-- Run SQL statements");

		ResultSet sourceStatement;
		sourceServer.connect();
		targetServer.connect();

		for (int i = 0 ; i < source.size() ; i++) {
			sourceStatement = sourceServer.selectStatement(source.get(i));
			System.out.println("[S] QUERY = "+source.get(i));
			while (sourceStatement.next()) {
				for (String distantQuery : distant.get(i)) {
					int open = 0;
					int close = 0;
					String temp = distantQuery;
					while (temp.contains("[")) {
						open = temp.indexOf("[");
						close = temp.indexOf("]");
						int column = Integer.parseInt(temp.substring(open+1, close));
						distantQuery = distantQuery.replace(temp.substring(open, close+1), sourceStatement.getString(column));
						temp = temp.substring(close+1);
					}
					System.out.println("[D] QUERY = "+distantQuery);
					targetServer.simpleStatement(distantQuery);
				}
			}
		}
		targetServer.disconnect();
		sourceServer.disconnect();
	}
}
