package org.dbsynctool;

import java.util.Set;

/**
 * 
 * This class is mapped with the configuration set in an XML file by the user.
 * 
 * @author Maxime Buisson
 *
 */
public class Config {

	/**
	 * The databases to be queried.
	 */
	private Set<Database> databases;
	/**
	 * Email information.
	 */
	private Email email;

	/**
	 * Construct a Config object.
	 * @param databases
	 * @param email
	 */
	public Config(Set<Database> databases, Email email) {
		this.databases = databases;
		this.email = email;
	}

	/**
	 * Return a set of available databases.
	 * @return
	 */
	public Set<Database> getDatabases() {
		return databases;
	}

	/**
	 * Return email configuration information.
	 * @return
	 */
	public Email getEmail() {
		return email;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(email);
		sb.append("\n");
		for (Database db : databases) {
			sb.append(db);
			sb.append("\n");
		}
		return sb.toString();
	}
}