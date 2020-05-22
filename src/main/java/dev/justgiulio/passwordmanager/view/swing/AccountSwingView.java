package dev.justgiulio.passwordmanager.view.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.view.AccountView;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;
import javax.swing.JLabel;

public class AccountSwingView extends javax.swing.JFrame implements AccountView{

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountSwingView() {
        initComponents();
    }

                 
    private void initComponents() {
    	modelDisplayedAccounts = new DisplayedAccountsTableModel();
        tableDisplayedAccounts = new javax.swing.JTable(modelDisplayedAccounts);
        tableDisplayedAccounts.setName("tableDisplayedAccounts");
    	scrollPaneAccountsTable = new javax.swing.JScrollPane(tableDisplayedAccounts);
        scrollPaneAccountsTable.setViewportView(tableDisplayedAccounts);
    	radioButtonGroupPasswordStrength = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        tabbedPanel = new javax.swing.JTabbedPane();
        tabbedPanel.setName("tabbedPanel");
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textFieldSiteName = new javax.swing.JTextField();
        textFieldSiteName.setToolTipText("");
        textFieldSiteName.setName("textFieldSiteName");
        jLabel4 = new javax.swing.JLabel();
        textFieldUsername = new javax.swing.JTextField();
        textFieldUsername.setName("textFieldUsername");
        jLabel5 = new javax.swing.JLabel();
        textFieldPassword = new javax.swing.JTextField();
        textFieldPassword.setName("textFieldPassword");
        buttonSaveAccount = new javax.swing.JButton();
        buttonSaveAccount.setEnabled(false);
        buttonSaveAccount.setName("buttonSaveAccount");
        labelErrorMessage = new javax.swing.JLabel();
        labelErrorMessage.setName("labelErrorMessage");
        jSeparator2 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        radioButtonLowStrength = new javax.swing.JRadioButton();
        radioButtonLowStrength.setName("radioButtonLowStrength");
        radioButtonMediumStrength = new javax.swing.JRadioButton();
        radioButtonMediumStrength.setName("radioButtonMediumStrength");
        radioButtonHighStrength = new javax.swing.JRadioButton();
        radioButtonHighStrength.setName("radioButtonHighStrength");
        jLabel7 = new javax.swing.JLabel();
        sliderPasswordLength = new javax.swing.JSlider();
        sliderPasswordLength.setName("sliderPasswordLength");
        buttonGeneratePassword = new javax.swing.JButton();
        buttonGeneratePassword.setName("buttonGeneratePassword");
        textFieldGeneratedPassword = new javax.swing.JTextField();
        textFieldGeneratedPassword.setEditable(false);
        textFieldGeneratedPassword.setName("textFieldGeneratedPassword");
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();    
        textFieldSearchText = new javax.swing.JTextField();
        textFieldSearchText.setName("textFieldSearchText");
        jLabel10 = new javax.swing.JLabel();
        buttonFindAllAccounts = new javax.swing.JButton();
        buttonFindAllAccounts.setName("buttonFindAllAccounts");
        jSeparator1 = new javax.swing.JSeparator();
        buttonFindBySiteAccounts = new javax.swing.JButton();
        buttonFindBySiteAccounts.setEnabled(false);
        buttonFindBySiteAccounts.setName("buttonFindBySiteAccounts");
        buttonFindByUsernameAccounts = new javax.swing.JButton();
        buttonFindByUsernameAccounts.setEnabled(false);
        buttonFindByUsernameAccounts.setName("buttonFindByUsernameAccounts");
        buttonFindByPasswordAccounts = new javax.swing.JButton();
        buttonFindByPasswordAccounts.setEnabled(false);
        buttonFindByPasswordAccounts.setName("buttonFindByPasswordAccounts");
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel2.setText("New Account");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel3.setText("Site");
      

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel4.setText("Username");
        

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel5.setText("Password");
        

        buttonSaveAccount.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        buttonSaveAccount.setText("Save");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3Layout.setHorizontalGroup(
        	jPanel3Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel3Layout.createSequentialGroup()
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false)
        					.addComponent(buttonSaveAccount, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jLabel3)
        					.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        					.addComponent(textFieldSiteName)
        					.addComponent(textFieldUsername)
        					.addComponent(textFieldPassword)
        					.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
        					.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
        				.addComponent(labelErrorMessage))
        			.addPreferredGap(ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
        			.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
        	jPanel3Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel3Layout.createSequentialGroup()
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addGap(6)
        					.addComponent(jLabel2)
        					.addGap(18)
        					.addComponent(jLabel3)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(textFieldSiteName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel4)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(textFieldUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel5)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(buttonSaveAccount)
        					.addGap(18)
        					.addComponent(labelErrorMessage))
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addGap(11)
        					.addComponent(jSeparator2, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)))
        			.addContainerGap())
        );
        jPanel3.setLayout(jPanel3Layout);

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel1.setText("Passowrd Generator");

        jLabel6.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel6.setText("Strength");

        radioButtonLowStrength.setText("Low");

        radioButtonMediumStrength.setText("Medium");

        radioButtonHighStrength.setSelected(true);
        radioButtonHighStrength.setText("High");

        jLabel7.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel7.setText("Length");

        sliderPasswordLength.setMaximum(32);
        sliderPasswordLength.setMinimum(1);
        sliderPasswordLength.setMinorTickSpacing(1);
        sliderPasswordLength.setPaintLabels(true);
        sliderPasswordLength.setPaintTicks(true);
        sliderPasswordLength.setSnapToTicks(true);
        sliderPasswordLength.setToolTipText("");
        sliderPasswordLength.setValue(1);

        buttonGeneratePassword.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        buttonGeneratePassword.setText("Generate");

        jLabel8.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel8.setText("Generated Password");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(radioButtonLowStrength)
                        .addGap(18, 18, 18)
                        .addComponent(radioButtonMediumStrength)
                        .addGap(18, 18, 18)
                        .addComponent(radioButtonHighStrength))
                    .addComponent(jLabel7))
                .addContainerGap(134, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(textFieldGeneratedPassword)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(buttonGeneratePassword)
                        .addComponent(jLabel8))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(sliderPasswordLength, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonLowStrength)
                    .addComponent(radioButtonMediumStrength)
                    .addComponent(radioButtonHighStrength))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sliderPasswordLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonGeneratePassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldGeneratedPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        			.addGap(18)
        			.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, 578, GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jPanel3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
        				.addComponent(jPanel4, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
        			.addContainerGap())
        );
        jPanel1.setLayout(jPanel1Layout);

        tabbedPanel.addTab("Add | Generate Password", jPanel1);

        jLabel9.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel9.setText("Accounts");

       

        jLabel10.setText("Text to FInd");

        buttonFindAllAccounts.setText("Find All");
       

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonFindBySiteAccounts.setText("Find by Site");

        buttonFindByUsernameAccounts.setText("Find By Username");

        buttonFindByPasswordAccounts.setText("Find By Password");
        
        JButton buttonDeleteAccount = new JButton("Delete");
        buttonDeleteAccount.setName("buttonDeleteAccount");
        buttonDeleteAccount.setEnabled(false);
        
        JButton buttonModifyUsername = new JButton("Modify Username");
        buttonModifyUsername.setName("buttonModifyUsername");
        buttonModifyUsername.setEnabled(false);
        
        JButton buttonModifyPassword = new JButton("Modify Password");
        buttonModifyPassword.setName("buttonModifyPassword");
        buttonModifyPassword.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel9)
        						.addGroup(jPanel2Layout.createSequentialGroup()
        							.addComponent(buttonFindAllAccounts)
        							.addGap(18)
        							.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel10)
        						.addGroup(jPanel2Layout.createSequentialGroup()
        							.addComponent(textFieldSearchText, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
        							.addGap(18)
        							.addComponent(buttonFindBySiteAccounts)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(buttonFindByUsernameAccounts)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(buttonFindByPasswordAccounts)))
        					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        				.addComponent(scrollPaneAccountsTable, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addComponent(buttonDeleteAccount)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonModifyUsername)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonModifyPassword)
        					.addContainerGap(426, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addGap(17)
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addComponent(jLabel9)
        					.addGap(12)
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(buttonFindAllAccounts)
        						.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addComponent(jLabel10)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(textFieldSearchText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(buttonFindBySiteAccounts)
        						.addComponent(buttonFindByUsernameAccounts)
        						.addComponent(buttonFindByPasswordAccounts))))
        			.addGap(18)
        			.addComponent(scrollPaneAccountsTable, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(buttonDeleteAccount)
        				.addComponent(buttonModifyUsername)
        				.addComponent(buttonModifyPassword))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2.setLayout(jPanel2Layout);

        tabbedPanel.addTab("Saved Password", jPanel2);

        jLabel11.setText("Password Manager | Giulio Fagioli 6006222");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPanel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPanel.getAccessibleContext().setAccessibleName("Save Account");
        
        final KeyAdapter btnSaveEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(textFieldSiteName.getText().trim().isEmpty() || textFieldUsername.getText().trim().isEmpty() || textFieldPassword.getText().trim().isEmpty())
					buttonSaveAccount.setEnabled(false);
	            else
	            {
	            	buttonSaveAccount.setEnabled(true);
	            }
			}
		};
		
		final KeyAdapter findButtonsEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(textFieldSearchText.getText().trim().isEmpty()) {
					buttonFindByPasswordAccounts.setEnabled(false);
					buttonFindBySiteAccounts.setEnabled(false);
					buttonFindByUsernameAccounts.setEnabled(false);

				}else
	            {
	            	buttonFindByPasswordAccounts.setEnabled(true);
					buttonFindBySiteAccounts.setEnabled(true);
					buttonFindByUsernameAccounts.setEnabled(true);
	            }
			}
		};
		
		textFieldSiteName.addKeyListener(btnSaveEnabler);
		textFieldUsername.addKeyListener(btnSaveEnabler);
		textFieldPassword.addKeyListener(btnSaveEnabler);
		
		textFieldSearchText.addKeyListener(findButtonsEnabler);
		
		radioButtonGroupPasswordStrength.add(radioButtonLowStrength);
		radioButtonGroupPasswordStrength.add(radioButtonMediumStrength);
		radioButtonGroupPasswordStrength.add(radioButtonHighStrength);
		
		sliderLengthPasswordLabel = new Hashtable<>();
		sliderLengthPasswordLabel.put(1, new JLabel("1"));
		sliderLengthPasswordLabel.put(16, new JLabel("15"));
		sliderLengthPasswordLabel.put(32, new JLabel("32"));
        sliderPasswordLength.setLabelTable(sliderLengthPasswordLabel);
        sliderPasswordLength.setPaintLabels(true);
	    
        final ListSelectionListener accountsButtonEnabler = new ListSelectionListener(){
            
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if(tableDisplayedAccounts.getSelectedRowCount() > 0) {
					buttonDeleteAccount.setEnabled(true);
					buttonModifyPassword.setEnabled(true);
					buttonModifyUsername.setEnabled(true);
				}
				else {
					buttonDeleteAccount.setEnabled(false);
					buttonModifyPassword.setEnabled(false);
					buttonModifyUsername.setEnabled(false);
				}
				
			}
        };
        
        tableDisplayedAccounts.getSelectionModel().addListSelectionListener(accountsButtonEnabler);
	        
        pack();
    }// </editor-fold>                        

                                               


    private javax.swing.JButton buttonFindAllAccounts;
    private javax.swing.JButton buttonFindByPasswordAccounts;
    private javax.swing.JButton buttonFindBySiteAccounts;
    private javax.swing.JButton buttonFindByUsernameAccounts;
    private javax.swing.JButton buttonGeneratePassword;
    private javax.swing.ButtonGroup radioButtonGroupPasswordStrength;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton buttonSaveAccount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane scrollPaneAccountsTable;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JLabel labelErrorMessage;
    private javax.swing.JRadioButton radioButtonHighStrength;
    private javax.swing.JRadioButton radioButtonLowStrength;
    private javax.swing.JRadioButton radioButtonMediumStrength;
    private javax.swing.JSlider sliderPasswordLength;
    private javax.swing.JTable tableDisplayedAccounts;
    private javax.swing.JTextField textFieldGeneratedPassword;
    private javax.swing.JTextField textFieldPassword;
    private javax.swing.JTextField textFieldSearchText;
    private javax.swing.JTextField textFieldSiteName;
    private javax.swing.JTextField textFieldUsername;
    private Hashtable<Integer, JLabel> sliderLengthPasswordLabel;
    private List<Account> displayedAccounts;
    private DisplayedAccountsTableModel modelDisplayedAccounts;

    
    public void setListAccountTableData(List<Account> displayedAccounts){
    	SwingUtilities.invokeLater(() ->
    		modelDisplayedAccounts.setModel(displayedAccounts)
		);
    }
    
	@Override
	public void showAccounts(List<Account> accounts) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void accountIsAdded() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showError(String string) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showError(String string, Account accountToSave) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void accountIsModified() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void accountIsDeleted() {
		// TODO Auto-generated method stub
		
	}
}
