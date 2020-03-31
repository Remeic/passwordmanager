package dev.justgiulio.passwordmanager.repository;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.ListAssert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.fppt.jedismock.RedisServer;
import com.giuliofagioli.passwordgenerator.model.Account;
import com.giuliofagioli.passwordgenerator.model.Credential;

import redis.clients.jedis.Jedis;

public class AccountRepositoryRedistTest {
	
	private static AccountRedisRepository accountRedisRepository;
	private static Jedis jedis;
	private static RedisServer server = null;

	@BeforeClass
	public static void beforeClass()   throws IOException {
		  server = RedisServer.newRedisServer();  // bind to a random port
		  server.start();
		  jedis = new Jedis(server.getHost(), server.getBindPort());
		  accountRedisRepository = new AccountRedisRepository(jedis);
	}
	
	@After
	public void after() {
		jedis.flushAll();
	}
	
	@AfterClass
	public static void afterClass() {
	  server.stop();
	  server = null;
	}
	
	@Test
	public void testFindAllWhenNotFound() {
		ListAssert assertThat = assertThat(accountRedisRepository.findAll());
		assertThat.isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		
	}
	
}
