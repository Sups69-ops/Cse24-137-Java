package com.Bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import java.util.List;

/**
 * Controller for handling deposit and withdraw operations.
 */
public class TransactionViewController {
    @FXML private Label titleLabel;
    @FXML private TextField amountField;
    @FXML private Label errorMessage;
    @FXML private ChoiceBox<String> accountTypeChoice;
    @FXML private ChoiceBox<Account> accountChoice;
    @FXML private Label currentBalanceLabel;

    private TransactionType transactionType;
    private Account currentAccount;
    private String customerId;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

    @FXML
    private void initialize() {
        // Hide error label and populate choice box with account types
        errorMessage.setVisible(false);
        accountTypeChoice.setItems(FXCollections.observableArrayList("SAVINGS", "INVESTMENTS", "CHEQUE"));
        // Configure accountChoice to show a friendly label for Account objects
        if (accountChoice != null) {
            accountChoice.setConverter(new StringConverter<Account>() {
                @Override
                public String toString(Account acct) {
                    if (acct == null) return "";
                    return String.format("%s — %s — %.2f", acct.getAccountNumber(), acct.getAccountType(), acct.getBalance());
                }

                @Override
                public Account fromString(String string) {
                    // Not used
                    return null;
                }
            });

            accountChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    currentAccount = newVal;
                    currentBalanceLabel.setText("Current Balance: " + String.format("%.2f", currentAccount.getBalance()));
                    if (currentAccount.getAccountType() != null) {
                        accountTypeChoice.setValue(currentAccount.getAccountType());
                    }
                }
            });
        }
    }

    /**
     * Set whether this view is for deposit or withdraw and update the title.
     */
    public void setTransactionType(TransactionType type) {
        this.transactionType = type;
        titleLabel.setText(type == TransactionType.DEPOSIT ? "Deposit" : "Withdraw");
    }

    /**
     * Provide the selected account to show its balance and type.
     */
    public void setAccountData(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
            this.customerId = currentAccount.getCustomerId();
            try {
                // Populate accountChoice with Account objects belonging to this customer via DAO
                List<Account> accounts = AccountDAO.getAccountsForCustomer(this.customerId);
                ObservableList<Account> customerAccounts = FXCollections.observableArrayList(accounts);
                accountChoice.setItems(customerAccounts);
                // Select the account that was passed in
                accountChoice.setValue(currentAccount);
                // Update balance and account type UI
                currentBalanceLabel.setText("Current Balance: " + String.format("%.2f", currentAccount.getBalance()));
                if (currentAccount.getAccountType() != null) {
                    accountTypeChoice.setValue(currentAccount.getAccountType());
                }
            } catch (Exception e) {
                showError("Error loading accounts: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onSubmit() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                showError("Amount must be greater than 0");
                return;
            }

            // Determine which account the user selected to operate on
            Account target = accountChoice.getValue();
            if (target == null) {
                showError("Please select an account to operate on");
                return;
            }

            double newBalance;
            if (transactionType == TransactionType.DEPOSIT) {
                newBalance = target.getBalance() + amount;
            } else {
                if (amount > target.getBalance()) {
                    showError("Insufficient funds");
                    return;
                }
                newBalance = target.getBalance() - amount;
            }

            // Update account balance and account type (if user changed it)
            String selectedType = accountTypeChoice.getValue();
            Account updatedAccount = new Account(
                target.getAccountNumber(),
                target.getCustomerId(),
                newBalance,
                selectedType != null ? selectedType : target.getAccountType(),
                target.getBranch()
            );
            try {
                AccountDAO.updateAccount(updatedAccount);
                // record transaction
                String typeStr = (transactionType == TransactionType.DEPOSIT) ? "DEPOSIT" : "WITHDRAW";
                TransactionDAO.createTransaction(updatedAccount.getAccountNumber(), typeStr, amount);
                navigateToAccount();
            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        } catch (Exception e) {
            showError("Error processing transaction: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        navigateToAccount();
    }

    private void navigateToAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
            Parent root = loader.load();

            AccountViewController controller = loader.getController();
            controller.setCustomerId(currentAccount.getCustomerId());

            Stage stage = (Stage) amountField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Error returning to account view: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }
}