import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Reads the statements in "statements.sql"
 * 
 * @author Maxime Buisson
 *
 */
public class SqlReader {
	
	private static final File sqlFile = new File("statements.sql");
	private List<String> statements;
	private Map<Integer, String> sourceStatements;
	private Map<Integer, String> targetStatements;
	
	public SqlReader() throws FileNotFoundException, MalformedInputException {
		System.out.println("\n-- Reading of the statements");
		Scanner scanner = new Scanner(sqlFile);
		scanner.useDelimiter(Pattern.compile("[.*][;][.*]"));
		statements = new ArrayList<String>();
		targetStatements = new HashMap<Integer, String>();
		sourceStatements = new HashMap<Integer, String>();
		
		while (scanner.hasNext()) {
			statements.add(scanner.next().replace(";", "").replaceAll("[\n]", "").replaceAll("[\n]", " "));
		}
		
		// for the target
		int i = 0;
		// for the source
		int j = 0;
		for (String s : statements) {
			String target = s;
			int numberOfSourceStatements = s.replaceAll("\\[", "").length();

			// no source statement
			if (numberOfSourceStatements == 0) {
				targetStatements.put(i, target);
				i++;
				continue;
			}
			
			if (numberOfSourceStatements != s.replaceAll("\\]", "").length()) {
				System.out.println("ERROR: You have an error in "+sqlFile.getName()+". The number of opening square brackets does not meet the " +
						"number of closing ones int the statement #"+(i+1)+".");
				throw new MalformedInputException(2);
			}

			int open, close;
			String source = target;
			while (source.contains("[")) {
				open = source.indexOf("[", 0);
				close = source.indexOf("]", 0);
				sourceStatements.put(j, source.substring(open+1, close));
				target = target.replace(sourceStatements.get(j), new Integer(j).toString());
				source = source.substring(close+1);
				j++;
			}
			targetStatements.put(i, target);
			i++;
		}
	}
	
	/**
	 * Unrefined statements like they appear in the statement file.
	 * @return
	 */
	public List<String> getStatements() {
		return this.statements;
	}


	public Map<Integer, String> getSourceStatements() {
		return sourceStatements;
	}

	
	public Map<Integer, String> getTargetStatements() {
		return targetStatements;
	}

}
