package com.Bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class NewAccountController {
    @FXML private ChoiceBox<String> accountTypeChoice;
    @FXML private TextField initialBalanceField;
    @FXML private Label statusLabel;

    private String customerId;

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @FXML
    private void initialize() {
        if (accountTypeChoice != null) {
            accountTypeChoice.getItems().addAll("SAVINGS", "INVESTMENTS", "CHEQUE");
        }
    }

    @FXML
    private void onCreate(javafx.event.ActionEvent event) {
        String acctType = accountTypeChoice.getValue();
        if (acctType == null || acctType.isEmpty()) {
            statusLabel.setText("Please select an account type");
            return;
        }
        double balance = 0.0;
        try {
            String txt = initialBalanceField.getText();
            if (txt != null && !txt.trim().isEmpty()) {
                balance = Double.parseDouble(txt.trim());
                if (balance < 0) {
                    statusLabel.setText("Balance cannot be negative");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid balance format");
            return;
        }

        try {
            String acctNum = AccountDAO.generateAccountNumber(acctType);
            Account acct = new Account(acctNum, this.customerId, balance, acctType, "Main");
            AccountDAO.createAccount(acct);
            // After creating, go back to account view for this customer
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
            Parent root = loader.load();
            AccountViewController controller = loader.getController();
            controller.setCustomerId(this.customerId);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("Error creating account: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error creating account: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel(javafx.event.ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
            Parent root = loader.load();
            AccountViewController controller = loader.getController();
            controller.setCustomerId(this.customerId);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("Error returning to accounts: " + e.getMessage());
        }
    }
}
