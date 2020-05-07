package dev.justgiulio.passwordmanager.controller;

import java.util.List;
import java.util.stream.Collectors;

import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.repository.AccountRepository;
import dev.justgiulio.passwordmanager.view.AccountView;

public class AccountController {

	private AccountView accountView;
	private AccountRepository accountRepository;
	
	
	public AccountController(AccountView accountView, AccountRepository accountRepository) {
		super();
		this.accountView = accountView;
		this.accountRepository = accountRepository;
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
		boolean alreadyExistingAccount = accountRepository.findByKey(accountToSave.getSite())
				.stream()
				.anyMatch(x -> x.getCredential().getUsername().equals(accountToSave.getCredential().getUsername()));
		if(alreadyExistingAccount) {
			accountView.showError("Already existing credential for the same site with the same username", accountToSave);
			return;
		}
		accountRepository.save(accountToSave);
		accountView.accountIsAdded();
	}


}
