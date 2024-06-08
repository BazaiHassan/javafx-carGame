package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;


public class GasStation extends ScreenObject {
    private static final int SCREEN_HEIGHT = 600; // Height of the screen
    private static final int SCREEN_WIDTH = 400;
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

        // Create a new Timeline for moving the gas station down
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(60), event -> {
            setY(getY() + speed); // Move the gas station down by 'speed' units

            // Check if the gas station is out of the screen
            if (getY() > SCREEN_HEIGHT) {
                removeFromPane(pane); // Remove from pane if out of the screen
                movementTimeline.stop(); // Stop the timeline
            }
        }));

        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }
}
