
package dev.justgiulio.passwordmanager.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.repository.AccountRepository;
import dev.justgiulio.passwordmanager.view.AccountView;

public class AccountControllerTest {

	@Mock
	private AccountRepository passwordRepository;
	
	@Mock
	private AccountView accountView;
	
	@InjectMocks
	AccountController controller;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAllAccountTest() {
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		when(passwordRepository.findAll()).thenReturn(accounts);
		controller.findAllAccounts();
		verify(accountView).showAccounts(accounts);
	}
	
	@Test
	public void findAccountsBySiteTest() {
		String site = "github";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		when(passwordRepository.findByKey(site)).thenReturn(accounts);
		controller.findAccountsByKey(site);
		verify(accountView).showAccounts(accounts);
	}
	
	@Test
	public void findAccountsByUsernameTest() {
		String username = "giulio";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		when(passwordRepository.findByUsername(username)).thenReturn(accounts);
		controller.findAccountsByUsername(username);
		verify(accountView).showAccounts(accounts);
	}
	
	@Test
	public void findAccountsByPasswordTest() {
		String password = "passgiulio";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		when(passwordRepository.findByPassword(password)).thenReturn(accounts);
		controller.findAccountsByPassword(password);
		verify(accountView).showAccounts(accounts);
	}
	
	@Test
	public void saveAccountTest() {
		Account accountToSave = new Account("github.com", new Credential("giulio","passgiulio"));
		InOrder inOrder = inOrder(passwordRepository,accountView);
		controller.saveAccount(accountToSave);
		inOrder.verify(passwordRepository).save(accountToSave);
		inOrder.verify(accountView).accountIsAdded();
	}
	
	
}
