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
	
	private final File sqlFile;
	private List<String> statements;
	private Map<Integer, String> sourceStatements;
	private List<String> targetStatements;
	
	public SqlReader(String file) throws FileNotFoundException, MalformedInputException {
		System.out.println("\n-- Reading of the statements ("+file+")");
		
		sqlFile = new File(file);
		Scanner scanner = new Scanner(sqlFile);
		scanner.useDelimiter(Pattern.compile("[;]"));
		statements = new ArrayList<String>();
		targetStatements = new ArrayList<String>();
		sourceStatements = new HashMap<Integer, String>();
		
		while (scanner.hasNext()) {
			String s = scanner.next().replaceAll("(\t)+", " ").replaceAll("(\n)+", " ");
			// clear the spaces
			s = s.replaceAll("[ ]{2,}", " ");
			s = s.replaceAll("^[\\s]", "");
			// fix a bug (an empty string is not a statement)
			if (s.isEmpty() || s.length() < 2) {
				continue;
			} else {
				statements.add(s);
			}
		}
		
		// for the source
		int j = 0;
		for (String s : statements) {
			String target = s;
			int numberOfSourceStatements = s.replaceAll("\\[", "").length();

			// no source statement
			if (numberOfSourceStatements == 0) {
				targetStatements.add(target);
				continue;
			}
			
			if (numberOfSourceStatements != s.replaceAll("\\]", "").length()) {
				System.out.println("ERROR: You have an error in "+sqlFile.getName()+". The number of opening square brackets does not meet the " +
						"number of closing ones int the statement #"+(targetStatements.size()+1)+".");
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
			targetStatements.add(target);
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

	
	public List<String> getTargetStatements() {
		return targetStatements;
	}

}
