package dev.justgiulio.passwordmanager.controller;

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


	public void findAll() {
		accountView.showAllAccounts(accountRepository.findAll());
	}


	public void findByKey(String site) {
		accountView.showAccountBySite(accountRepository.findByKey(site));
	}

}
