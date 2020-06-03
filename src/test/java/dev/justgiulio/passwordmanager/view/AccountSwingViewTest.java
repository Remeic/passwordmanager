package dev.justgiulio.passwordmanager.view;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
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
public class AccountSwingViewTest extends AssertJSwingJUnitTestCase  {
	private FrameFixture window;
	private AccountSwingView accountSwingView;
	
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
		window = new FrameFixture(robot(), accountSwingView);
		window.show(); // shows the frame to test
	}


   
	@Test @GUITest
	public void testControlsInitialStates() {

		//Verify Components on first panel of tabbedPanel
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.label("labelErrorMessage").requireDisabled();
		window.label("labelErrorMessage").requireText("");
		window.label("labelAccountAdded").requireDisabled();
		window.label("labelAccountAdded").requireText("");
		window.button("buttonGeneratePassword").requireEnabled();
		window.button("buttonSaveAccount").requireDisabled();
		window.tabbedPane("tabbedPanel").requireVisible();
		window.radioButton("radioButtonHighStrength");
		window.radioButton("radioButtonLowStrength");
		window.radioButton("radioButtonMediumStrength").check(true);
		window.slider("sliderPasswordLength");
		window.textBox("textFieldGeneratedPassword").requireText("");
		window.textBox("textFieldGeneratedPassword").requireNotEditable();
		window.textBox("textFieldPassword").requireText("");
		window.textBox("textFieldSiteName").requireText("");
		window.textBox("textFieldUsername").requireText("");
		//Verify Components on second panel of tabbedPanel
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.button("buttonFindAllAccounts").requireEnabled();
		window.button("buttonFindByPasswordAccounts").requireDisabled();
		window.button("buttonFindBySiteAccounts").requireDisabled();
		window.button("buttonFindByUsernameAccounts").requireDisabled();
		window.table("tableDisplayedAccounts");
		window.textBox("textFieldSearchText").requireText("");
		window.button("buttonDeleteAccount").requireDisabled();
		window.button("buttonModifyUsername").requireDisabled();
		window.button("buttonModifyPassword").requireDisabled();
		window.textBox("textFieldUpdateCell").requireDisabled();
		window.textBox("textFieldUpdateCell").requireText("");
		window.label("labelOperationResult").requireDisabled();
		window.label("labelOperationResult").requireText("");

	}
	
	
	@Test @GUITest
	public void testWhenTextFieldForSiteAndUsernameAndPasswordAreNotEmptySaveButtonIsEnabled() {
		window.textBox("textFieldSiteName").enterText("github.com");
		window.textBox("textFieldUsername").enterText("");
		window.textBox("textFieldPassword").enterText("");
		window.button("buttonSaveAccount").requireDisabled();
		resetInputTextAccountCredential();
		
		window.textBox("textFieldSiteName").enterText("");
		window.textBox("textFieldUsername").enterText("remeic");
		window.textBox("textFieldPassword").enterText("");
		window.button("buttonSaveAccount").requireDisabled();
		resetInputTextAccountCredential();
		
		window.textBox("textFieldSiteName").enterText("");
		window.textBox("textFieldUsername").enterText("");
		window.textBox("textFieldPassword").enterText("passgiulio");
		window.button("buttonSaveAccount").requireDisabled();
		resetInputTextAccountCredential();
		
		window.textBox("textFieldSiteName").enterText("github.com");
		window.textBox("textFieldUsername").enterText("remeic");
		window.textBox("textFieldPassword").enterText("");
		window.button("buttonSaveAccount").requireDisabled();
		resetInputTextAccountCredential();
		
		window.textBox("textFieldSiteName").enterText("");
		window.textBox("textFieldUsername").enterText("remeic");
		window.textBox("textFieldPassword").enterText("passgiulio");
		window.button("buttonSaveAccount").requireDisabled();
		resetInputTextAccountCredential();
		
		window.textBox("textFieldSiteName").enterText("github.com");
		window.textBox("textFieldUsername").enterText("");
		window.textBox("textFieldPassword").enterText("passgiulio");
		window.button("buttonSaveAccount").requireDisabled();
		resetInputTextAccountCredential();
		
		window.textBox("textFieldSiteName").enterText("github.com");
		window.textBox("textFieldUsername").enterText("remeic");
		window.textBox("textFieldPassword").enterText("passgiulio");
		window.button("buttonSaveAccount").requireEnabled();
	}
	
	@Test @GUITest
	public void testWhenTextFieldForFindsOperationIsNotEmptyFindButtonsAreEnabled() {
		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.textBox("textFieldSearchText").focus();
		window.textBox("textFieldSearchText").enterText("github.com");
		window.button("buttonFindByPasswordAccounts").requireEnabled();
		window.button("buttonFindBySiteAccounts").requireEnabled();
		window.button("buttonFindByUsernameAccounts").requireEnabled();

		window.textBox("textFieldSearchText").focus();
		window.textBox("textFieldSearchText").deleteText();
		window.button("buttonFindByPasswordAccounts").requireDisabled();
		window.button("buttonFindBySiteAccounts").requireDisabled();
		window.button("buttonFindByUsernameAccounts").requireDisabled();

	}
	
	@Test @GUITest
	public void testWhenAccountIsSelectedOnDisplayedAccountsTableAndFieldCellUpdateIsNotEmptyMofidyButtonsAreEnable() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		accountSwingView.setListAccountTableData(accounts);
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").requireText("");
		window.textBox("textFieldUpdateCell").requireEnabled();
		window.button("buttonDeleteAccount").requireEnabled();
		window.button("buttonModifyUsername").requireDisabled();
		window.button("buttonModifyPassword").requireDisabled();
		
		
		window.textBox("textFieldUpdateCell").enterText("newValue");
		window.button("buttonModifyUsername").requireEnabled();
		window.button("buttonModifyPassword").requireEnabled();
		
		window.textBox("textFieldUpdateCell").deleteText();
		window.button("buttonModifyUsername").requireDisabled();
		window.button("buttonModifyPassword").requireDisabled();
		
	}
	
	@Test @GUITest
	public void testWhenAccountIsSelectedOnDisplayedAccountsTableAndUserClickModifyButtonsCellUpdateComponentsAreEnable() {
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		accountSwingView.setListAccountTableData(accounts);
		
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.scrollPane("scrollPaneAccounts").focus();
		window.table("tableDisplayedAccounts").focus();
		window.table("tableDisplayedAccounts").selectRows(0);
		window.button("buttonModifyUsername").click();
		window.textBox("textFieldUpdateCell").requireText("");
		
		window.table("tableDisplayedAccounts").unselectRows(0);
		window.textBox("textFieldUpdateCell").requireText("");
		
		window.table("tableDisplayedAccounts").selectRows(0);
		window.button("buttonModifyPassword").click();
		window.textBox("textFieldUpdateCell").requireEnabled();
		window.textBox("textFieldUpdateCell").requireText("");
		
	}
	
	@Test @GUITest
	public void testModifyUsernameButtonsDelegateToController() {
		final String UPDATED_USERNAME = "newUsername";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		accountSwingView.setListAccountTableData(accounts);

		//Verify modifyUsername called when action performed on Modify Username Button
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.scrollPane("scrollPaneAccounts").focus();
		window.table("tableDisplayedAccounts").focus();
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText(UPDATED_USERNAME);
		window.button("buttonModifyUsername").click();
		verify(accountController).modifyUsername(new Account("github.com", new Credential("giulio","passgiulio")), UPDATED_USERNAME);
		
	}
	
	@Test @GUITest
	public void testModifyPasswordButtonDelegateToController() {
		final String UPDATED_PASSWORD = "newPassword";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		accountSwingView.setListAccountTableData(accounts);
		
		//Verify modifyUsername called when action performed on Modify Password Button
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.scrollPane("scrollPaneAccounts").focus();
		window.table("tableDisplayedAccounts").focus();
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText(UPDATED_PASSWORD);
		window.button("buttonModifyPassword").click();
		verify(accountController).modifyPassword(new Account("github.com", new Credential("giulio","passgiulio")), UPDATED_PASSWORD);
	}
	
	@Test @GUITest
	public void testShowAccountsDisplayCorrectAccountsIntoTable() {
		Account firstAccount = new Account("github.com", new Credential("giulio","passgiulio"));
		Account secondAccount = new Account("github.com", new Credential("remeic","remegiulio"));
		List<Account> accountDisplayed = Arrays.asList(firstAccount,secondAccount);
		accountSwingView.showAccounts(accountDisplayed);
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accountList = getAccountsList(window.table("tableDisplayedAccounts").contents());
		assertThat(accountList).containsAll(accountDisplayed);
	}
	
	@Test @GUITest
	public void testAccountIsModifiedShowLabelWithSuccessInfo() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.label("labelOperationResult").requireDisabled();
		window.label("labelOperationResult").requireText("");
		accountSwingView.accountIsModified();
		window.label("labelOperationResult").requireEnabled();
		window.label("labelOperationResult").requireText("Account Modified!");
		
	}
	
	@Test @GUITest
	public void testLabelReturnToInitialStateWhenTableSelectionChange() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")),new Account("github.com", new Credential("remeic","passremeic")));
		accountSwingView.setListAccountTableData(accounts);
		window.table("tableDisplayedAccounts").selectRows(0);
		accountSwingView.accountIsModified();
		window.table("tableDisplayedAccounts").unselectRows(0);
		window.table("tableDisplayedAccounts").selectRows(1);
		window.label("labelOperationResult").requireDisabled();
		window.label("labelOperationResult").requireText("");
		
	}
	
	@Test @GUITest
	public void testAccountIsDeletedShowLabelWithSuccessInfo() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		window.label("labelOperationResult").requireDisabled();
		window.label("labelOperationResult").requireText("");
		accountSwingView.accountIsDeleted();
		window.label("labelOperationResult").requireEnabled();
		window.label("labelOperationResult").requireText("Account Deleted!");
		
	}
	
	@Test @GUITest
	public void testShowErrorDisplayErrorLabelWithCorrectText() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.label("labelErrorMessage").requireDisabled();
		window.label("labelErrorMessage").requireText("");
		
		accountSwingView.showError("Generic Error");
		window.label("labelErrorMessage").requireEnabled();
		window.label("labelErrorMessage").requireText("Generic Error");
	}
	
	@Test @GUITest
	public void testShowErrorAccountDisplayErrorLabelWithCorrectText() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.label("labelErrorMessage").requireDisabled();
		window.label("labelErrorMessage").requireText("");
		String accountToString = "Account [site=github.com, credential=Credential [username=remeic, password=passremeic]]";
		
		accountSwingView.showError("Generic Error", new Account("github.com", new Credential("remeic","passremeic")));
		window.label("labelErrorMessage").requireEnabled();
		window.label("labelErrorMessage").requireText("Generic Error: "+accountToString);
	}
	
	@Test @GUITest
	public void testLabelErrorReturnToInitalStateIfAccountInputFieldsChange() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		accountSwingView.showError("Generic Error");
		window.textBox("textFieldSiteName").enterText(" ");
		window.label("labelErrorMessage").requireDisabled();
		window.label("labelErrorMessage").requireText("");
		
		accountSwingView.showError("Generic Error");
		window.textBox("textFieldUsername").enterText(" ");
		window.label("labelErrorMessage").requireDisabled();
		window.label("labelErrorMessage").requireText("");
		
		accountSwingView.showError("Generic Error");
		window.textBox("textFieldPassword").enterText(" ");
		window.label("labelErrorMessage").requireDisabled();
		window.label("labelErrorMessage").requireText("");
	}
	
	
	@Test @GUITest
	public void testAccountIsAddedShowLabelWithCorrectText() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.label("labelAccountAdded").requireDisabled();
		window.label("labelAccountAdded").requireText("");
		
		accountSwingView.accountIsAdded();
		window.label("labelAccountAdded").requireEnabled();
		window.label("labelAccountAdded").requireText("Account Saved!");
		
	}
	
	@Test @GUITest
	public void testLabelAccountAddedReturnToInitalStateIfAccountInputFieldsChange() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		accountSwingView.accountIsAdded();
		window.textBox("textFieldSiteName").enterText(" ");
		window.label("labelAccountAdded").requireDisabled();
		window.label("labelAccountAdded").requireText("");
		
		accountSwingView.accountIsAdded();
		window.textBox("textFieldUsername").enterText(" ");
		window.label("labelAccountAdded").requireDisabled();
		window.label("labelAccountAdded").requireText("");
		
		accountSwingView.accountIsAdded();
		window.textBox("textFieldPassword").enterText(" ");
		window.label("labelAccountAdded").requireDisabled();
		window.label("labelAccountAdded").requireText("");
	}
	
	
	@Test @GUITest
	public void testGenerateButtonDelegateControllerHighStrength() {
		window.tabbedPane("tabbedPanel").selectTab(0);
		window.radioButton("radioButtonHighStrength").check();
		window.slider("sliderPasswordLength").slideTo(15);
		window.button("buttonGeneratePassword").click();
		verify(accountController).generatePassword(15,"STRENGHT_PASSWORD_HIGH");
	}
	
	@Test @GUITest
	public void testGenerateButtonDelegateControllerMediumStrength() {
		window.radioButton("radioButtonMediumStrength").check();
		window.slider("sliderPasswordLength").slideTo(15);
		window.button("buttonGeneratePassword").click();		
		verify(accountController).generatePassword(15,"STRENGHT_PASSWORD_MEDIUM");
	}
	
	@Test @GUITest
	public void testGenerateButtonDelegateControllerLowStrength() {
		window.radioButton("radioButtonLowStrength").check();
		window.slider("sliderPasswordLength").slideTo(15);
		window.button("buttonGeneratePassword").click();		
		verify(accountController).generatePassword(15,"STRENGHT_PASSWORD_LOW");
	}
	
	@Test  @GUITest
	public void testPasswordIsGeneratedDisplayCorrectTextInsideInputField() {
		window.textBox("textFieldGeneratedPassword").requireText("");
		window.textBox("textFieldGeneratedPassword").requireNotEditable();
		accountSwingView.passwordIsGenereated("generatedPassword");
		window.textBox("textFieldGeneratedPassword").requireText("generatedPassword");
		window.textBox("textFieldGeneratedPassword").requireNotEditable();
	}
	
	

	private void resetInputTextAccountCredential() {
		window.textBox("textFieldSiteName").deleteText();
		window.textBox("textFieldUsername").deleteText();
		window.textBox("textFieldPassword").deleteText();
	}
	
	
	
	

	private List<Account> getAccountsList(String[][] tableContent){
		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < tableContent.length; i++) {
			accounts.add(new Account(tableContent[i][0], new Credential(tableContent[i][1],tableContent[i][2])));
		}
		return accounts;
	}
	

}
