package dev.justgiulio.passwordmanager.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.data.TableCell.row;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.awaitility.Awaitility.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JScrollPaneFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.justgiulio.passwordmanager.controller.AccountController;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.view.swing.AccountSwingView;

@RunWith(GUITestRunner.class)
public class AccountSwingViewTest extends AssertJSwingJUnitTestCase {
	private FrameFixture window;
	private AccountSwingView accountSwingView;
	private Robot customRobot;
	@Mock
	private AccountController accountController;

	@Before
	public void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			accountSwingView = new AccountSwingView();
			accountSwingView.setAccountController(accountController);
			return accountSwingView;
		});
		this.customRobot = robot();
		window = new FrameFixture(this.customRobot, accountSwingView);
		window.show(); // shows the frame to test
	}

	@After
	public void cleanUpWindow() {
		window.cleanUp();
	}
	
	@Test
	@GUITest
	public void testControlsInitialStates() {
		// Verify Components on first panel of tabbedPanel
		await().atMost(40, TimeUnit.SECONDS).untilAsserted(() ->{
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");
		panelPass.button(JButtonMatcher.withName("buttonGeneratePassword")).requireEnabled();
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		panelPass.radioButton("radioButtonHighStrength").requireVisible();
		panelPass.radioButton("radioButtonLowStrength").requireVisible();
		panelPass.radioButton("radioButtonMediumStrength").check(true);
		panelPass.slider("sliderPasswordLength").requireVisible();
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).requireNotEditable();
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).requireText("");
		// Verify Components on second panel of tabbedPanel
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).requireEnabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).requireDisabled();
		panelAccounts.table("tableDisplayedAccounts").requireVisible();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).requireText("");
		panelAccounts.button(JButtonMatcher.withName("buttonDeleteAccount")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).requireDisabled();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireDisabled();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireText("");
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireDisabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("");
		});
	}
	

	@Test
	@GUITest
	public void testSliderHaveMinimumValueOf8andMaximumOf32() {
		int minimumAllowedValueOfLength = 8;
		int maximumAllowedValueOfLength = 32;

		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		assertThat(panelPass.slider("sliderPasswordLength").target().getMinimum()).isEqualTo(minimumAllowedValueOfLength);
		assertThat(panelPass.slider("sliderPasswordLength").target().getMaximum()).isEqualTo(maximumAllowedValueOfLength);

	}
	
	@Test
	@GUITest
	public void testWhenTextFieldForSiteAndUsernameAndPasswordAreNotEmptySaveButtonIsEnabled() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("github.com");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("github.com");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		resetInputTextAccountCredential();

		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("remeic");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		resetInputTextAccountCredential();

		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("passgiulio");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		resetInputTextAccountCredential();


		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("github.com");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("remeic");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		resetInputTextAccountCredential();

		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("remeic");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("passgiulio");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		resetInputTextAccountCredential();

		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("github.com");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("passgiulio");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
		resetInputTextAccountCredential();

		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("github.com");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("remeic");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("passgiulio");
		window.button(JButtonMatcher.withName("buttonSaveAccount")).requireEnabled();
	}

	@Test
	@GUITest
	public void testWhenTextFieldForFindsOperationIsNotEmptyFindButtonsAreEnabled() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText("github.com");
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).requireEnabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).requireEnabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).requireEnabled();

		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).deleteText();
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).requireDisabled();

	}

	@Test
	@GUITest
	public void testWhenAccountIsSelectedOnDisplayedAccountsTableAndFieldCellUpdateIsNotEmptyMofidyButtonsAreEnable() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.selectRows(0);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireText("");
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireEnabled();
		panelAccounts.button("buttonDeleteAccount").requireEnabled();
		panelAccounts.button("buttonModifyUsername").requireDisabled();
		panelAccounts.button("buttonModifyPassword").requireDisabled();

		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText("newValue");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).requireEnabled();
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).requireEnabled();

		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).deleteText();
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).requireDisabled();
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).requireDisabled();

	}

	@Test
	@GUITest
	public void testWhenAccountIsSelectedOnDisplayedAccountsTableAndUserClickModifyButtonsCellUpdateComponentsAreEnable() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});

		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).click();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireText("");

		table.unselectRows(0);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireText("");

		table.selectRows(0);
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).click();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireEnabled();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireText("");

	}

	@Test
	@GUITest
	public void testModifyUsernameButtonsDelegateToController() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		final String UPDATED_USERNAME = "newUsername";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		// Verify modifyUsername called when action performed on Modify Username Button
		JScrollPaneFixture scrollPane = panelAccounts.scrollPane("scrollPaneAccounts");
		scrollPane.focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText(UPDATED_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).click();
		verify(accountController).modifyUsername(new Account("github.com", new Credential("giulio", "passgiulio")),
				UPDATED_USERNAME);

	}

	@Test
	@GUITest
	public void testModifyPasswordButtonDelegateToController() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		final String UPDATED_PASSWORD = "newPassword";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		// Verify modifyUsername called when action performed on Modify Password Button
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		JScrollPaneFixture scrollPane = panelAccounts.scrollPane("scrollPaneAccounts");
		scrollPane.focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		window.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText(UPDATED_PASSWORD);
		window.button(JButtonMatcher.withName("buttonModifyPassword")).click();
		verify(accountController).modifyPassword(new Account("github.com", new Credential("giulio", "passgiulio")),
				UPDATED_PASSWORD);
	}

	@Test
	@GUITest
	public void testShowAccountsDisplayCorrectAccountsIntoTable() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		
		Account firstAccount = new Account("github.com", new Credential("giulio", "passgiulio"));
		Account secondAccount = new Account("github.com", new Credential("remeic", "remegiulio"));
		List<Account> accountDisplayed = Arrays.asList(firstAccount, secondAccount);
		GuiActionRunner.execute(() -> {
			accountSwingView.showAccounts(accountDisplayed);
		});

		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		List<Account> accountList = getAccountsList(table.contents());
		assertThat(accountList).containsAll(accountDisplayed);
	}

	@Test
	@GUITest
	public void testAccountIsModifiedShowLabelWithSuccessInfo() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireDisabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("");
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsModified();
		});
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireEnabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("Account Modified!");

	}
	
	
	@Test
	@GUITest
	public void testAccountIsModifiedResetTextField() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),
				new Account("github.com", new Credential("remeic", "passremeic")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.selectRows(0);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText("modifyInput");
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsModified();

		});
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).requireText("");
	}

	@Test
	@GUITest
	public void testLabelReturnToInitialStateWhenTableSelectionChange() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),
				new Account("github.com", new Credential("remeic", "passremeic")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.selectRows(0);
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsModified();

		});
		table.unselectRows(0);
		table.selectRows(1);
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireDisabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("");
	}

	@Test
	@GUITest
	public void testAccountIsDeletedShowLabelWithSuccessInfo() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireDisabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("");
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsDeleted();
		});
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireEnabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("Account Deleted!");
	}
	

	@Test
	@GUITest
	public void testShowErrorDisplayErrorLabelWithCorrectText() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");
		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Generic Error");
		});
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireEnabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("Generic Error");
	}

	@Test
	@GUITest
	public void testShowErrorAccountDisplayErrorLabelWithCorrectText() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");
		String accountToString = "Account [site=github.com, credential=Credential [username=remeic, password=passremeic]]";
		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Generic Error: ", new Account("github.com", new Credential("remeic", "passremeic")));
		});
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireEnabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("Generic Error: " + accountToString);
	}

	@Test
	@GUITest
	public void testLabelErrorReturnToInitalStateIfAccountInputFieldsChange() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Generic Error");
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(" ");
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");

		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Generic Error");
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(" ");
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");

		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Generic Error");
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(" ");
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");
	}

	@Test
	@GUITest
	public void testAccountIsAddedShowLabelWithCorrectText() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireEnabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("Account Saved!");

	}

	@Test
	@GUITest
	public void testLabelAccountAddedReturnToInitalStateIfAccountInputFieldsChange() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(" ");
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");

		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(" ");
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");

		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(" ");
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");

	}

	@Test
	@GUITest
	public void testGenerateButtonDelegateControllerHighStrength() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.radioButton("radioButtonHighStrength").check();
		panelPass.slider("sliderPasswordLength").slideTo(15);
		panelPass.button(JButtonMatcher.withName("buttonGeneratePassword")).click();
		verify(accountController).generatePassword(15, "STRENGHT_PASSWORD_HIGH");
	}

	@Test
	@GUITest
	public void testGenerateButtonDelegateControllerMediumStrength() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.radioButton("radioButtonMediumStrength").check();
		panelPass.slider("sliderPasswordLength").slideTo(15);
		panelPass.button(JButtonMatcher.withName("buttonGeneratePassword")).click();
		verify(accountController).generatePassword(15, "STRENGHT_PASSWORD_MEDIUM");
		
	}

	@Test
	@GUITest
	public void testGenerateButtonDelegateControllerLowStrength() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.radioButton("radioButtonLowStrength").check();
		panelPass.slider("sliderPasswordLength").slideTo(15);
		panelPass.button(JButtonMatcher.withName("buttonGeneratePassword")).click();
		verify(accountController).generatePassword(15, "STRENGHT_PASSWORD_LOW");
	}

	@Test
	@GUITest
	public void testPasswordIsGeneratedDisplayCorrectTextInsideInputField() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).requireNotEditable();
		GuiActionRunner.execute(() -> {
			accountSwingView.passwordIsGenereated("generatedPassword");
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).requireText("generatedPassword");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldGeneratedPassword")).requireNotEditable();
	}
	
	@Test
	@GUITest
	public void testPasswordIsGeneratedResetInputField() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText("siteName");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText("username");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText("password");
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).requireText("");
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).requireText("");
		
	}

	@Test
	@GUITest
	public void testButtonSaveAccountDelegateControllerSaveAccount() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(accountToSave.getSite());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(accountToSave.getCredential().getUsername());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(accountToSave.getCredential().getPassword());
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).click();
		verify(accountController).saveAccount(accountToSave);
	}
	
	@Test
	@GUITest
	public void testFindAccountsByUsernameDelegateControllerFindByUsername() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText("textToSearch");
		panelAccounts.button(JButtonMatcher.withName("buttonFindByUsernameAccounts")).click();
		verify(accountController).findAccountsByUsername("textToSearch");
	}
	
	@Test
	@GUITest
	public void testFindAccountsByPasswordDelegateControllerFindByPassword() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText("textToSearch");
		panelAccounts.button(JButtonMatcher.withName("buttonFindByPasswordAccounts")).click();
		verify(accountController).findAccountsByPassword("textToSearch");
	}
	
	
	@Test
	@GUITest
	public void testFindAccountsBySiteDelegateControllerFindBySite() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();		
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldSearchText")).enterText("textToSearch");
		panelAccounts.button(JButtonMatcher.withName("buttonFindBySiteAccounts")).click();
		verify(accountController).findAccountsByKey("textToSearch");
	}
	
	@Test
	@GUITest
	public void testFindAllAccountDelegateControllerFindAll() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();		
		panelAccounts.button(JButtonMatcher.withName("buttonFindAllAccounts")).click();
		verify(accountController).findAllAccounts();
	}
	
	@Test
	@GUITest
	public void testDeleteAccountDelegateControllerDeleteAccount() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();		
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),
				new Account("github.com", new Credential("remeic", "passremeic")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.selectRows(0);
		panelAccounts.button(JButtonMatcher.withName("buttonDeleteAccount")).click();
		verify(accountController).delete(new Account("github.com", new Credential("giulio", "passgiulio")));
	}

	@Test
	@GUITest
	public void testModifyOperationErrorShowLabel() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();		
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireDisabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("");
		GuiActionRunner.execute(() -> {
			accountSwingView.showAccountRelatedError("Generic Error");
		});
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireEnabled();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("Generic Error");
	}
	
	
	@Test
	@GUITest
	public void testModifyUsernameButtonReturnDisableAfterModifyUsernameIsCalledSuccessfull() {
		final String UPDATED_USERNAME = "newUsername";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")));
		
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();	
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText(UPDATED_USERNAME);
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).requireEnabled();
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsModified();
		});
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("Account Modified!");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyUsername")).requireDisabled();


	}
	
	@Test
	@GUITest
	public void testModifyPasswordButtonReturnDisableAfterModifyPasswordIsCalledSuccessfull() {
		final String UPDATED_PASSWORD = "newPassword";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")));
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();	
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		
		panelAccounts.textBox(JTextComponentMatcher.withName("textFieldUpdateCell")).enterText(UPDATED_PASSWORD);
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).requireEnabled();
		accountSwingView.accountIsModified();
		panelAccounts.label(JLabelMatcher.withName("labelOperationResult")).requireText("Account Modified!");
		panelAccounts.button(JButtonMatcher.withName("buttonModifyPassword")).requireDisabled();


	}
	
	@Test
	@GUITest
	public void testCellOnThirdColumnForPasswordIsEditable() {
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),new Account("gitlab.com", new Credential("remeic", "remepassword")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();	
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		TableCell cell = row(0).column(2);
		table.requireEditable(cell);
		cell = row(1).column(2);
		table.requireEditable(cell);
	
	}
	
	@Test
	@GUITest
	public void testCellOnSecondColumnForUsernameIsEditable() {
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),new Account("gitlab.com", new Credential("remeic", "remepassword")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();	
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		TableCell cell = row(0).column(1);
		table.requireEditable(cell);
		cell = row(1).column(1);
		table.requireEditable(cell);
	
	}
	
	@Test
	@GUITest
	public void testCellOnFirstAndSecondColumnAreNotEditable() {
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),new Account("gitlab.com", new Credential("remeic", "remepassword")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();	
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		TableCell cell = row(0).column(0);
		table.requireNotEditable(cell);
		cell = row(1).column(0);
		table.requireNotEditable(cell);
        verifyNoMoreInteractions(ignoreStubs(accountController));
	}
	
	@Test
	@GUITest
	public void testEditAnEditableCellNotCallAnyMethodOnController() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio", "passgiulio")),new Account("gitlab.com", new Credential("remeic", "remepassword")));
		GuiActionRunner.execute(() -> {
			accountSwingView.setListAccountTableData(accounts);
		});
		JPanelFixture panelAccounts = window.panel("panelDisplayedAccounts");
		panelAccounts.focus();	
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		JTableFixture table = window.table("tableDisplayedAccounts");
		table.focus();
		table.selectRows(0);
		panelAccounts.scrollPane("scrollPaneAccounts").focus();
		TableCell cell = row(0).column(1);
		table.enterValue(cell, "newUsername");
		verifyNoMoreInteractions(ignoreStubs(accountController));
		cell = row(0).column(2);
		table.enterValue(cell, "newPassword");
        verifyNoMoreInteractions(ignoreStubs(accountController));
	}
	
	
	@Test
	@GUITest
	public void testSaveAccountButtonIsDisableAfterAccountIsAdded() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		panelPass.textBox(JTextComponentMatcher.withName("textFieldSiteName")).enterText(accountToSave.getSite());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldUsername")).enterText(accountToSave.getCredential().getUsername());
		panelPass.textBox(JTextComponentMatcher.withName("textFieldPassword")).enterText(accountToSave.getCredential().getPassword());
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).requireEnabled();
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.button(JButtonMatcher.withName("buttonSaveAccount")).requireDisabled();
	}
	
	@Test
	@GUITest
	public void testSaveAccountLabelNotVisibleWhenGenericErrorIsShowed() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireEnabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("Account Saved!");
		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Generic Error");
		});
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");
	}
	
	@Test
	@GUITest
	public void testSaveAccountLabelNotVisibleWhenSpecificErrorIsShowed() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		Account account = new Account("github.com", new Credential("remeic", "remepassword"));
		GuiActionRunner.execute(() -> {
			accountSwingView.accountIsAdded();
		});
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireEnabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("Account Saved!");
		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Specific Error Related to", account);
		});
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelAccountAdded")).requireText("");
	}
	
	@Test
	@GUITest
	public void testErrorLabelNotVisibileWhenAccountSavedWithSuccess() {
		JTabbedPaneFixture tabbedPane = window.tabbedPane("tabbedPanel");
		tabbedPane.requireVisible();
		tabbedPane.selectTab(0);
		JPanelFixture panelPass = window.panel("panelGeneratePassword");
		panelPass.focus();
		Account account = new Account("github.com", new Credential("remeic", "remepassword"));
		GuiActionRunner.execute(() -> {
			accountSwingView.showError("Specific Error Related to", account);
		});
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireEnabled();
		assertThat(window.label("labelErrorMessage").target().getText()).isNotEqualTo("");
		accountSwingView.accountIsAdded();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireDisabled();
		panelPass.label(JLabelMatcher.withName("labelErrorMessage")).requireText("");


	}
	
	
	
	
	private void resetInputTextAccountCredential() {
		window.textBox("textFieldSiteName").deleteText();
		window.textBox("textFieldUsername").deleteText();
		window.textBox("textFieldPassword").deleteText();
	}

	private List<Account> getAccountsList(String[][] tableContent) {
		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < tableContent.length; i++) {
			accounts.add(new Account(tableContent[i][0], new Credential(tableContent[i][1], tableContent[i][2])));
		}
		return accounts;
	}

}
