package com.Bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

public class RegisterViewController {
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    @FXML
    private void onRegister(javafx.event.ActionEvent event) throws Exception {
        String id = customerIdField.getText();
        String name = customerNameField.getText();
        if (id == null || id.isEmpty() || name == null || name.isEmpty()) {
            return;
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
}


