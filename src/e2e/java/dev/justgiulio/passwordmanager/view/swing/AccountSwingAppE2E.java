package dev.justgiulio.passwordmanager.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
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
import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

@RunWith(GUITestRunner.class)
public class AccountSwingAppE2E extends AssertJSwingJUnitTestCase {

	
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer redis =
		new GenericContainer("redis:3.0.2") 
			.withExposedPorts(6379);
	
	private FrameFixture window;
	private Jedis redisClient;
	
	
	
	@Override
	protected void onSetUp() {
		redisClient = new Jedis(redis.getContainerIpAddress(),redis.getMappedPort(6379));
		application("dev.justgiulio.passwordmanager.app.swing.AccountSwingApp")
		.withArgs(
			"--redis-host=" + redis.getContainerIpAddress(),
			"--redis-port=" + redis.getMappedPort(6379).toString()
		)
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
		Account accountToSave = new Account("github.com", new Credential("remeic", "remepassword"));
		window.textBox("textFieldSiteName").enterText(accountToSave.getSite());
		window.textBox("textFieldUsername").enterText(accountToSave.getCredential().getUsername());
		window.textBox("textFieldPassword").enterText(accountToSave.getCredential().getPassword());
		window.button("buttonSaveAccount").click();
		assertThat(window.label("labelAccountAdded").text()).isEqualTo("Account Saved!");
		assertThat(window.textBox("textFieldSiteName").text()).isEqualTo("");
	}

}
