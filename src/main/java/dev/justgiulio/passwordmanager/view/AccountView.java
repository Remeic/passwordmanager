package dev.justgiulio.passwordmanager.view;
import java.util.List;

import dev.justgiulio.passwordmanager.model.Account;

public interface AccountView {
	void showAccounts(List<Account> accounts);

	void accountIsAdded();

	void showError(String string);

	void showError(String string, Account accountToSave);

	void accountIsModified();

	void accountIsDeleted();
	
	void passwordIsGenereated(String generatedPassword);
	
	void showAccountRelatedError(String string);

}
