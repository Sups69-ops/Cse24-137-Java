package com.Bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import java.io.IOException;

import java.util.List;
import java.sql.SQLException;

public class RegisterViewController {
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private ChoiceBox<String> initialAccountType;

    @FXML
    private void onRegister(javafx.event.ActionEvent event) throws Exception {
        String id = customerIdField.getText();
        String name = customerNameField.getText();
        String password = passwordField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        
        if (id == null || id.isEmpty() || name == null || name.isEmpty() || password == null || password.isEmpty()) {
            return;
        }

        User newUser = new User(id, name, password, address, phone);
        try {
            UserDAO.saveUser(newUser);
        } catch (SQLException e) {
            // If DB write fails, log and continue to allow user to try again from UI
            System.err.println("Error saving user to DB: " + e.getMessage());
        }

        // If user selected an initial account type, create the account with 0 balance
        String acctType = null;
        if (initialAccountType != null) {
            acctType = initialAccountType.getValue();
        }
        if (acctType != null && !acctType.isEmpty()) {
            // generate account number and save via AccountDAO
            try {
                String acctNum = AccountDAO.generateAccountNumber(acctType);
                Account acct = new Account(acctNum, id, 0.00, acctType, "Main");
                AccountDAO.createAccount(acct);
            } catch (SQLException e) {
                System.out.println("Warning: could not create initial account: " + e.getMessage());
            }
        }

        goToLogin(event);
    }

    @FXML
    private void onCancel(javafx.event.ActionEvent event) throws Exception {
        goToLogin(event);
    }

    private void goToLogin(javafx.event.ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("LogInView.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        if (initialAccountType != null) {
            initialAccountType.setItems(FXCollections.observableArrayList("SAVINGS", "INVESTMENTS", "CHEQUE"));
        }
    }
}


