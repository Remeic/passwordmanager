package dev.justgiulio.passwordmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.fixture.JTableFixture;
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


@RunWith(GUITestRunner.class)
public class AccountSwingAppE2E extends AssertJSwingJUnitTestCase {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer redis = new GenericContainer("redis:6.0.6").withExposedPorts(6379);

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
	private Robot customRobot;
	@Override
	protected void onSetUp() {
		redisClient = new Jedis(redis.getContainerIpAddress(), redis.getMappedPort(6379));
		application("dev.justgiulio.passwordmanager.app.swing.AccountSwingApp")
				.withArgs("--redis-host=" + redis.getContainerIpAddress(),
						"--redis-port=" + redis.getMappedPort(6379).toString())
				.start();
		customRobot = robot();
		customRobot.settings().timeoutToBeVisible(10000);
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Password Manager".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(customRobot);

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
	
	@After
	public void cleanUpWindow() {
		window.cleanUp();
	}

	@Test
	@GUITest
	public void testAddAccountsAreShowedInTable() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(accountToSave.getSite());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(accountToSave.getCredential().getUsername());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(accountToSave.getCredential().getPassword());
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).click();
		assertThat(panelPass.label(JLabelMatcher.withName("labelAccountAdded")).text()).isEqualTo("Account Saved!");
		Account secondAccountToSave = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(secondAccountToSave.getSite());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(secondAccountToSave.getCredential().getUsername());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(secondAccountToSave.getCredential().getPassword());
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).click();
		assertThat(panelPass.label(JLabelMatcher.withName("labelAccountAdded")).text()).isEqualTo("Account Saved!");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		List<Account> accountList = getAccountsList(table.contents());
		assertThat(accountList).containsExactly(accountToSave, secondAccountToSave);

	}

	@Test
	@GUITest
	public void testGeneratedPasswordUsedAsAccountPassword() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.radioButton("radioButtonLowStrength").check();
		panelPass.slider("sliderPasswordLength").slideTo(8);
		panelPass.button(JButtonMatcher.withName("buttonGeneratePassword")).click();
		panelPass.textBox("textFieldGeneratedPassword").selectAll();
		String copiedPassword = window.textBox("textFieldGeneratedPassword").target().getSelectedText();
		Account accountToSaveWithGeneratedPassword = new Account("github.com",
				new Credential("remeic", copiedPassword));
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(accountToSaveWithGeneratedPassword.getSite());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(accountToSaveWithGeneratedPassword.getCredential().getUsername());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(copiedPassword);
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).click();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		List<Account> accountList = getAccountsList(table.contents());
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(accountToSave.getSite());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(accountToSave.getCredential().getUsername());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(accountToSave.getCredential().getPassword());
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).click();
		assertThat(panelPass.label(JLabelMatcher.withName("labelErrorMessage")).text())
				.isEqualTo("Already existing: " + accountToSave);
	}

	@Test
	@GUITest
	public void testDeleteAccountShowErrorLabelIfAccountNotExistsAnymore() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		table.selectRows(cell.row());
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonDeleteAccount")).click();
		assertThat(panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).text()).isEqualTo("Can't find any account for selected site");
	}

	@Test
	@GUITest
	public void testModifyAccountUsernameShowErrorLabelIfAccountNotExistsAnymore() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		table.selectRows(cell.row());
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText("newUsername");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).click();
		assertThat(panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).text())
				.isEqualTo("Can't find any account for selected site with specified username");
	}

	@Test
	@GUITest
	public void testModifyAccountPasswordShowErrorLabelIfAccountNotExistsAnymore() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		table.selectRows(cell.row());
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText("newPassword");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).click();
		assertThat(panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).text())
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText(ACCOUNT_FIXTURE_1_SITE);
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		List<Account> accountList = getAccountsList(table.contents());
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText(ACCOUNT_FIXTURE_2_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		List<Account> accountList = getAccountsList(table.contents());
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText(ACCOUNT_FIXTURE_1_PASSWORD);
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).click();
		List<Account> accountList = getAccountsList(table.contents());
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText(ACCOUNT_FIXTURE_3_PASSWORD);
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		List<Account> accountList = getAccountsList(table.contents());
		assertThat(accountList).containsExactly(firstAccountToSave, secondAccountToSave);
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).click();
		accountList = getAccountsList(table.contents());
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText(ACCOUNT_FIXTURE_1_SITE);
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		List<Account> accountList = getAccountsList(table.contents());
		assertThat(accountList).containsExactly(firstAccountToSave, secondAccountToSave);
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_1_SITE, ACCOUNT_FIXTURE_1_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).click();
		accountList = getAccountsList(table.contents());
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
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText(ACCOUNT_FIXTURE_2_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		List<Account> accountList = getAccountsList(table.contents());
		assertThat(accountList).containsExactly(secondAccountToSave, firstAccountToSave);
		removeTestAccountDirectlyToDB(ACCOUNT_FIXTURE_2_SITE, ACCOUNT_FIXTURE_2_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).click();
		accountList = getAccountsList(table.contents());
		assertThat(accountList).containsExactly(secondAccountToSave);

	}

	@Test
	@GUITest
	public void testModifyAccountPasswordUsingGeneratedPasswordFromGeneratorPanel() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_PASSWORD));
		table.selectRows(cell.row());
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.button(JButtonMatcher.withName("buttonGeneratePassword")).click();
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).selectAll();
		String copiedPassword = panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).target().getSelectedText();
		tabbedPane.selectTab(1);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText(copiedPassword);
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).click();
		assertThat(panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).text()).isEqualTo("Account Modified!");
	}

	@Test
	@GUITest
	public void testModifyAccountUsernameSuccessWithCorrectResultLabel() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		table.selectRows(cell.row());
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText("newUsername");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).click();
		assertThat(panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).text()).isEqualTo("Account Modified!");
	}

	@Test
	@GUITest
	public void testModifyAccountPasswordSuccessWithCorrectResultLabel() {
		Account firstAccount = new Account(ACCOUNT_FIXTURE_1_SITE,new Credential(ACCOUNT_FIXTURE_1_USERNAME,ACCOUNT_FIXTURE_1_PASSWORD));
		addAccountUsingUi(Arrays.asList(firstAccount));		
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_PASSWORD));
		table.selectRows(cell.row());
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText("newPassword");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).click();
		assertThat(panelAccounts.label("labelOperationResult").text()).isEqualTo("Account Modified!");
	}
	
	@Test
	@GUITest
	public void testUsingCellToModifyValueOfAccountDoNotAffect() {
		Account firstAccount = new Account("github.com", new Credential("remeic", "remepassword"));
		Account secondAccount = new Account("gitlab.com", new Credential("giulio", "giuliopassword"));
		List<Account> accountList = Arrays.asList(firstAccount,secondAccount);
		addAccountUsingUi(accountList);
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		JTableFixture table = panelAccounts.table("tableDisplayedAccounts");
		table.focus();
		JTableCellFixture cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_PASSWORD));
		cell.enterValue("newPassword");
		cell = table
				.cell(Pattern.compile(ACCOUNT_FIXTURE_1_USERNAME));
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).click();
		cell.enterValue("newUsername");
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		List<Account>  recoveredAccount = getAccountsList(table.contents());
		assertThat(recoveredAccount).containsExactlyElementsOf(accountList);
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
