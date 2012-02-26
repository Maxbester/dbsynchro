package org.dbsynctool;


public class Smtp {

	private String address;
	private String port;
	private String login;
	private String password;
	
	/**
	 * @param address
	 * @param port
	 */
	public Smtp(String address, String port) {
		super();
		this.address = address;
		this.port = port;
	}

	/**
	 * @param address
	 * @param port
	 * @param login
	 * @param password
	 */
	public Smtp(String address, String port, String login, String password) {
		super();
		this.address = address;
		this.port = port;
		this.login = login;
		this.password = password;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
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
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasAuth() {
		return login != null && password != null && !login.equals("");
	}

	@Override
	public String toString() {
		return address+":"+port;
	}
}