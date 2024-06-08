package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GenerateEnemyCar {
    private Pane pane;
    private List<EnemyCar> enemyCars;
    private int speed;

    public GenerateEnemyCar(Pane pane) {
        this.pane = pane;
        this.enemyCars = new ArrayList<>();
        this.speed = 20;
    }

    public void start() {
        int[] randomPosition = {40, 140, 240, 320};
        Timeline carGenTimeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.seconds(2), actionEvent -> {
            try {
                EnemyCar enemyCar = new EnemyCar(randomPosition[(int) (Math.random() * 4)], 0, 40, 80, new Image(new FileInputStream("images/car2.png")), speed);
                enemyCar.addToPane(pane);
                enemyCar.move(pane);
                enemyCars.add(enemyCar);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        carGenTimeLine.getKeyFrames().add(kf);
        carGenTimeLine.setCycleCount(Timeline.INDEFINITE);
        carGenTimeLine.play();
    }

    public void setEnemyCarsSpeed(int speed) {
        this.speed = speed; // Update the speed value for future cars
        for (EnemyCar enemyCar : enemyCars) {
            enemyCar.setSpeed(speed);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public List<EnemyCar> getEnemyCars() {
        return enemyCars;
    }
}



