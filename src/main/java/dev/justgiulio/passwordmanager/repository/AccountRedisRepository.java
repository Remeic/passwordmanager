package dev.justgiulio.passwordmanager.repository;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

public class AccountRedisRepository {

	public AccountRedisRepository(Jedis jedis) {
		// TODO Auto-generated constructor stub
	}

	public List findAll() {
		return new ArrayList<>();
	}

}
