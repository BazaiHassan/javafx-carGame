package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Coin extends ScreenObject {

    private static Pane mainPane;
    private Timeline movementTimeline;
    private static final int SCREEN_HEIGHT = 600;

    public static void initialize(Pane pane) {
        mainPane = pane;
    }

    public Coin(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }

    public static void addCoinCounter() throws FileNotFoundException {
        Image img = new Image(new FileInputStream("images/coin.png"));
        ImageView coinImg = new ImageView(img);
        coinImg.setLayoutX(360);
        coinImg.setLayoutY(40);
        coinImg.setFitHeight(20);
        coinImg.setFitWidth(20);
        mainPane.getChildren().add(coinImg);
    }

    public void showRandomlyOnScreen(Pane pane) {
        Random random = new Random();
        int randomX = 10 + random.nextInt(311 - 10);
        int randomY = 10 + random.nextInt(411 - 10);

        setX(randomX);
        setY(randomY);

        addToPane(pane);
    }

    public void moveDown(Pane pane, int speed) {

        if (movementTimeline != null) {
            movementTimeline.stop();
        }

        movementTimeline = new Timeline(new KeyFrame(Duration.millis(60), event -> {
            setY(getY() + speed);


            if (getY() > SCREEN_HEIGHT) {
                removeFromPane(pane);
                movementTimeline.stop();
            }
        }));

        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }
}
