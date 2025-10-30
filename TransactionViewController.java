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
import javafx.collections.FXCollections;

/**
 * Controller for handling deposit and withdraw operations.
 */
public class TransactionViewController {
    @FXML private Label titleLabel;
    @FXML private TextField amountField;
    @FXML private Label errorMessage;
    @FXML private ChoiceBox<String> accountTypeChoice;
    @FXML private Label currentBalanceLabel;

    private TransactionType transactionType;
    private Account currentAccount;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

    @FXML
    private void initialize() {
        // Hide error label and populate choice box with account types
        errorMessage.setVisible(false);
        accountTypeChoice.setItems(FXCollections.observableArrayList("SAVINGS", "INVESTMENTS", "CHEQUE"));
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
            currentBalanceLabel.setText("Current Balance: " + String.format("%.2f", currentAccount.getBalance()));
            // Select the account type in the choice box if it matches one of the values
            if (currentAccount.getAccountType() != null) {
                accountTypeChoice.setValue(currentAccount.getAccountType());
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

            double newBalance;
            if (transactionType == TransactionType.DEPOSIT) {
                newBalance = currentAccount.getBalance() + amount;
            } else {
                if (amount > currentAccount.getBalance()) {
                    showError("Insufficient funds");
                    return;
                }
                newBalance = currentAccount.getBalance() - amount;
            }

        // Update account balance and account type (if user changed it)
        String selectedType = accountTypeChoice.getValue();
        Account updatedAccount = new Account(
            currentAccount.getAccountNumber(),
            currentAccount.getCustomerId(),
            newBalance,
            selectedType != null ? selectedType : currentAccount.getAccountType(),
            currentAccount.getBranch()
        );
        AccountFileManager.updateAccount(updatedAccount);
            navigateToAccount();

        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        } catch (IOException e) {
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