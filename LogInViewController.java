package com.Bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class LogInViewController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label errorMessage;

    @FXML
    private void initialize() {}

    @FXML
    private void onLogin(javafx.event.ActionEvent event) throws Exception {
        if ("user".equals(usernameField.getText()) && "password".equals(passwordField.getText())) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("AccountView.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            errorMessage.setText("Invalid username or password");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void onRegister(javafx.event.ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("RegisterView.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}