package dev.justgiulio.passwordmanager.model;

public class Account {

	String site;
	Credential credential;
	
	public Account(String site, Credential credential) {
		this.site = site;
		this.credential = credential;
	}

	public Credential getCredential() {
		return this.credential;
	}

	public String getSite() {
		return this.site;
	}

}
