package dev.justgiulio.passwordmanager.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.ListAssert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fppt.jedismock.RedisServer;

import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import redis.clients.jedis.Jedis;

public class AccountRepositoryRedistTest {
	
	private static AccountRepository accountRedisRepository;
	private static Jedis jedis;
	private static RedisServer server = null;

	@BeforeClass
	public static void beforeClass()   throws IOException {
		  server = RedisServer.newRedisServer();  // bind to a random port
		  server.start();
		  jedis = new Jedis(server.getHost(), server.getBindPort());
		  accountRedisRepository = new AccountRedisRepository(jedis);
	}
	
	@After
	public void after() {
		jedis.flushAll();
	}
	
	@AfterClass
	public static void afterClass() {
	  server.stop();
	  server = null;
	}
	
	/**
	 * Test for findAll method
	 */
	
	@Test
	public void testFindAllWhenNotFound() {
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findAll());
		assertThat.isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addAccountToRedisDatabase(new Account("github.com", new Credential("giulio","passgiulio")));
		addAccountToRedisDatabase(new Account("gitlab.com", new Credential("remeic","passremeic")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findAll());
		assertThat.containsExactly(new Account("github.com", new Credential("giulio","passgiulio")),new Account("gitlab.com", new Credential("remeic","passremeic")));
	}
	
	@Test
	public void testFindAllOrdered() {
		addAccountToRedisDatabase(new Account("github.com", new Credential("giulio","passgiulio")));
		addAccountToRedisDatabase(new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findAll());
		assertThat.containsExactly(new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword")),new Account("github.com", new Credential("giulio","passgiulio")));
	}
	
	@Test
	public void testFindAllWhenExistMultipleAccountForSameSite() {
		addAccountToRedisDatabase(new Account("github.com", new Credential("giulio","passgiulio")));
		addAccountToRedisDatabase(new Account("github.com", new Credential("remeic","passremeic")));
		List<Account> savedAccounts = new ArrayList<>();
		savedAccounts.add(new Account("github.com", new Credential("giulio","passgiulio")));
		savedAccounts.add(new Account("github.com", new Credential("remeic","passremeic")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findAll());
		assertThat.containsAll(savedAccounts);
	}
	
	/**
	 * Test for findByKey method
	 */
	@Test
	public void testFindByKeyWhenAccountIsNotFound() {
		String key = "github.com";
		assertThat(accountRedisRepository.findByKey(key)).isEmpty();
	}
	
	
	@Test
	public void testFindByKeyWhenAccountIsFound() {
		String key = "github.com";
		addAccountToRedisDatabase(new Account("gitlab",new Credential("remeic","passremeic")));
		addAccountToRedisDatabase(new Account(key,new Credential("giulio","passgiulio")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByKey(key));
		assertThat.containsExactly(new Account(key,new Credential("giulio","passgiulio")));
		addAccountToRedisDatabase(new Account(key,new Credential("giulio","passgiulio2")));
	}
	
	/**
	 * Test for findByUsername method
	 */
	
	@Test
	public void testFindByUsernameWhenAccountIsNotFound() {
		String username = "giulio";
		assertThat(accountRedisRepository.findByUsername(username)).isEmpty();
	}
	
	@Test
	public void testFindByUsernameWhenAccountIsFound() {
		String username = "giulio";
		addAccountToRedisDatabase(new Account("gitlab",new Credential("remeic","passremeic")));
		addAccountToRedisDatabase(new Account("github",new Credential("giulio","passgiulio")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByUsername(username));
		assertThat.containsExactly(new Account("github",new Credential("giulio","passgiulio")));
	}
	
	@Test
	public void testFindByUsernameWhenUsernameIsUsedInMultipleAccounts() {
		String username = "giulio";
		Account githubAccount = new Account("github",new Credential("giulio","passgiulio"));
		Account gitlabAccount = new Account("gitlab",new Credential("giulio","passremeic"));
		Account bitbucketAccount = new Account("bitbucket",new Credential("remegiulio","passremegiulio"));
		addAccountToRedisDatabase(githubAccount);
		addAccountToRedisDatabase(gitlabAccount);
		addAccountToRedisDatabase(bitbucketAccount);
		List<Account> savedAccounts = new ArrayList<>();
		savedAccounts.add(githubAccount);
		savedAccounts.add(gitlabAccount);
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByUsername(username));
		assertThat.containsAll(savedAccounts);
	}
	
	@Test
	public void testFindByUsernameOrdered() {
		String username = "giulio";
		addAccountToRedisDatabase(new Account("github.com", new Credential(username,"passgiulio")));
		addAccountToRedisDatabase(new Account("dontUseFacebook.privacy", new Credential(username,"giuliopassword")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByUsername(username));
		assertThat.containsExactly(new Account("dontUseFacebook.privacy", new Credential("giulio","giuliopassword")),new Account("github.com", new Credential("giulio","passgiulio")));
	}
	
	/**
	 * Test for findByPassword method
	 */
	@Test
	public void testFindByPasswordWhenAccountIsNotFound() {
		String password = "passgiulio";
		assertThat(accountRedisRepository.findByPassword(password)).isEmpty();
	}
	
	@Test
	public void testFindByPasswordWhenAccountIsFound() {
		String password = "passgiulio";
		addAccountToRedisDatabase(new Account("gitlab",new Credential("remeic","passremeic")));
		addAccountToRedisDatabase(new Account("github",new Credential("giulio","passgiulio")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByPassword(password));
		assertThat.containsExactly(new Account("github",new Credential("giulio","passgiulio")));
	}
	
	@Test
	public void testFindByPasswordWhenPasswordIsUsedInMultipleAccounts() {
		String password = "passgiulio";
		Account githubAccount = new Account("github",new Credential("giulio","passgiulio"));
		Account gitlabAccount = new Account("gitlab",new Credential("giulio","passremeic"));
		Account bitbucketAccount = new Account("bitbucket",new Credential("remegiulio","passgiulio"));
		addAccountToRedisDatabase(githubAccount);
		addAccountToRedisDatabase(gitlabAccount);
		addAccountToRedisDatabase(bitbucketAccount);
		List<Account> savedAccounts = new ArrayList<>();
		savedAccounts.add(githubAccount);
		savedAccounts.add(bitbucketAccount);
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByPassword(password));
		assertThat.containsAll(savedAccounts);
	}
	
	@Test
	public void testFindByPasswordOrdered() {
		String password = "passgiulio";
		addAccountToRedisDatabase(new Account("github.com", new Credential("remeic",password)));
		addAccountToRedisDatabase(new Account("dontUseFacebook.privacy", new Credential("giulio",password)));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findByPassword(password));
		assertThat.containsExactly(new Account("dontUseFacebook.privacy", new Credential("giulio",password)),new Account("github.com", new Credential("remeic",password)));
	}
	
	/**
	 * Test for save method
	 */
	@Test
	public void testSaveAccountWhenDatabaseIsEmpty() {
		String result = accountRedisRepository.save(new Account("github",new Credential("giulio","passgiulio")));
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findAll());
		assertThatList.containsExactly(new Account("github",new Credential("giulio","passgiulio")));
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testSaveAccountWhenDatabaseIsNotEmpty() {
		Account githubAccount = new Account("github",new Credential("giulio","passgiulio"));
		Account gitlabAccount = new Account("gitlab",new Credential("remeic","passremeic"));
		List<Account> savedAccounts = new ArrayList<>();
		savedAccounts.add(githubAccount);
		savedAccounts.add(gitlabAccount);
		addAccountToRedisDatabase(githubAccount);
		String result = accountRedisRepository.save(gitlabAccount);
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findAll());
		assertThatList.containsAll(savedAccounts);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testSaveMultipleAccountForSameSite() {
		Account githubAccount = new Account("github",new Credential("giulio","passgiulio"));
		Account gitlabAccount = new Account("github",new Credential("remeic","passremeic"));
		List<Account> savedAccounts = new ArrayList<>();
		savedAccounts.add(githubAccount);
		savedAccounts.add(gitlabAccount);
		String resultGithub = accountRedisRepository.save(githubAccount);
		String resultGitlab = accountRedisRepository.save(gitlabAccount);
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findAll());
		assertThatList.containsAll(savedAccounts);
		assertThat(resultGithub).isEqualTo("OK");
		assertThat(resultGitlab).isEqualTo("OK");
	}
	
	@Test
	public void testModifyExistingAccountUsingSaveUpdateValue() {
		Account githubAccount = new Account("github",new Credential("giulio","passgiulio"));
		Account accountToSaveAsModify = new Account("github",new Credential("giulio","passMoreSecure123"));
		List<Account> savedAccounts = new ArrayList<>();
		savedAccounts.add(githubAccount);
		accountRedisRepository.save(githubAccount);
		accountRedisRepository.save(accountToSaveAsModify);
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findByKey(githubAccount.getSite()));
		assertThatList.containsExactly(accountToSaveAsModify);
	}
	
	/**
	 * Test for delete method
	 */
	@Test
	public void testDeleteWhenDatabaseIsEmpty() {
		accountRedisRepository.delete(new Account("github",new Credential("giulio","passgiulio")));
		ListAssert<Account> assertThat = assertThat(accountRedisRepository.findAll());
		assertThat.isEmpty();
	}
	
	@Test
	public void testDeleteAccountFound() {
		Account accountToDelete = new Account("github",new Credential("giulio","passgiulio"));
		addAccountToRedisDatabase(accountToDelete);
		accountRedisRepository.delete(accountToDelete);
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findAll());
		assertThatList.isEmpty();
	}
	
	@Test
	public void testDeleteAccountForSiteWithMultipleAccount() {
		Account accountToDelete = new Account("github",new Credential("giulio","passgiulio"));
		addAccountToRedisDatabase(accountToDelete);
		addAccountToRedisDatabase(new Account("github",new Credential("remeic","passremeic")));
		accountRedisRepository.delete(accountToDelete);
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findAll());
		assertThatList.containsExactly(new Account("github",new Credential("remeic","passremeic")));
	}
	
	@Test
	public void testDeleteAccountNotFound() {
		addAccountToRedisDatabase(new Account("github",new Credential("giulio","passgiulio")));
		accountRedisRepository.delete(new Account("gitlab",new Credential("remeic","passremeic")));
		ListAssert<Account> assertThatList = assertThat(accountRedisRepository.findAll());
		assertThatList.containsExactly(new Account("github",new Credential("giulio","passgiulio")));
	}
	
	
	
	/**
	 * Utility Method for Add Account to Database
	 * @param account Target Account to save on Redis Database
	 * @return Result of save operation
	 */
	public String addAccountToRedisDatabase(Account account) {
		Map<String, String> tmpMap = new HashMap<String, String>();
		Credential tmpCredential = account.getCredential();
		tmpMap.put(tmpCredential.getUsername(), tmpCredential.getPassword());
		String result =  jedis.hmset(account.getSite(), tmpMap);
		return result;
	}
}

