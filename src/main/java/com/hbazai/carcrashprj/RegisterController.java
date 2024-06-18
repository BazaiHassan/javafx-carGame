package com.hbazai.carcrashprj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegisterAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        saveToFile(username, password);
    }

    @FXML
    private void handleBackAction(ActionEvent event){
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            HelloApplication main = new HelloApplication();
            main.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write("Username: " + username + ", Password: " + password+ ", Score: " + 0);
            writer.newLine();
            System.out.println("User saved: " + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
