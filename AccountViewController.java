package com.Bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccountViewController {
    @FXML private TextField accountNumberField;
    @FXML private TextField balanceField;
    @FXML private ComboBox<String> branchCombo;
    @FXML private TextField customerIdField;
    @FXML private ComboBox<String> accountTypeCombo;

    @FXML private TableView<AccountData> accountsTable;
    @FXML private TableColumn<AccountData, String> colAccountNumber;
    @FXML private TableColumn<AccountData, Integer> colBalance;
    @FXML private TableColumn<AccountData, String> colBranch;
    @FXML private TableColumn<AccountData, String> colCustomerId;
    @FXML private TableColumn<AccountData, String> colAccountType;

    @FXML private Button insertButton;

    private final ObservableList<AccountData> accounts = FXCollections.observableArrayList();

    public static class AccountData {
        private String accountNumber;
        private String customerId;
        private String accountType;
        private String branch;
        private int balance;

        public AccountData(String accountNumber, String customerId, String accountType, String branch, int balance) {
            this.accountNumber = accountNumber;
            this.customerId = customerId;
            this.accountType = accountType;
            this.branch = branch;
            this.balance = balance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getCustomerId() { return customerId; }
        public String getAccountType() { return accountType; }
        public String getBranch() { return branch; }
        public int getBalance() { return balance; }
    }

    @FXML
    private void initialize() {
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAccountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        accountsTable.setItems(accounts);

        accountTypeCombo.setItems(FXCollections.observableArrayList("SAVINGS", "CHEQUE", "INVESTMENT"));
        branchCombo.setItems(FXCollections.observableArrayList("Gaborone", "Francistown", "Serowe", "Maun"));
    }

    @FXML
    private void onInsert() {
        String accountNumber = accountNumberField.getText();
        String customerId = customerIdField.getText();
        String type = accountTypeCombo.getValue();
        String branch = branchCombo.getValue();
        int parsedBalance = 0;
        try {
            parsedBalance = Integer.parseInt(balanceField.getText());
        } catch (Exception ignored) {}

        if (type == null || accountNumber.isEmpty() || customerId.isEmpty()) {
            return;
        }

        AccountData newAccount = new AccountData(accountNumber, customerId, type, branch, parsedBalance);
        accounts.add(newAccount);

        accountNumberField.clear();
        balanceField.clear();
        customerIdField.clear();
        accountTypeCombo.getSelectionModel().clearSelection();
        branchCombo.getSelectionModel().clearSelection();
    }
}


