package dev.justgiulio.passwordmanager.view;
import dev.justgiulio.passwordmanager.model.Account;
import java.util.List;

public interface AccountView {
	void showAccounts(List<Account> accounts);

	void accountIsAdded();

	void showError(String string);

	void showError(String string, Account accountToSave);

	void accountIsModified();

}
