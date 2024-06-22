package com.hbazai.carcrashprj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;



    @FXML
    private void handleRegisterAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isUsernameTaken(username)) {
            showCustomAlert("Username Already Taken", "The username '" + username + "' is already taken.");
        } else {
            saveToFile(username, password);
            showCustomAlert("Registration Successful", "User '" + username + "' can play now.");
            Constants.USER_CHECK = true;
            Constants.USERNAME = username;
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


    private boolean isUsernameTaken(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: " + username)) {
                    return true; // Username found in the file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write("Username: " + username + ", Password: " + password + ", Score: 0");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCustomAlert(String title, String message) {
        CustomAlert alert = new CustomAlert(title, message);
        alert.showAndWait();
    }
}
