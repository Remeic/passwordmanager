package dev.justgiulio.passwordmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.github.fppt.jedismock.RedisServer;

import dev.justgiulio.passwordmanager.controller.AccountController;
import dev.justgiulio.passwordmanager.generator.Generator;
import dev.justgiulio.passwordmanager.generator.SecureRandomGenerator;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.repository.AccountRedisRepository;
import redis.clients.jedis.Jedis;

@RunWith(GUITestRunner.class)
public class AccountSwingViewIT extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private AccountSwingView accountSwingView;
	private static Jedis jedis;
	private static RedisServer server = null;
	private static Generator passwordGenerator;
	private AccountRedisRepository accountRedisRepository;
	private AccountController accountController;

	@BeforeClass
	public static void beforeClass() throws IOException {
		server = RedisServer.newRedisServer(); // bind to a random port
		server.start();
		jedis = new Jedis(server.getHost(), server.getBindPort());
	}

	@Before
	public void onSetUp() {
		accountRedisRepository = new AccountRedisRepository(jedis);
		passwordGenerator = new SecureRandomGenerator();

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

	@Test
	@GUITest
	public void testAccountIsModifiedPassword() {
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		accountRedisRepository.save(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(1);
		GuiActionRunner.execute(
				() ->accountController.findAllAccounts());
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText("newPassword");
		window.button("buttonModifyPassword").click();
		assertThat(window.label("labelOperationResult").text())
		.isEqualTo("Account Modified!");
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(new Account("github.com", new Credential("remeic", "newPassword")));
		assertThat(accountRedisRepository.findAll()).containsExactly(new Account("github.com", new Credential("remeic", "newPassword")));
	}

	@Test
	@GUITest
	public void testAccountIsModifiedUsername() {
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		accountRedisRepository.save(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(1);
		GuiActionRunner.execute(
				() ->accountController.findAllAccounts());
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText("newUsername");
		window.button("buttonModifyUsername").click();
		assertThat(window.label("labelOperationResult").text())
		.isEqualTo("Account Modified!");
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(new Account("github.com", new Credential("newUsername", "remepassword")));
		assertThat(accountRedisRepository.findAll()).containsExactly(new Account("github.com", new Credential("newUsername", "remepassword")));
	}

	@Test
	@GUITest
	public void testAccountIsDeleted() {
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		accountRedisRepository.save(accountToSave);
		window.tabbedPane("tabbedPanel").selectTab(1);
		GuiActionRunner.execute(
				() ->accountController.findAllAccounts());
		window.table("tableDisplayedAccounts").selectRows(0);
		window.button("buttonDeleteAccount").click();
		assertThat(window.label("labelOperationResult").text())
		.isEqualTo("Account Deleted!");
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).isEmpty();
		assertThat(accountRedisRepository.findAll()).isEmpty();

	}
	
	

	@Test
	@GUITest
	public void testSaveAccountShowedInFindAll() {
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		window.textBox("textFieldSiteName").enterText(accountToSave.getSite());
		window.textBox("textFieldUsername").enterText(accountToSave.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(accountToSave.getCredential().getPassword());
		window.button("buttonSaveAccount").click();
		assertThat(window.label("labelAccountAdded").text()).isEqualTo("Account Saved!");
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(accountToSave);
		assertThat(accountRedisRepository.findAll()).containsExactly(accountToSave);

	}

	@Test
	@GUITest
	public void testFindAllAccounts() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		Account firstAccount = new Account("github.com", new Credential("remeic", "remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount, secondAccount);
		assertThat(accountRedisRepository.findAll()).containsExactly(firstAccount, secondAccount);

	}

	@Test
	@GUITest
	public void testFindByKey() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		String site = "github.com";
		Account firstAccount = new Account(site, new Credential("remeic", "remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		Account thirdAccount = new Account(site, new Credential("remegiulio", "remepassword"));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(site);
		window.button("buttonFindBySiteAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount, thirdAccount);
		assertThat(accountRedisRepository.findByKey(site)).containsExactly(firstAccount, thirdAccount);

	}

	@Test
	@GUITest
	public void testFindByUsername() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		String username = "remeic";
		Account firstAccount = new Account("github.com", new Credential(username, "remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		Account thirdAccount = new Account("gitlab.com", new Credential(username, "remepassword"));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		window.textBox("textFieldSearchText").enterText(username);
		window.button("buttonFindByUsernameAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount,thirdAccount);
		assertThat(accountRedisRepository.findByUsername(username)).containsExactly(firstAccount, thirdAccount);

	}
	
	@Test
	@GUITest
	public void testFindByPassword() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		String password = "remepassword";
		Account firstAccount = new Account("github.com", new Credential("remeic", password));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		Account thirdAccount = new Account("gitlab.com", new Credential("remeic", password));
		accountRedisRepository.save(firstAccount);
		accountRedisRepository.save(secondAccount);
		accountRedisRepository.save(thirdAccount);
		window.textBox("textFieldSearchText").enterText(password);
		window.button("buttonFindByPasswordAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount,thirdAccount);
		assertThat(accountRedisRepository.findByPassword(password)).containsExactly(firstAccount, thirdAccount);

	}
	
	@Test
	@GUITest
	public void testSaveAccountAlreadyPresentDipslayErrorLabel() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		accountRedisRepository.save(accountToSave);
		window.textBox("textFieldSiteName").enterText(accountToSave.getSite());
		window.textBox("textFieldUsername").enterText(accountToSave.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(accountToSave.getCredential().getPassword());
		window.button("buttonSaveAccount").click();
		assertThat(window.label("labelErrorMessage").text())
		.isEqualTo("Already existing: "+accountToSave.toString());
		assertThat(accountRedisRepository.findAll()).containsExactly(accountToSave);


	}
	

	@Test
	@GUITest
	public void testModifyUsernameAccountNotExistingShowErrorLabel() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),
				new Account("github.com", new Credential("remeic", "passremeic")));
		accountSwingView.setListAccountTableData(accounts);
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText("newUsername");
		window.button("buttonModifyUsername").click();
		assertThat(window.label("labelOperationResult").text())
		.isEqualTo("Can't find any account for selected site with specified username");
		assertThat(accountRedisRepository.findAll()).isEmpty();

		
	}
	
	@Test
	@GUITest
	public void testModifyPasswordAccountNotExistingShowErrorLabel() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),
				new Account("github.com", new Credential("remeic", "passremeic")));
		accountSwingView.setListAccountTableData(accounts);
		assertThat(accountRedisRepository.findAll()).isEmpty();
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText("newPassword");
		window.button("buttonModifyPassword").click();
		assertThat(window.label("labelOperationResult").text())
		.isEqualTo("Can't find any account for selected site with specified password");
		assertThat(accountRedisRepository.findAll()).isEmpty();
		
	}
	
	
	@Test
	@GUITest
	public void testDeleteAccountNotExistingShowErrorLabel() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),
				new Account("github.com", new Credential("remeic", "passremeic")));
		accountSwingView.setListAccountTableData(accounts);
		assertThat(accountRedisRepository.findAll()).isEmpty();
		window.table("tableDisplayedAccounts").selectRows(0);
		window.button("buttonDeleteAccount").click();
		assertThat(window.label("labelOperationResult").text())
		.isEqualTo("Can't find any account for selected site");
		
	}
	
	
	
	
	private List<Account> getAccountsList(String[][] tableContent) {
		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < tableContent.length; i++) {
			accounts.add(new Account(tableContent[i][0], new Credential(tableContent[i][1], tableContent[i][2])));
		}
		return accounts;
	}

}
