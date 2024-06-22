package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Fuel extends ScreenObject {
    private static List<ImageView> fuels = new ArrayList<>();
    private static Pane mainPane;

    public Fuel(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }

    public static void initialize(Pane pane) {
        mainPane = pane;
    }

    public static void addFuel(int numberOfFuel) throws FileNotFoundException {
        for (int i = 0; i < numberOfFuel; i++) {
            Image img = new Image(new FileInputStream("images/fuel.png"));
            ImageView fuelImg = new ImageView(img);
            fuelImg.setLayoutX(i * 20 + 280);
            fuelImg.setLayoutY(10);
            fuelImg.setFitHeight(20);
            fuelImg.setFitWidth(20);
            fuels.add(fuelImg);
            mainPane.getChildren().add(fuelImg);
        }
    }

    public static void consumeFuel() {
        Timeline fuelTimeLine = new Timeline(new KeyFrame(Duration.seconds(20), actionEvent -> {
            if (fuels.size() > 0) {
                ImageView fuel = fuels.remove(fuels.size() - 1);
                mainPane.getChildren().remove(fuel);
            } else {
                Platform.runLater(() -> {
                    showGameOverAlert();
                    stopGame();
                });
            }
        }));
        fuelTimeLine.setCycleCount(Timeline.INDEFINITE);
        fuelTimeLine.play();
    }

    private static void showGameOverAlert() {

    }

    private static void stopGame() {

    }
}
