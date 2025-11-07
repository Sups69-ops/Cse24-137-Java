package com.Bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.SQLException;

public class LogInViewController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessage;

    @FXML
    private void initialize() {}

    @FXML
    private void onLogin(javafx.event.ActionEvent event) throws Exception {
        String customerId = usernameField.getText();
        String password = passwordField.getText();
        
        if (customerId == null || customerId.isEmpty() || password == null || password.isEmpty()) {
            errorMessage.setText("Please enter both customer ID and password");
            errorMessage.setVisible(true);
            return;
        }

        try {
            if (UserDAO.validateUser(customerId, password)) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
                Parent root = loader.load();
                
                // Pass the customer ID to the account view
                AccountViewController controller = loader.getController();
                controller.setCustomerId(customerId);
                
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                errorMessage.setText("Invalid customer ID or password");
                errorMessage.setVisible(true);
            }
        } catch (SQLException sq) {
            errorMessage.setText("Database error: " + sq.getMessage());
            errorMessage.setVisible(true);
        } catch (IOException e) {
            errorMessage.setText("Error checking credentials: " + e.getMessage());
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