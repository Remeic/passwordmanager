package dev.justgiulio.passwordmanager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.GenericContainer;

import dev.justgiulio.passwordmanager.generator.Generator;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.repository.AccountRedisRepository;
import dev.justgiulio.passwordmanager.view.AccountView;
import redis.clients.jedis.Jedis;

public class AccountControllerIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer redis = new GenericContainer("redis:3.0.2").withExposedPorts(6379);

	public Jedis jedis;
	public AccountRedisRepository accountRedisRepository;
	public AccountController accountController;
	public Generator passwordGenerator;

	@Mock
	public AccountView accountView;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		jedis = new Jedis(redis.getContainerIpAddress(), redis.getFirstMappedPort());
		accountRedisRepository = new AccountRedisRepository(jedis);
		accountController = new AccountController(accountView, accountRedisRepository, passwordGenerator);
		jedis.flushAll();
	}

	@AfterClass
	public static void afterClass() {
		redis.stop();
	}

	@Test
	public void testFindAllAccounts() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		Account secondAccount = new Account("github.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword"));

		Comparator<Account> byUsername =
				(Account o1, Account o2)->o1.getCredential().getUsername().compareTo(o2.getCredential().getUsername());
		List<Account> accountList = Arrays.asList(thirdAccount);
		List<Account> accountListGithub = Arrays.asList(firstAccount,secondAccount);
		accountListGithub.sort(byUsername);
		
		List<Account> accounts = new ArrayList<>();
		accounts.addAll(accountList);
		accounts.addAll(accountListGithub);

		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);

		accountController.findAllAccounts();
		verify(accountView)
			.showAccounts(accounts);
					
	}
	
	
}
