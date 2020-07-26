package dev.justgiulio.passwordmanager.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.GenericContainer;

import dev.justgiulio.passwordmanager.generator.Generator;
import dev.justgiulio.passwordmanager.generator.SecureRandomGenerator;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.repository.AccountRedisRepository;
import dev.justgiulio.passwordmanager.view.AccountView;
import redis.clients.jedis.Jedis;

/**
 * Tests integration of the controller with a Redis repository 
 * 
 * Communicates with a Redis server on localhost 
 * 
 * 
 * @author Giulio Fagioli
 *
 */
public class AccountControllerIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer redis = new GenericContainer("redis:3.0.2").withExposedPorts(6379);
	
	public  Jedis jedis;
	public AccountRedisRepository accountRedisRepository;
	public AccountController accountController;
	public static Generator passwordGenerator;

	@Mock
	public AccountView accountView;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		jedis = new Jedis(redis.getContainerIpAddress(), redis.getFirstMappedPort());
		accountRedisRepository = new AccountRedisRepository(jedis);
		passwordGenerator = new SecureRandomGenerator();
		accountController = new AccountController(accountView, accountRedisRepository, passwordGenerator);
		jedis.flushAll();
		jedis.flushDB();
	}
	
	@After
	public void cleanUp() {		
		jedis.flushAll();
		jedis.flushDB();
	}
	

	@Test
	public void testFindAllAccounts() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		Account secondAccount = new Account("github.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword"));

		List<Account> accounts =  Arrays.asList(thirdAccount,secondAccount,firstAccount);
		
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(thirdAccount);
		accountRedisRepository.save(secondAccount);

		accountController.findAllAccounts();
		verify(accountView)
			.showAccounts(accounts);
					
	}
	
	@Test
	public void testFindAccountsBySite() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		Account secondAccount = new Account("github.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword"));

		List<Account> accountListGithub = Arrays.asList(secondAccount,firstAccount);
		
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(thirdAccount);
		accountRedisRepository.save(secondAccount);
		
	
		accountController.findAccountsByKey("github.com");
		verify(accountView)
			.showAccounts(accountListGithub);
					
	}
	
	@Test
	public void testFindAccountsByUsername() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		Account secondAccount = new Account("github.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword"));
				
		List<Account> accountList = Arrays.asList(thirdAccount,secondAccount);
		
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		
		
		accountController.findAccountsByUsername("giulio");
		verify(accountView)
			.showAccounts(accountList);
					
	}
	
	@Test
	public void testFindAccountsByPassword() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		Account secondAccount = new Account("github.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword"));
				
		List<Account> accountList = Arrays.asList(thirdAccount,secondAccount);

		
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		
		accountController.findAccountsByPassword("giuliopassword");
		verify(accountView)
			.showAccounts(accountList);
					
	}
	
	@Test
	public void testSaveAccount() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		accountController.saveAccount(firstAccount);
		verify(accountView)
			.accountIsAdded();
					
	}
	
	
	@Test
	public void testDeleteAccount() {
		Account accountToDelete = new Account("github.com", new Credential("remeic","remepassword"));
		accountRedisRepository.save(accountToDelete);
		accountController.delete(accountToDelete);
		verify(accountView)
			.accountIsDeleted();
					
	}
	
	
	@Test
	public void testModifyAccountUsername() {
		Account accountToModify = new Account("github.com", new Credential("remeic","remepassword"));
		accountRedisRepository.save(accountToModify);
		accountController.modifyUsername(accountToModify,"giulio");
		verify(accountView)
			.accountIsModified();
					
	}
	
	
	@Test
	public void testModifyAccountPassword() {
		Account accountToModify = new Account("github.com", new Credential("remeic","remepassword"));
		accountRedisRepository.save(accountToModify);
		accountController.modifyPassword(accountToModify,"newPassword4564");
		verify(accountView)
			.accountIsModified();
					
	}
	
	
	@Test
	public void testGenerateMediumPassword() {
		accountController.generatePassword(16, "STRENGHT_PASSWORD_MEDIUM");		
		verify(accountView).passwordIsGenereated(anyString());
  
					
	}
	
	
}
