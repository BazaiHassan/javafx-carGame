package com.hbazai.carcrashprj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Scores extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        showScoresScene(stage);
    }

    public void showScoresScene(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("scores.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("User Registration");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
