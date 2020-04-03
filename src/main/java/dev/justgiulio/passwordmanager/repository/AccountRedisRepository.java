package dev.justgiulio.passwordmanager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
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
			.map(key -> this.fromMapToAccounts(key,this.getMapFromKey(key)))
			.forEach(account -> accounts.addAll(account));
		return accounts;
	}

	public List<Account> findByKey(String key) {
		List<Account> accounts;
		accounts = this.fromMapToAccounts(key,this.getMapFromKey(key));
		return accounts;
	}
	
	public List<Account> findByUsername(String username) {
		return new ArrayList<Account>();
	}
	
	
	
	
	/** Private Methods */

	private List<Account> fromMapToAccounts(String key, Map<String, String> jedisSavedMap) {
		List<String> savedKeys;
		List<Account> savedAccounts;
		List<Credential> savedCredential;
		savedKeys = jedisSavedMap.keySet().stream().collect(Collectors.toList());
		savedCredential = savedKeys.stream().map(keyMap -> new Credential(keyMap,jedisSavedMap.get(keyMap)))
				.collect(Collectors.toList());
		savedAccounts = savedCredential.stream().map(credential -> new Account(key, credential))
				.collect(Collectors.toList());
		return savedAccounts;
	}
	
	

	private Map<String,String> getMapFromKey(String key){
		return this.client.hgetAll(key);
	}

	
	

}
