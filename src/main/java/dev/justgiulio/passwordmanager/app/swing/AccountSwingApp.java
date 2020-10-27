package dev.justgiulio.passwordmanager.app.swing;
import java.awt.EventQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.LoggerFactory;

import dev.justgiulio.passwordmanager.controller.AccountController;
import dev.justgiulio.passwordmanager.generator.Generator;
import dev.justgiulio.passwordmanager.generator.SecureRandomGenerator;
import dev.justgiulio.passwordmanager.repository.AccountRedisRepository;
import dev.justgiulio.passwordmanager.view.swing.AccountSwingView;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import redis.clients.jedis.Jedis;


@Command(mixinStandardHelpOptions = true)
public class AccountSwingApp implements Callable<Void> {

	@Option(names = { "--redis-host" }, description = "Redis host address")
	private String redisHost = "localhost";

	@Option(names = { "--redis-port" }, description = "Redis host port")
	private int redisPort = 6379;


	private AccountRedisRepository accountRedisRepository;
	private AccountController accountController;
	private AccountSwingView accountSwingView;
	private Generator passwordGenerator;
	private Jedis redisClient;
	
	public static void main(String... args) {
		BasicConfigurator.configure();
		new CommandLine(new AccountSwingApp()).execute(args);
	}
	
	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				redisClient = new Jedis(redisHost,redisPort);
				accountRedisRepository = new AccountRedisRepository(redisClient);
				accountSwingView = new AccountSwingView();
				passwordGenerator = new SecureRandomGenerator();
				accountController = new AccountController(accountSwingView, accountRedisRepository, passwordGenerator);
				accountSwingView.setAccountController(accountController);
				accountSwingView.setVisible(true);
			} catch (Exception e) {
				LoggerFactory.getLogger(getClass())
					.error("An exception was thrown!", e);
			}
		});
		return null;
	}
	
}
