package dev.justgiulio.passwordmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;

import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import redis.clients.jedis.Jedis;
import java.util.Arrays;


@RunWith(GUITestRunner.class)
public class AccountSwingAppE2E extends AssertJSwingJUnitTestCase {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer redis = new GenericContainer("redis:3.0.2").withExposedPorts(6379);

	private FrameFixture window;
	private Jedis redisClient;
	private static final String ACCOUNT_FIXTURE_1_SITE = "github.com";
	private static final String ACCOUNT_FIXTURE_1_USERNAME = "remeic";
	private static final String ACCOUNT_FIXTURE_1_PASSWORD = "remepassword";

	private static final String ACCOUNT_FIXTURE_2_SITE = "gitlab.com";
	private static final String ACCOUNT_FIXTURE_2_USERNAME = "giulio";
	private static final String ACCOUNT_FIXTURE_2_PASSWORD = "giuliopassword";

	private static final String ACCOUNT_FIXTURE_3_SITE = "github.com";
	private static final String ACCOUNT_FIXTURE_3_USERNAME = "remegiulio";
	private static final String ACCOUNT_FIXTURE_3_PASSWORD = "remepassword";

	@Override
	protected void onSetUp() {
		redisClient = new Jedis(redis.getContainerIpAddress(), redis.getMappedPort(6379));
		application("dev.justgiulio.passwordmanager.app.swing.AccountSwingApp")
				.withArgs("--redis-host=" + redis.getContainerIpAddress(),
						"--redis-port=" + redis.getMappedPort(6379).toString())
				.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Password Manager".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Before
	@After
	public void cleanUpDB() {
		redisClient.flushDB();
	}

	@Override
	protected void onTearDown() {
		redisClient.close();
	}

	@Test
	@GUITest
	public void testAddAccountsAreShowedInTable() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		window.textBox("textFieldSiteName").enterText(accountToSave.getSite());
		window.textBox("textFieldUsername").enterText(accountToSave.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(accountToSave.getCredential().getPassword());
		window.button("buttonSaveAccount").click();
		assertThat(window.label("labelAccountAdded").text()).isEqualTo("Account Saved!");
		Account secondAccountToSave = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		window.textBox("textFieldSiteName").enterText(secondAccountToSave.getSite());
		window.textBox("textFieldUsername").enterText(secondAccountToSave.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(secondAccountToSave.getCredential().getPassword());
		window.button("buttonSaveAccount").click();
		assertThat(window.label("labelAccountAdded").text()).isEqualTo("Account Saved!");
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(accountToSave, secondAccountToSave);

	}

	@Test
	@GUITest
	public void testGeneratedPasswordUsedAsAccountPassword() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.radioButton("radioButtonLowStrength").check();
		window.slider("sliderPasswordLength").slideTo(8);
		window.button("buttonGeneratePassword").click();
		window.textBox("textFieldGeneratedPassword").selectAll();
		String copiedPassword = window.textBox("textFieldGeneratedPassword").target().getSelectedText();
		Account accountToSaveWithGeneratedPassword = new Account("github.com",
				new Credential("remeic", copiedPassword));
		window.textBox("textFieldSiteName").enterText(accountToSaveWithGeneratedPassword.getSite());
		window.textBox("textFieldUsername").enterText(accountToSaveWithGeneratedPassword.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(copiedPassword);
		window.button("buttonSaveAccount").click();
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(accountToSaveWithGeneratedPassword);

	}

	@Test
	@GUITest
	public void testAccountListAlreadyContainsAccountIfAccountIsPresentInDB() {
		addTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD);
		addTestAccountDirectlyToDB(ACCOUNT_FIXTURE_2_SITE, ACCOUNT_FIXTURE_2_USERNAME, ACCOUNT_FIXTURE_2_PASSWORD);
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,
				new Credential(ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,
				new Credential(ACCOUNT_FIXTURE_2_USERNAME, ACCOUNT_FIXTURE_2_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccount, secondAccount);

	}

	@Test
	@GUITest
	public void testSaveAccountShowErrorLabelIfAccountAlreadyExists() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		Account accountToSave = new Account(ACCOUNT_FIXTURE_1_SITE,
				new Credential(ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD));
		window.textBox("textFieldSiteName").enterText(accountToSave.getSite());
		window.textBox("textFieldUsername").enterText(accountToSave.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(accountToSave.getCredential().getPassword());
		window.button("buttonSaveAccount").click();
		assertThat(window.label("labelErrorMessage").text())
				.isEqualTo("Already existing: " + accountToSave);
	}

	@Test
	@GUITest
	public void testDeleteAccountShowErrorLabelIfAccountNotExistsAnymore() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		JTableCellFixture cell = window.table("tableDisplayedAccounts")
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		window.table("tableDisplayedAccounts").selectRows(cell.row());
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		window.button("buttonDeleteAccount").click();
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Can't find any account for selected site");
	}

	@Test
	@GUITest
	public void testModifyAccountUsernameShowErrorLabelIfAccountNotExistsAnymore() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		JTableCellFixture cell = window.table("tableDisplayedAccounts")
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		window.table("tableDisplayedAccounts").selectRows(cell.row());
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		window.textBox("textFieldUpdateCell").enterText("newUsername");
		window.button("buttonModifyUsername").click();
		assertThat(window.label("labelOperationResult").text())
				.isEqualTo("Can't find any account for selected site with specified username");
	}

	@Test
	@GUITest
	public void testModifyAccountPasswordShowErrorLabelIfAccountNotExistsAnymore() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		JTableCellFixture cell = window.table("tableDisplayedAccounts")
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_PASSWORD));
		window.table("tableDisplayedAccounts").selectRows(cell.row());
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		window.textBox("textFieldUpdateCell").enterText("newPassword");
		window.button("buttonModifyPassword").click();
		assertThat(window.label("labelOperationResult").text())
				.isEqualTo("Can't find any account for selected site with specified password");
	}

	@Test
	@GUITest
	public void testFindBySiteShowCorrectAccounts() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_2_PASSWORD));
		Account thirdAccount = new Account(ACCOUNT_FIXTURE_3_SITE,new Credential(ACCOUNT_FIXTURE_3_USERNAME,ACCOUNT_FIXTURE_3_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount,secondAccount,thirdAccount));
		Account firstAccountToSave = new Account(ACCOUNT_FIXTURE_1_SITE,
				new Credential(ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccountToSave = new Account(ACCOUNT_FIXTURE_3_SITE,
				new Credential(ACCOUNT_FIXTURE_3_USERNAME, ACCOUNT_FIXTURE_3_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(ACCOUNT_FIXTURE_1_SITE);
		window.button("buttonFindBySiteAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccountToSave, secondAccountToSave);
	}

	@Test
	@GUITest
	public void testFindByUsernameShowCorrectAccounts() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_2_PASSWORD));
		Account thirdAccount = new Account(ACCOUNT_FIXTURE_3_SITE,new Credential(ACCOUNT_FIXTURE_3_USERNAME,ACCOUNT_FIXTURE_3_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount,secondAccount,thirdAccount));
		Account firstAccountToSave = new Account(ACCOUNT_FIXTURE_2_SITE,
				new Credential(ACCOUNT_FIXTURE_2_USERNAME, ACCOUNT_FIXTURE_2_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(ACCOUNT_FIXTURE_2_USERNAME);
		window.button("buttonFindByUsernameAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccountToSave);
	}

	@Test
	@GUITest
	public void testFindByPasswordShowCorrectAccounts() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_2_PASSWORD));
		Account thirdAccount = new Account(ACCOUNT_FIXTURE_3_SITE,new Credential(ACCOUNT_FIXTURE_3_USERNAME,ACCOUNT_FIXTURE_3_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount,secondAccount,thirdAccount));
		Account firstAccountToSave = new Account(ACCOUNT_FIXTURE_1_SITE,
				new Credential(ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccountToSave = new Account(ACCOUNT_FIXTURE_3_SITE,
				new Credential(ACCOUNT_FIXTURE_3_USERNAME, ACCOUNT_FIXTURE_3_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(ACCOUNT_FIXTURE_1_PASSWORD);
		window.button("buttonFindByPasswordAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccountToSave, secondAccountToSave);
	}

	@Test
	@GUITest
	public void testFindByPasswordShowUpdatedValueWhenRecalledOnceAccountIsRemovedFromDB() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_2_PASSWORD));
		Account thirdAccount = new Account(ACCOUNT_FIXTURE_3_SITE,new Credential(ACCOUNT_FIXTURE_3_USERNAME,ACCOUNT_FIXTURE_3_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount,secondAccount,thirdAccount));
		Account firstAccountToSave = new Account(ACCOUNT_FIXTURE_1_SITE,
				new Credential(ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccountToSave = new Account(ACCOUNT_FIXTURE_3_SITE,
				new Credential(ACCOUNT_FIXTURE_3_USERNAME, ACCOUNT_FIXTURE_3_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(ACCOUNT_FIXTURE_3_PASSWORD);
		window.button("buttonFindByPasswordAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccountToSave, secondAccountToSave);
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		window.button("buttonFindByPasswordAccounts").click();
		accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(secondAccountToSave);

	}

	@Test
	@GUITest
	public void testFindBySiteShowUpdatedValueWhenRecalledOnceAccountIsRemovedFromDB() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_2_PASSWORD));
		Account thirdAccount = new Account(ACCOUNT_FIXTURE_3_SITE,new Credential(ACCOUNT_FIXTURE_3_USERNAME,ACCOUNT_FIXTURE_3_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount,secondAccount,thirdAccount));
		Account firstAccountToSave = new Account(ACCOUNT_FIXTURE_1_SITE,
				new Credential(ACCOUNT_FIXTURE_1_USERNAME, ACCOUNT_FIXTURE_1_PASSWORD));
		Account secondAccountToSave = new Account(ACCOUNT_FIXTURE_3_SITE,
				new Credential(ACCOUNT_FIXTURE_3_USERNAME, ACCOUNT_FIXTURE_3_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(ACCOUNT_FIXTURE_1_SITE);
		window.button("buttonFindBySiteAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(firstAccountToSave, secondAccountToSave);
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		window.button("buttonFindBySiteAccounts").click();
		accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(secondAccountToSave);

	}

	@Test
	@GUITest
	public void testFindByUsernameShowUpdatedValueWhenRecalledOnceAccountIsRemovedFromDB() {
		Account secondAccount = new Account(ACCOUNT_FIXTURE_2_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_2_PASSWORD));
		Account thirdAccount = new Account(ACCOUNT_FIXTURE_3_SITE,new Credential(ACCOUNT_FIXTURE_2_USERNAME,ACCOUNT_FIXTURE_3_PASSWORD));
		addAccountUsingUi(Arrays.asList(secondAccount,thirdAccount));
		Account firstAccountToSave  = new Account(ACCOUNT_FIXTURE_2_SITE,
				new Credential(ACCOUNT_FIXTURE_2_USERNAME, ACCOUNT_FIXTURE_2_PASSWORD));
		Account  secondAccountToSave = new Account(ACCOUNT_FIXTURE_3_SITE,
				new Credential(ACCOUNT_FIXTURE_2_USERNAME, ACCOUNT_FIXTURE_3_PASSWORD));
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").enterText(ACCOUNT_FIXTURE_2_USERNAME);
		window.button("buttonFindByUsernameAccounts").click();
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(secondAccountToSave, firstAccountToSave);
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_2_SITE, ACCOUNT_FIXTURE_2_USERNAME);
		window.button("buttonFindByUsernameAccounts").click();
		accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsExactly(secondAccountToSave);

	}

	@Test
	@GUITest
	public void testModifyAccountPasswordUsingGeneratedPasswordFromGeneratorPanel() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		JTableCellFixture cell = window.table("tableDisplayedAccounts")
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_PASSWORD));
		window.table("tableDisplayedAccounts").selectRows(cell.row());
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.button("buttonGeneratePassword").click();
		window.textBox("textFieldGeneratedPassword").selectAll();
		String copiedPassword = window.textBox("textFieldGeneratedPassword").target().getSelectedText();
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldUpdateCell").enterText(copiedPassword);
		window.button("buttonModifyPassword").click();
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Modified!");
	}

	@Test
	@GUITest
	public void testModifyAccountUsernameSuccessWithCorrectResultLabel() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		JTableCellFixture cell = window.table("tableDisplayedAccounts")
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		window.table("tableDisplayedAccounts").selectRows(cell.row());
		window.textBox("textFieldUpdateCell").enterText("newUsername");
		window.button("buttonModifyUsername").click();
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Modified!");
	}

	@Test
	@GUITest
	public void testModifyAccountPasswordSuccessWithCorrectResultLabel() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").click();
		JTableCellFixture cell = window.table("tableDisplayedAccounts")
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_PASSWORD));
		window.table("tableDisplayedAccounts").selectRows(cell.row());
		window.textBox("textFieldUpdateCell").enterText("newPassword");
		window.button("buttonModifyPassword").click();
		assertThat(window.label("labelOperationResult").text()).isEqualTo("Account Modified!");
	}


	private void addAccountUsingUi(List<Account> accounts) {
		for(Account account : accounts) {
			window.textBox("textFieldSiteName").enterText(account.getSite());
			window.textBox("textFieldUsername").enterText(account.getCredential().getUsername());
			window.textBox("textFieldPassword").enterText(account.getCredential().getPassword());
			window.button("buttonSaveAccount").click();
		}
		
	}
	
	private void addTestAccountDirectlyToDB(String site, String username, String password) {
		Map<String, String> mapToSave = new HashMap<>();
		mapToSave.put(username, password);
		redisClient.hmset(site, mapToSave);
	}

	private void removeTestAccountDirectlyToDB(String site, String username) {
		redisClient.hdel(site, username);
	}

	private List<Account> getAccountsList(String[][] tableContent) {
		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < tableContent.length; i++) {
			accounts.add(new Account(tableContent[i][0], new Credential(tableContent[i][1], tableContent[i][2])));
		}
		return accounts;
	}

}
