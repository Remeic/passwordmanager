package dev.justgiulio.passwordmanager.view;

import static org.junit.Assert.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import dev.justgiulio.passwordmanager.view.swing.AccountSwingView;

@RunWith(GUITestRunner.class)
public class AccountSwingViewTest extends AssertJSwingJUnitTestCase  {
	private FrameFixture window;
	private AccountSwingView accountSwingView;

	@Before
	public void onSetUp() {
		GuiActionRunner.execute(() -> {
			accountSwingView = new AccountSwingView();
			return accountSwingView;
		});
		window = new FrameFixture(robot(), accountSwingView);
		window.show(); // shows the frame to test
	}


   
	@Test @GUITest
	public void testControlsInitialStates() {

		//Verify Components on first panel of tabbedPanel
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
	}
	
	
	@Test @GUITest
	public void testWhenTextFieldForSiteAndUsernameAndPasswordAreNotEmptySaveButtonIsEnabled() {
		window.textBox("textFieldSiteName").enterText("github.com");
		window.textBox("textFieldUsername").enterText("remeic");
		window.textBox("textFieldPassword").enterText("passgiulio");
		window.button("buttonSaveAccount").requireEnabled();
	}
	

}
