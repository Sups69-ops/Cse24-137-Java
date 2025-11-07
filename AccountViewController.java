package com.Bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AccountViewController {
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;
    @FXML private Button newAccountButton;
    @FXML private Button logoutButton;
    @FXML private TableView<Account> accountsTable;
    @FXML private TableColumn<Account, String> colAccountNumber;
    @FXML private TableColumn<Account, Double> colBalance;
    @FXML private TableColumn<Account, String> colBranch;
    @FXML private TableColumn<Account, String> colCustomerId;
    @FXML private TableColumn<Account, String> colAccountType;

    /** Set up the table columns so each column shows the right account field. */
    @FXML
    private void initialize() {
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAccountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        
    // Show account type as plain text (non-editable) in the table view
    colAccountType.setEditable(false);
    accountsTable.setEditable(false);
        
        // Handle account type changes. Run file I/O off the JavaFX thread.
        // Account type is shown as plain text; editing is disabled.
    }

    /**
     * Show a simple error dialog with a title and message.
     * @param title dialog title
     * @param content dialog message
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Open the transaction screen for the selected account.
     * @param type deposit or withdraw
     */
    private void handleTransaction(TransactionViewController.TransactionType type) {
        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account first");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionView.xml"));
            Parent root = loader.load();
            TransactionViewController controller = loader.getController();
            controller.setTransactionType(type);
            controller.setAccountData(selectedAccount);
            Stage stage = (Stage) depositButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not open transaction view: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeposit() {
        handleTransaction(TransactionViewController.TransactionType.DEPOSIT);
    }

    @FXML
    private void handleWithdraw() {
        handleTransaction(TransactionViewController.TransactionType.WITHDRAW);
    }

    @FXML
    private void handleNewAccount() {
        try {
            Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
            String customerId = null;
            if (selectedAccount != null) {
                customerId = selectedAccount.getCustomerId();
            } else {
                // If no account selected, we still need a customer id. Try to infer from table.
                if (accountsTable.getItems() != null && !accountsTable.getItems().isEmpty()) {
                    customerId = accountsTable.getItems().get(0).getCustomerId();
                } else {
                    showAlert("Error", "No customer context available to create an account");
                    return;
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewAccountView.fxml"));
            Parent root = loader.load();
            NewAccountController controller = loader.getController();
            controller.setCustomerId(customerId);

            Stage stage = (Stage) newAccountButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not open new account view: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LogInView.fxml"));
            if (root == null) {
                throw new IOException("Failed to load LogInView.fxml");
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not return to login view: " + e.getMessage());
        }
    }

    /**
     * Load accounts from disk and show only those that belong to customerId.
     * @param customerId show accounts for this customer
     * @throws IOException if reading the accounts file fails
     */
    public void setCustomerId(String customerId) {
        try {
            List<Account> filtered = AccountDAO.getAccountsForCustomer(customerId);
            ObservableList<Account> items = FXCollections.observableArrayList(filtered);
            accountsTable.setItems(items);
            if (filtered.isEmpty()) {
                showAlert("No accounts", "No accounts found for this user. You can create one later.");
            }
        } catch (Exception e) {
            showAlert("Error", "Could not load accounts: " + e.getMessage());
        }
    }
}


