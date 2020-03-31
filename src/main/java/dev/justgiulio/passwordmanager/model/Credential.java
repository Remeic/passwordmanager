package dev.justgiulio.passwordmanager.model;

public class Credential {

	String username;
	String password;
	
	public Credential(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

	

}
