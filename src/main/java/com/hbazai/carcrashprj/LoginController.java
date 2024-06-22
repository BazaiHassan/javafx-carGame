package com.hbazai.carcrashprj;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticateUser(username, password)) {
            showCustomAlert("Congratulation","Login was successful.");
            Constants.USER_CHECK = true;
            Constants.USERNAME = username;
        } else {
            showCustomAlert("Failed","Incorrect username or password.");
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            HelloApplication main = new HelloApplication();
            main.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean authenticateUser(String username, String password) {
        boolean isAuthenticated = false;
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(", ");
                if (parts.length >= 2) {
                    String savedUsername = parts[0].trim().substring("Username: ".length());
                    String savedPassword = parts[1].trim().substring("Password: ".length());

                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        isAuthenticated = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    private void showCustomAlert(String title, String message) {
        CustomAlert alert = new CustomAlert(title, message);
        alert.showAndWait();
    }

}



