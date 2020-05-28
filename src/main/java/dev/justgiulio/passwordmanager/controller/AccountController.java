package dev.justgiulio.passwordmanager.controller;

import dev.justgiulio.passwordmanager.generator.Generator;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.repository.AccountRepository;
import dev.justgiulio.passwordmanager.view.AccountView;

public class AccountController {

	private AccountView accountView;
	private AccountRepository accountRepository;
	private Generator passwordGenerator;
	static final String STRENGHT_PASSWORD_HIGH = "STRENGHT_PASSWORD_HIGH";
	static final String STRENGHT_PASSWORD_MEDIUM = "STRENGHT_PASSWORD_MEDIUM";
	static final String STRENGHT_PASSWORD_LOW = "STRENGHT_PASSWORD_LOW";

	public AccountController(AccountView accountView, AccountRepository accountRepository, Generator passwordGenerator) {
		super();
		this.accountView = accountView;
		this.accountRepository = accountRepository;
		this.passwordGenerator = passwordGenerator;
	}


	public void findAllAccounts() {
		accountView.showAccounts(accountRepository.findAll());
	}


	public void findAccountsByKey(String site) {
		accountView.showAccounts(accountRepository.findByKey(site));
	}
	
	public void findAccountsByUsername(String username) {
		accountView.showAccounts(accountRepository.findByUsername(username));
	}


	public void findAccountsByPassword(String password) {
		accountView.showAccounts(accountRepository.findByPassword(password));
		
	}


	public void saveAccount(Account accountToSave) {
		if(checkIfAccountAlreadyExists(accountToSave)) {
			accountView.showError("Already existing credential for the same site with the same username", accountToSave);
		}else {
			accountRepository.save(accountToSave);
			accountView.accountIsAdded();
		}
		
	}


	public void modifyPassword(Account alreadySavedAccount, String newCredentialPassword) {
		if(checkIfAccountAlreadyExists(alreadySavedAccount)) {
			accountRepository.save(new Account(alreadySavedAccount.getSite(), new Credential(alreadySavedAccount.getCredential().getUsername(), newCredentialPassword)));
			accountView.accountIsModified();
		}else {
			accountView.showError("Can't find any account for selected site with specified username");
		}
		
	}
	

	public void modifyUsername(Account alreadySavedAccount, String newCredentialUsername) {
		if(checkIfAccountAlreadyExists(alreadySavedAccount)) {
			accountRepository.delete(alreadySavedAccount);
			accountRepository.save(new Account(alreadySavedAccount.getSite(), new Credential(newCredentialUsername,alreadySavedAccount.getCredential().getPassword())));
			accountView.accountIsModified();
		}else {
			accountView.showError("Can't find any account for selected site with specified username");
		}
		
	}
	
	public void delete(Account accountToDelete) {
		if(checkIfAccountAlreadyExists(accountToDelete)) {
			accountRepository.delete(accountToDelete);
			accountView.accountIsDeleted();
		}else {
			accountView.showError("Can't find any account for selected site");
		}

	}
	
	public void generatePassword(int length,String strenght) {
		int strenghtLevel = 2;
		String generatedPassword;
		
		if(strenght.equals(STRENGHT_PASSWORD_LOW)) {
			strenghtLevel = 0;
		}
		else if(strenght.equals(STRENGHT_PASSWORD_MEDIUM)){
			strenghtLevel = 1;
		}
		
		generatedPassword = this.passwordGenerator.generate(4, strenghtLevel);
		accountView.passwordIsGenereated(generatedPassword);
		
	}
	
	
	
	private boolean checkIfAccountAlreadyExists(Account account) {
		return accountRepository.findByKey(account.getSite())
				.stream()
				.anyMatch(x -> x.getCredential().getUsername().equals(account.getCredential().getUsername()));
	}


}
