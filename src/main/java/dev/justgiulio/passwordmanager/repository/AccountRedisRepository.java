package dev.justgiulio.passwordmanager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import redis.clients.jedis.Jedis;

public class AccountRedisRepository {

	Jedis client;

	public AccountRedisRepository(Jedis client) {
		this.client = client;
	}

	public List<Account> findAll() {
		List<String> keys;
		List<Account> accounts = new ArrayList<>();
		keys = client.keys("*").stream().collect(Collectors.toList());
		keys
			.stream()
			.map(key -> this.fromMapToAccount(key,this.client.hgetAll(key)))
			.forEach(account -> accounts.addAll(account));
		return accounts;
	}

	private List<Account> fromMapToAccounts(String key, Map<String, String> jedisSavedMap) {
		List<String> savedKeys;
		List<Account> savedAccounts;
		savedKeys = jedisSavedMap.keySet().stream().collect(Collectors.toList());
		savedAccounts = savedKeys.stream().map(keyMap -> new Account(key,new Credential(key, jedisSavedMap.get(key))))
				.collect(Collectors.toList());
		return savedAccounts;
	}

}
