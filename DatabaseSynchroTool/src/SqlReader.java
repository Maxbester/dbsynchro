import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Reads the statements in "statements.sql".
 * 
 * @author Maxime Buisson
 *
 */
public class SqlReader {
	
	private final File sqlFile;
	private List<String> sQueries;
	private Multimap<Integer, String> dQueries;
	
	/**
	 * Analyzes the query file. The results are stocked in sQueries and dQueries.
	 * @param file The path and name of the file that contains the queries.
	 * @throws FileNotFoundException
	 * @throws MalformedInputException
	 */
	public SqlReader(String file) throws FileNotFoundException, MalformedInputException {
		System.out.println("\n-- Reading of the statements ("+file+")");

		sqlFile = new File(file);
		Scanner scanner = new Scanner(sqlFile);
		scanner.useDelimiter(Pattern.compile("[\\n]"));
		sQueries = new ArrayList<String>();
		dQueries = ArrayListMultimap.create();
		
		int i=0;
		while (scanner.hasNext()) {
			String s = scanner.next();
//			System.out.println("SCANNER: "+s);
			if (s.startsWith("s:")) {
				s = s.replaceAll("s:", "");
				sQueries.add(s);
				i = sQueries.size();
			} else if (s.startsWith("d:")) {
				s = s.replaceAll("d:", "");
				dQueries.put(i-1, s);
			} else if (s.startsWith("--")) {
				// commentaire
			} else {
				System.out.println("ERROR: the query file ("+file+") is malformed.");
				throw new MalformedInputException(3);
			}
		}
		System.out.println("SQL file reading OK");
	}
	

	/**
	 * Returns the queries that will be executed on the distant database.
	 * @return
	 */
	public Multimap<Integer, String> getdQueries() {
		return dQueries;
	}

	/**
	 * Returns the queries that will be executed on the source database.
	 * @return
	 */
	public List<String> getsQueries() {
		return sQueries;
	}
}
