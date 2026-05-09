package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    String result;

    private ZenSpace mainApp;
    public void setApp(ZenSpace app) {
        this.mainApp = app;
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password cannot be empty.");
            return;
        }


        NormalUser normalUser = NormalUser.loginFromFile(username, password);

        if (normalUser != null) {
            statusLabel.setText("Login successful! Welcome, " + username + ".");
            if (mainApp != null) {
                mainApp.loadMainAppView(normalUser);
            }
        } else {
            statusLabel.setText("Login failed! Check credentials or register.");
        }
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password cannot be empty.");
            return;
        }

        result = NormalUser.registerToFile(username, password);

        if ("SUCCESS".equals(result)) {
            statusLabel.setText("Registration successful! You can now log in.");
            usernameField.clear();
            passwordField.clear();
        } else {
            statusLabel.setText(result);
        }
    }


}