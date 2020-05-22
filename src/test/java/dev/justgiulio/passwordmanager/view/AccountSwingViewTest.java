package dev.justgiulio.passwordmanager.view;


import static org.mockito.Mockito.verify;

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

import dev.justgiulio.passwordmanager.controller.AccountController;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.view.swing.AccountSwingView;

import org.mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
		window.label("labelErrorMessage").requireText("");
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
		
		window.textBox("textFieldSearchText").enterText("github.com");
		window.button("buttonFindByPasswordAccounts").requireEnabled();
		window.button("buttonFindBySiteAccounts").requireEnabled();
		window.button("buttonFindByUsernameAccounts").requireEnabled();

		window.textBox("textFieldSearchText").deleteText();
		window.button("buttonFindByPasswordAccounts").requireDisabled();
		window.button("buttonFindBySiteAccounts").requireDisabled();
		window.button("buttonFindByUsernameAccounts").requireDisabled();

	}
	
	@Test @GUITest
	public void testWhenAccountIsSelectedOnDisplayedAccountsTableMofidyButtonsAreEnable() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		accountSwingView.setListAccountTableData(accounts);
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").requireText("");
		window.textBox("textFieldUpdateCell").requireEnabled();
		window.button("buttonDeleteAccount").requireEnabled();
		window.button("buttonModifyUsername").requireEnabled();
		window.button("buttonModifyPassword").requireEnabled();
		
		window.table("tableDisplayedAccounts").unselectRows(0);
		window.button("buttonDeleteAccount").requireDisabled();
		window.button("buttonModifyUsername").requireDisabled();
		window.button("buttonModifyPassword").requireDisabled();
		
		
	}
	
	@Test @GUITest
	public void testWhenAccountIsSelectedOnDisplayedAccountsTableAndUserClickModifyButtonsCellUpdateComponentsAreEnable() {
		window.tabbedPane("tabbedPanel").selectTab(1);
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		accountSwingView.setListAccountTableData(accounts);
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
	
	@Test
	public void testModifyUsernameButtonsDelegateToController() {
		final String UPDATED_USERNAME = "newUsername";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountSwingView.setListAccountTableData(accounts);

		//Verify modifyUsername called when action performed on Modify Username Button
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText(UPDATED_USERNAME);
		window.button("buttonModifyUsername").click();
		verify(accountController).modifyUsername(new Account("github.com", new Credential("giulio","passgiulio")), UPDATED_USERNAME);		
	}
	
	@Test
	public void testModifyPasswordButtonDelegateToController() {
		final String UPDATED_PASSWORD = "newPassword";
		List<Account> accounts = Arrays.asList(new Account("github.com", new Credential("giulio","passgiulio")));
		window.tabbedPane("tabbedPanel").selectTab(1);
		accountSwingView.setListAccountTableData(accounts);
		//Verify modifyUsername called when action performed on Modify Password Button
		window.table("tableDisplayedAccounts").selectRows(0);
		window.textBox("textFieldUpdateCell").enterText(UPDATED_PASSWORD);
		window.button("buttonModifyPassword").click();
		verify(accountController).modifyPassword(new Account("github.com", new Credential("giulio","passgiulio")), UPDATED_PASSWORD);

	}
	
	private void resetInputTextAccountCredential() {
		window.textBox("textFieldSiteName").deleteText();
		window.textBox("textFieldUsername").deleteText();
		window.textBox("textFieldPassword").deleteText();
	}
	

}
