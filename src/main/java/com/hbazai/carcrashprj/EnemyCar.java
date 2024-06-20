package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class EnemyCar extends ScreenObject {
    private double speed;

    public EnemyCar(int x, int y, int width, int height, Image img, double speed) {
        super(x, y, width, height, img);
        this.speed = speed;
    }

    public void move(Pane pane) {
        Timeline movingTimeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(speed), actionEvent -> {
            setLayoutY(getLayoutY() + 1);
            if (getLayoutY() > 500) {
                removeFromPane(pane);
            }
        });

        movingTimeline.getKeyFrames().add(kf);
        movingTimeline.setCycleCount(Timeline.INDEFINITE);
        movingTimeline.play();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}


