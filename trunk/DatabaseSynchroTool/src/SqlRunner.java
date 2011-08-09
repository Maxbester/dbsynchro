import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

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
	 * @param source
	 * @param distant
	 * @param sourceServer
	 * @param targetServer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public SqlRunner(List<String> source, Multimap<Integer, String> distant, Server sourceServer, Server targetServer) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		log.addHandler(Controler.getHandler());
		log.setLevel(Controler.getLevel());
		log.info(" -- Run SQL statements");
		
		ResultSet sourceStatement;
		sourceServer.connect();
		targetServer.connect();

		for (int i = 0 ; i < source.size() ; i++) {
			sourceStatement = sourceServer.selectStatement(source.get(i));
			log.fine("[source] : "+source.get(i));
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
					log.fine("[distant] : "+distantQuery);
					targetServer.simpleStatement(distantQuery);
				}
			}
		}
		targetServer.disconnect();
		sourceServer.disconnect();
	}
}
