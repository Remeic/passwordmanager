
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
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","giulio")));
		when(passwordRepository.findAll()).thenReturn(accounts);
		controller.findAll();
		verify(accountView).showAccounts(accounts);
	}
	
	@Test
	public void findAccountsBySite() {
		String site = "github";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","giulio")));
		when(passwordRepository.findByKey(site)).thenReturn(accounts);
		controller.findByKey(site);
		verify(accountView).showAccounts(accounts);
	}
	
	@Test
	public void findAccountsByUsername() {
		String username = "giulio";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","giulio")));
		when(passwordRepository.findByUsername(username)).thenReturn(accounts);
		controller.findByUsername(username);
		verify(accountView).showAccounts(accounts);
	}
	
}
