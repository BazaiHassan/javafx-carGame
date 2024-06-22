package com.hbazai.carcrashprj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HelloController {
    @FXML
    private Pane mainPane;


    @FXML
    protected void initialize() throws FileNotFoundException {
        // Set the background image when the class is initialized
        setMainPaneBackground();
    }

    @FXML
    protected void startBtnAction() throws FileNotFoundException {
        if (Constants.USER_CHECK){
            ScreenManager sm = new ScreenManager(mainPane);
            sm.start();
        }else {
            showCustomAlert("Failed","You must create an account or login.");
        }
    }

    @FXML
    protected void scoreBtnAction(ActionEvent event){
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scores scores = new Scores();
            scores.showScoresScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void registerBtnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Register register = new Register();
            register.showRegistrationScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void loginBtnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Login login = new Login();
            login.showLoginScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMainPaneBackground() throws FileNotFoundException {
        // Load the image file
        Image image = new Image(new FileInputStream("images/menu-bg.png"));

        // Create a BackgroundImage
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // Set the Background of the Pane
        Background background = new Background(backgroundImage);
        mainPane.setBackground(background);
    }

    private void showCustomAlert(String title, String message) {
        CustomAlert alert = new CustomAlert(title, message);
        alert.showAndWait();
    }


}