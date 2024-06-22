package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;


public class GasStation extends ScreenObject {
    private static final int SCREEN_HEIGHT = 600;

    private Timeline movementTimeline;

    public GasStation(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
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
        // Stop any existing movement
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
