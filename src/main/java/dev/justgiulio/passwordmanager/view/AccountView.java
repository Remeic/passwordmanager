package dev.justgiulio.passwordmanager.view;
import dev.justgiulio.passwordmanager.model.Account;
import java.util.List;

public interface AccountView {
	void showAllAccounts(List<Account> accounts);
}
