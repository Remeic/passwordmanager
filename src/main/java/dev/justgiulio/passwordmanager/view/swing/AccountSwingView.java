package dev.justgiulio.passwordmanager.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dev.justgiulio.passwordmanager.controller.AccountController;
import dev.justgiulio.passwordmanager.model.Account;
import dev.justgiulio.passwordmanager.model.Credential;
import dev.justgiulio.passwordmanager.view.AccountView;

public class AccountSwingView extends javax.swing.JFrame implements AccountView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountSwingView() {
		initComponents();
	}

	private void initComponents() {
		this.setVisible(true);
		modelDisplayedAccounts = new DisplayedAccountsTableModel();
		tableDisplayedAccounts = new javax.swing.JTable(modelDisplayedAccounts);
		tableDisplayedAccounts.setName("tableDisplayedAccounts");
		scrollPaneAccountsTable = new javax.swing.JScrollPane(tableDisplayedAccounts);
		scrollPaneAccountsTable.setName("scrollPaneAccounts");
		scrollPaneAccountsTable.setViewportView(tableDisplayedAccounts);
		radioButtonGroupPasswordStrength = new javax.swing.ButtonGroup();
		tabbedPanel = new javax.swing.JTabbedPane();
		tabbedPanel.setName("tabbedPanel");
		panelGeneratePassword = new javax.swing.JPanel();
		panelGeneratePassword.setName("panelGeneratePassword");
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
		labelErrorMessage.setEnabled(false);
		labelErrorMessage.setName("labelErrorMessage");
		jSeparator2 = new javax.swing.JSeparator();
		jPanel4 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		radioButtonLowStrength = new javax.swing.JRadioButton();
		radioButtonLowStrength.setActionCommand("STRENGHT_PASSWORD_LOW");
		radioButtonLowStrength.setName("radioButtonLowStrength");
		radioButtonMediumStrength = new javax.swing.JRadioButton();
		radioButtonMediumStrength.setActionCommand("STRENGHT_PASSWORD_MEDIUM");
		radioButtonMediumStrength.setName("radioButtonMediumStrength");
		radioButtonHighStrength = new javax.swing.JRadioButton();
		radioButtonHighStrength.setActionCommand("STRENGHT_PASSWORD_HIGH");
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
		panelDisplayedAccounts = new javax.swing.JPanel();
		panelDisplayedAccounts.setName("panelDisplayedAccounts");
		jLabel9 = new javax.swing.JLabel();
		textFieldSearchText = new javax.swing.JTextField();
		textFieldSearchText.setName("textFieldSearchText");
		jLabel10 = new javax.swing.JLabel();
		buttonFindAllAccounts = new javax.swing.JButton();
		buttonFindAllAccounts.setActionCommand("FILTER_ALL");
		buttonFindAllAccounts.setName("buttonFindAllAccounts");
		jSeparator1 = new javax.swing.JSeparator();
		buttonFindBySiteAccounts = new javax.swing.JButton();
		buttonFindBySiteAccounts.setActionCommand("FILTER_BY_SITE");
		buttonFindBySiteAccounts.setEnabled(false);
		buttonFindBySiteAccounts.setName("buttonFindBySiteAccounts");
		buttonFindByUsernameAccounts = new javax.swing.JButton();
		buttonFindByUsernameAccounts.setActionCommand("FILTER_BY_USERNAME");
		buttonFindByUsernameAccounts.setEnabled(false);
		buttonFindByUsernameAccounts.setName("buttonFindByUsernameAccounts");
		buttonFindByPasswordAccounts = new javax.swing.JButton();
		buttonFindByPasswordAccounts.setActionCommand("FILTER_BY_PASSWORD");
		buttonFindByPasswordAccounts.setEnabled(false);
		buttonFindByPasswordAccounts.setName("buttonFindByPasswordAccounts");
		jLabel11 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jLabel2.setFont(new java.awt.Font(SELECTED_FONT, 0, 18)); // NOI18N
		jLabel2.setText("New Account");

		jLabel3.setFont(new java.awt.Font(SELECTED_FONT, 1, 14)); // NOI18N
		jLabel3.setText("Site");

		jLabel4.setFont(new java.awt.Font(SELECTED_FONT, 1, 14)); // NOI18N
		jLabel4.setText("Username");

		jLabel5.setFont(new java.awt.Font(SELECTED_FONT, 1, 14)); // NOI18N
		jLabel5.setText("Password");

		buttonSaveAccount.setFont(new java.awt.Font(SELECTED_FONT, 1, 18)); // NOI18N
		buttonSaveAccount.setText("Save");

		jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

		labelAccountAdded = new JLabel("");
		labelAccountAdded.setEnabled(false);
		labelAccountAdded.setName("labelAccountAdded");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout
				.createSequentialGroup()
				.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(buttonSaveAccount, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel3)
								.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
								.addComponent(textFieldSiteName).addComponent(textFieldUsername)
								.addComponent(textFieldPassword)
								.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addComponent(labelErrorMessage).addComponent(labelAccountAdded))
				.addPreferredGap(ComponentPlacement.RELATED, 115, Short.MAX_VALUE).addComponent(jSeparator2,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout
				.createSequentialGroup()
				.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup().addGap(6).addComponent(jLabel2).addGap(18)
								.addComponent(jLabel3).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldSiteName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(jLabel4)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(jLabel5)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(buttonSaveAccount).addGap(18).addComponent(labelErrorMessage)
								.addGap(18).addComponent(labelAccountAdded))
						.addGroup(jPanel3Layout.createSequentialGroup().addGap(11).addComponent(jSeparator2,
								GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)))
				.addContainerGap()));
		jPanel3.setLayout(jPanel3Layout);

		jLabel1.setFont(new java.awt.Font(SELECTED_FONT, 0, 18)); // NOI18N
		jLabel1.setText("Passowrd Generator");

		jLabel6.setFont(new java.awt.Font(SELECTED_FONT, 1, 14)); // NOI18N
		jLabel6.setText("Strength");

		radioButtonLowStrength.setText("Low");

		radioButtonMediumStrength.setText("Medium");

		radioButtonHighStrength.setSelected(true);
		radioButtonHighStrength.setText("High");

		jLabel7.setFont(new java.awt.Font(SELECTED_FONT, 1, 14)); // NOI18N
		jLabel7.setText("Length");

		sliderPasswordLength.setMaximum(32);
		sliderPasswordLength.setMinimum(1);
		sliderPasswordLength.setMinorTickSpacing(1);
		sliderPasswordLength.setPaintLabels(true);
		sliderPasswordLength.setPaintTicks(true);
		sliderPasswordLength.setSnapToTicks(true);
		sliderPasswordLength.setToolTipText("");
		sliderPasswordLength.setValue(1);

		buttonGeneratePassword.setFont(new java.awt.Font(SELECTED_FONT, 1, 16)); // NOI18N
		buttonGeneratePassword.setText("Generate");

		jLabel8.setFont(new java.awt.Font(SELECTED_FONT, 1, 14)); // NOI18N
		jLabel8.setText("Generated Password");

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout
				.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6)
								.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(jPanel4Layout.createSequentialGroup().addComponent(radioButtonLowStrength)
										.addGap(18, 18, 18).addComponent(radioButtonMediumStrength).addGap(18, 18, 18)
										.addComponent(radioButtonHighStrength))
								.addComponent(jLabel7)).addContainerGap(134, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(textFieldGeneratedPassword)
								.addGroup(jPanel4Layout.createSequentialGroup()
										.addGroup(jPanel4Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(buttonGeneratePassword).addComponent(jLabel8))
										.addGap(0, 0, Short.MAX_VALUE))
								.addComponent(sliderPasswordLength, javax.swing.GroupLayout.DEFAULT_SIZE, 370,
										Short.MAX_VALUE)));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)
						.addGap(18, 18, 18).addComponent(jLabel6)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(radioButtonLowStrength).addComponent(radioButtonMediumStrength)
								.addComponent(radioButtonHighStrength))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel7)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(sliderPasswordLength, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(buttonGeneratePassword)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel8)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(textFieldGeneratedPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(102, Short.MAX_VALUE)));

		javax.swing.GroupLayout glPanelGeneratePassword = new javax.swing.GroupLayout(panelGeneratePassword);
		glPanelGeneratePassword
				.setHorizontalGroup(glPanelGeneratePassword.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, glPanelGeneratePassword.createSequentialGroup().addContainerGap()
								.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE).addGap(18)
								.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, 578, GroupLayout.PREFERRED_SIZE)));
		glPanelGeneratePassword.setVerticalGroup(glPanelGeneratePassword.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, glPanelGeneratePassword.createSequentialGroup().addContainerGap()
						.addGroup(glPanelGeneratePassword.createParallelGroup(Alignment.LEADING)
								.addComponent(jPanel3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 342,
										Short.MAX_VALUE)
								.addComponent(jPanel4, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 342,
										Short.MAX_VALUE))
						.addContainerGap()));
		panelGeneratePassword.setLayout(glPanelGeneratePassword);

		tabbedPanel.addTab("Add | Generate Password", panelGeneratePassword);

		jLabel9.setFont(new java.awt.Font(SELECTED_FONT, 0, 14)); // NOI18N
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
		buttonModifyUsername.setActionCommand(ACTION_MODIFY_USERNAME);
		buttonModifyUsername.setName("buttonModifyUsername");
		buttonModifyUsername.setEnabled(false);

		JButton buttonModifyPassword = new JButton("Modify Password");
		buttonModifyPassword.setActionCommand("MODIFY_PASSWORD");
		buttonModifyPassword.setName("buttonModifyPassword");
		buttonModifyPassword.setEnabled(false);

		textFieldUpdateCell = new JTextField();
		textFieldUpdateCell.setName("textFieldUpdateCell");
		textFieldUpdateCell.setEnabled(false);
		textFieldUpdateCell.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);

		labelOperationResult = new JLabel("");
		labelOperationResult.setEnabled(false);
		labelOperationResult.setName("labelOperationResult");

		javax.swing.GroupLayout glPanelDisplayedAccounts = new javax.swing.GroupLayout(panelDisplayedAccounts);
		glPanelDisplayedAccounts.setHorizontalGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.LEADING)
				.addGroup(glPanelDisplayedAccounts.createSequentialGroup().addContainerGap()
						.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.LEADING)
								.addGroup(glPanelDisplayedAccounts.createSequentialGroup()
										.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.LEADING)
												.addComponent(jLabel9)
												.addGroup(glPanelDisplayedAccounts.createSequentialGroup()
														.addComponent(buttonFindAllAccounts).addGap(18)
														.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 21,
																GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.LEADING)
												.addComponent(jLabel10)
												.addGroup(glPanelDisplayedAccounts.createSequentialGroup()
														.addComponent(textFieldSearchText, GroupLayout.PREFERRED_SIZE,
																159, GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(buttonFindBySiteAccounts)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(buttonFindByUsernameAccounts)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(buttonFindByPasswordAccounts)))
										.addContainerGap(209, Short.MAX_VALUE))
								.addComponent(scrollPaneAccountsTable, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										971, Short.MAX_VALUE)
								.addGroup(
										glPanelDisplayedAccounts.createSequentialGroup()
												.addComponent(buttonDeleteAccount)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(separator, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(textFieldUpdateCell, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(buttonModifyUsername).addGap(18)
												.addComponent(buttonModifyPassword).addGap(18)
												.addComponent(labelOperationResult, GroupLayout.PREFERRED_SIZE, 350,
														GroupLayout.PREFERRED_SIZE)
												.addContainerGap()))));
		glPanelDisplayedAccounts.setVerticalGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.LEADING)
				.addGroup(glPanelDisplayedAccounts.createSequentialGroup().addGap(17).addGroup(glPanelDisplayedAccounts
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(glPanelDisplayedAccounts.createSequentialGroup().addComponent(jLabel9).addGap(12)
								.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.LEADING)
										.addComponent(buttonFindAllAccounts).addComponent(jSeparator1,
												GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
						.addGroup(glPanelDisplayedAccounts.createSequentialGroup().addComponent(jLabel10)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.BASELINE)
										.addComponent(textFieldSearchText, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonFindBySiteAccounts)
										.addComponent(buttonFindByUsernameAccounts)
										.addComponent(buttonFindByPasswordAccounts))))
						.addGap(18)
						.addComponent(
								scrollPaneAccountsTable, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.TRAILING)
								.addGroup(glPanelDisplayedAccounts.createParallelGroup(Alignment.BASELINE)
										.addComponent(buttonDeleteAccount)
										.addComponent(textFieldUpdateCell, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonModifyUsername).addComponent(buttonModifyPassword)
										.addComponent(labelOperationResult))
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
						.addContainerGap(9, Short.MAX_VALUE)));
		panelDisplayedAccounts.setLayout(glPanelDisplayedAccounts);

		tabbedPanel.addTab("Saved Password", panelDisplayedAccounts);

		jLabel11.setText("Password Manager | Giulio Fagioli 6006222");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(tabbedPanel).addGroup(layout.createSequentialGroup()
												.addComponent(jLabel11).addGap(0, 0, Short.MAX_VALUE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(10, Short.MAX_VALUE).addComponent(jLabel11)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tabbedPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		tabbedPanel.getAccessibleContext().setAccessibleName("Save Account");

		final KeyAdapter btnSaveEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				resetErrorLabel();
				resetAccountAddedLabel();
				boolean buttonIsEnable = !(textFieldSiteName.getText().trim().isEmpty()
						|| textFieldUsername.getText().trim().isEmpty()
						|| textFieldPassword.getText().trim().isEmpty());
				buttonSaveAccount.setEnabled(buttonIsEnable);
			}
		};

		final KeyAdapter findButtonsEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (textFieldSearchText.getText().trim().isEmpty()) {
					buttonFindByPasswordAccounts.setEnabled(false);
					buttonFindBySiteAccounts.setEnabled(false);
					buttonFindByUsernameAccounts.setEnabled(false);
				} else {
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

		final ListSelectionListener accountsButtonEnabler = new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {
				resetLabelOperationResult();
				if (tableDisplayedAccounts.getSelectedRowCount() > 0) {
					textFieldUpdateCell.setEnabled(true);
					textFieldUpdateCell.setText("");
					buttonDeleteAccount.setEnabled(true);
				} else {
					buttonDeleteAccount.setEnabled(false);
					buttonModifyPassword.setEnabled(false);
					buttonModifyUsername.setEnabled(false);
					textFieldUpdateCell.setEnabled(false);
				}

			}
		};

		final KeyAdapter modifyButtonsEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (textFieldUpdateCell.getText().trim().isEmpty()) {
					buttonModifyPassword.setEnabled(false);
					buttonModifyUsername.setEnabled(false);
				} else {
					buttonModifyPassword.setEnabled(true);
					buttonModifyUsername.setEnabled(true);
				}
			}
		};

		textFieldUpdateCell.addKeyListener(modifyButtonsEnabler);

		tableDisplayedAccounts.getSelectionModel().addListSelectionListener(accountsButtonEnabler);
		ActionListener updateCellComponentsEnabler = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int selectedRow = tableDisplayedAccounts.getSelectedRow();
				String currentActionCommand = actionEvent.getActionCommand();
				if (currentActionCommand.equals(ACTION_MODIFY_USERNAME)) {
					SwingUtilities.invokeLater(() -> {
						String site = (String) tableDisplayedAccounts.getValueAt(selectedRow, 0);
						String username = (String) tableDisplayedAccounts.getValueAt(selectedRow, 1);
						String password = (String) tableDisplayedAccounts.getValueAt(selectedRow, 2);
						Account tmpAccount = new Account(site, new Credential(username, password));
						accountController.modifyUsername(tmpAccount, textFieldUpdateCell.getText());

					});
				} else {
					SwingUtilities.invokeLater(() -> {
						String site = (String) tableDisplayedAccounts.getValueAt(selectedRow, 0);
						String username = (String) tableDisplayedAccounts.getValueAt(selectedRow, 1);
						String password = (String) tableDisplayedAccounts.getValueAt(selectedRow, 2);
						Account tmpAccount = new Account(site, new Credential(username, password));
						accountController.modifyPassword(tmpAccount, textFieldUpdateCell.getText());
					});
				}
			}
		};

		buttonModifyUsername.addActionListener(updateCellComponentsEnabler);
		buttonModifyPassword.addActionListener(updateCellComponentsEnabler);

		
		
		ActionListener generatePasswordListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				accountController.generatePassword(sliderPasswordLength.getValue(),
						radioButtonGroupPasswordStrength.getSelection().getActionCommand());
			}
		};

		buttonGeneratePassword.addActionListener(generatePasswordListener);
		
		ActionListener saveAccountListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				accountController.saveAccount(new Account(textFieldSiteName.getText(),
						new Credential(textFieldUsername.getText(), textFieldPassword.getText())));
			}
		};
		buttonSaveAccount.addActionListener(saveAccountListener);
		
		ActionListener findAllListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				accountController.findAllAccounts();
			}
		};
		buttonFindAllAccounts.addActionListener(findAllListener);
		
		ActionListener findByUsernameListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				accountController.findAccountsByUsername(textFieldSearchText.getText());
			}
		};
		buttonFindByUsernameAccounts.addActionListener(findByUsernameListener);
		
		ActionListener findByPasswordListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				accountController.findAccountsByPassword(textFieldSearchText.getText());
			}
		};
		buttonFindByPasswordAccounts.addActionListener(findByPasswordListener);
		

		ActionListener findBySiteListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				accountController.findAccountsByKey(textFieldSearchText.getText());
			}
		};
		buttonFindBySiteAccounts.addActionListener(findBySiteListener);
		

		ActionListener deleteAccountListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int selectedRow = tableDisplayedAccounts.getSelectedRow();
				SwingUtilities.invokeLater(() -> {
					String site = (String) tableDisplayedAccounts.getValueAt(selectedRow, 0);
					String username = (String) tableDisplayedAccounts.getValueAt(selectedRow, 1);
					String password = (String) tableDisplayedAccounts.getValueAt(selectedRow, 2);
					accountController.delete(new Account(site, new Credential(username,password)));
				});
			}
		};
		buttonDeleteAccount.addActionListener(deleteAccountListener);

		pack();
	}// </editor-fold>

	private javax.swing.JButton buttonFindAllAccounts;
	private javax.swing.JButton buttonFindByPasswordAccounts;
	private javax.swing.JButton buttonFindBySiteAccounts;
	private javax.swing.JButton buttonFindByUsernameAccounts;
	private javax.swing.JButton buttonGeneratePassword;
	private javax.swing.ButtonGroup radioButtonGroupPasswordStrength;
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
	private javax.swing.JPanel panelGeneratePassword;
	private javax.swing.JPanel panelDisplayedAccounts;
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
	private transient List<Account> displayedAccounts;
	private DisplayedAccountsTableModel modelDisplayedAccounts;
	private JTextField textFieldUpdateCell;
	private static final String ACTION_MODIFY_USERNAME = "MODIFY_USERNAME";
	private transient AccountController accountController;
	private JLabel labelOperationResult;
	private JLabel labelAccountAdded;
	private static final String SELECTED_FONT = "sansserif";

	public void setListAccountTableData(List<Account> accountsTableData) {
		SwingUtilities.invokeLater(() -> {
			displayedAccounts = accountsTableData;
			modelDisplayedAccounts.setModel(displayedAccounts);
		});
	}

	@Override
	public void showAccounts(List<Account> accounts) {
		SwingUtilities.invokeLater(() -> this.setListAccountTableData(accounts));

	}

	@Override
	public void accountIsAdded() {
		
		SwingUtilities.invokeLater(() -> {
			labelAccountAdded.setEnabled(true);
			labelAccountAdded.setText("Account Saved!");
			textFieldUsername.setText("");
			textFieldSiteName.setText("");
			textFieldPassword.setText("");

		});
	}

	@Override
	public void showError(String string) {
		SwingUtilities.invokeLater(() -> {
			labelErrorMessage.setEnabled(true);
			labelErrorMessage.setText(string);
		});
	}

	@Override
	public void showError(String string, Account accountToSave) {
		SwingUtilities.invokeLater(() -> {
			labelErrorMessage.setEnabled(true);
			labelErrorMessage.setText(string + ": " + accountToSave);
		});
	}

	@Override
	public void accountIsModified() {
		SwingUtilities.invokeLater(() -> {
			labelOperationResult.setEnabled(true);
			labelOperationResult.setText("Account Modified!");
		});

	}

	@Override
	public void accountIsDeleted() {
		SwingUtilities.invokeLater(() -> {
			labelOperationResult.setEnabled(true);
			labelOperationResult.setText("Account Deleted!");
		});

	}

	@Override
	public void passwordIsGenereated(String generatedPassword) {
		SwingUtilities.invokeLater(() -> textFieldGeneratedPassword.setText(generatedPassword));
	}
	
	@Override
	public void showAccountRelatedError(String string) {
		SwingUtilities.invokeLater(() -> {
			labelOperationResult.setEnabled(true);
			labelOperationResult.setText(string);
		});
		
	}

	public void setAccountController(AccountController accountController) {
		this.accountController = accountController;
	}

	private void resetLabelOperationResult() {
		SwingUtilities.invokeLater(() -> {
			labelOperationResult.setEnabled(false);
			labelOperationResult.setText("");
		});
	}

	private void resetErrorLabel() {
		SwingUtilities.invokeLater(() -> {
			labelErrorMessage.setEnabled(false);
			labelErrorMessage.setText("");
		});
	}

	private void resetAccountAddedLabel() {
		SwingUtilities.invokeLater(() -> {
			labelAccountAdded.setEnabled(false);
			labelAccountAdded.setText("");
		});
	}

	

}
