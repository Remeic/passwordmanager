package dev.justgiulio.passwordmanager.view.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import dev.justgiulio.passwordmanager.model.Account;

public class DisplayedAccountsTableModel  extends AbstractTableModel {
	private String[] columnNames = {"Site","Username","Password"};
	private transient List<Account> accounts;
	
	public DisplayedAccountsTableModel() {
		accounts = new ArrayList<>();
	}

	
	@Override
	public int getColumnCount() {
        return columnNames.length;
    }

	@Override
    public int getRowCount() {
        return accounts.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return false;
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object cellItem = null;
	      if (columnIndex == 0) {
	    	  cellItem = accounts.get(rowIndex).getSite();
	      }
	      if (columnIndex == 1) {
	    	  cellItem = accounts.get(rowIndex).getCredential().getUsername();
	      }
	      if (columnIndex == 2) {
	    	  cellItem = accounts.get(rowIndex).getCredential().getPassword();
	      }
	      return cellItem;
	}
	
	public void setModel(List<Account> accountsUpdated) {
		this.accounts = accountsUpdated;
		this.fireTableDataChanged();
	}
	

}
