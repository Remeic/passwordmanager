package dev.justgiulio.passwordmanager.view.swing;



import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
<<<<<<< HEAD
=======
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
>>>>>>> Test, implement integration test for AccountSwingView

import com.github.fppt.jedismock.RedisServer;

import dev.justgiulio.passwordmanager.controller.AccountController;
import dev.justgiulio.passwordmanager.generator.Generator;
import dev.justgiulio.passwordmanager.generator.SecureRandomGenerator;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.repository.AccountRedisRepository;
import redis.clients.jedis.Jedis;

@RunWith(GUITestRunner.class)
public class AccountSwingViewIT  extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private AccountSwingView accountSwingView;
	private static Jedis jedis;
	private static RedisServer server = null;
	private static Generator passwordGenerator;
	private AccountRedisRepository accountRedisRepository;
<<<<<<< HEAD
=======
	
	@Mock
>>>>>>> Test, implement integration test for AccountSwingView
	private AccountController accountController;
	
	
	@BeforeClass
	public static void beforeClass()   throws IOException {
		  server = RedisServer.newRedisServer();  // bind to a random port
		  server.start();
		  jedis = new Jedis(server.getHost(), server.getBindPort());
	}
	
	@Before
	public void onSetUp() {
		accountRedisRepository = new AccountRedisRepository(jedis);
		passwordGenerator = new SecureRandomGenerator();
<<<<<<< HEAD
=======
		MockitoAnnotations.initMocks(this);
>>>>>>> Test, implement integration test for AccountSwingView
		
		GuiActionRunner.execute(() -> {
			accountSwingView = new AccountSwingView();
			accountController = new AccountController(accountSwingView, accountRedisRepository, passwordGenerator);
			accountSwingView.setAccountController(accountController);
			return accountSwingView;
		});
		
		window = new FrameFixture(robot(), accountSwingView);
		window.show(); // shows the frame to test
	}
	
	@After
	public void after() {
		jedis.flushAll();
	}
<<<<<<< HEAD

=======
	
	@Test @GUITest
	public void testAccountIsAdded() {
		Account accountToSave = new Account("github.com", new Credential("remeic","remepassword"));
		accountController.saveAccount(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(0);
		assertThat(window.label("labelAccountAdded").text()).isEqualTo("Account Saved!");
	}
>>>>>>> Test, implement integration test for AccountSwingView
	
	@Test @GUITest
	public void testAccountIsModifiedPassowrd() {
		Account accountToSave = new Account("github.com", new Credential("remeic","remepassword"));
		accountRedisRepository.save(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.modifyPassword(accountToSave, "newPassword");
<<<<<<< HEAD
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.findAllAccounts();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(new Account("github.com", new Credential("remeic","newPassword")));
=======
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Modified!");
>>>>>>> Test, implement integration test for AccountSwingView
	}
	
	@Test @GUITest
	public void testAccountIsModifiedUsername() {
		Account accountToSave = new Account("github.com", new Credential("remeic","remepassword"));
		accountRedisRepository.save(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.modifyUsername(accountToSave, "remegiulio");
<<<<<<< HEAD
		accountController.findAllAccounts();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(new Account("github.com", new Credential("remegiulio","remepassword")));
=======
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Modified!");
>>>>>>> Test, implement integration test for AccountSwingView
	}
	
	@Test @GUITest
	public void testAccountIsDeleted() {
		Account accountToSave = new Account("github.com", new Credential("remeic","remepassword"));
		accountRedisRepository.save(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.delete(accountToSave);
<<<<<<< HEAD
		accountController.findAllAccounts();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).isEmpty();
=======
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Deleted!");
>>>>>>> Test, implement integration test for AccountSwingView
	}
	
	@Test @GUITest
	public void testFindAllAccounts() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio","giuliopassword"));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.findAllAccounts();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount,secondAccount);
	}
	
	@Test @GUITest
	public void testFindByKey() {
		String site = "github.com";
		Account firstAccount = new Account(site, new Credential("remeic","remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account(site, new Credential("remegiulio","remepassword"));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.findAccountsByKey(site);
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount,thirdAccount);
	}
	
	@Test @GUITest
	public void testFindByUsername() {
		String username = "remeic";
		Account firstAccount = new Account("github.com", new Credential(username,"remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("gitlab.com", new Credential(username,"remepassword"));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.findAccountsByUsername(username);
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount,thirdAccount);
	}
	
	@Test @GUITest
	public void testFindByPassword() {
		String password = "remepassword";
		Account firstAccount = new Account("github.com", new Credential("remeic",password));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio","giuliopassword"));
		Account thirdAccount = new Account("gitlab.com", new Credential("remegiulio",password));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.findAccountsByPassword(password);
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount,thirdAccount);
	}
	
<<<<<<< HEAD
	@Test @GUITest
	public void testSaveAccountShowedInFindAll() {
		Account firstAccount = new Account("github.com", new Credential("remeic","remepassword"));
		accountController.saveAccount(firstAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountController.findAllAccounts();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount);
	}
	
=======
>>>>>>> Test, implement integration test for AccountSwingView
	private List<Account> getAccountsList(String[][] tableContent){
		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < tableContent.length; i++) {
			accounts.add(new Account(tableContent[i][0], new Credential(tableContent[i][1],tableContent[i][2])));
		}
		return accounts;
	}
	
<<<<<<< HEAD
	
	
=======
//	@Test @GUITest
//	public void testPasswordIsGenerated() {
//		window.tabbedPane("tabbedPanel").selectTab(0);
//		int sliderValue = window.slider("sliderPasswordLength").target().getValue();
//		accountController.generatePassword(sliderValue, "STRENGHT_PASSWORD_HIGH");
//		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Deleted!");
//	}
>>>>>>> Test, implement integration test for AccountSwingView
}
