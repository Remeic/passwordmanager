package dev.justgiulio.passwordmanager.repository;

import java.util.List;

import dev.justgiulio.passwordmanager.model.Account;

public interface AccountRepository {

	List<Account> findAll();

	List<Account> findByKey(String key);

	List<Account> findByUsername(String username);

	List<Account> findByPassword(String password);

	String save(Account accountToSave);

	void delete(Account account);

}