package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateEnemyCar {
    private Pane pane;
    private List<EnemyCar> enemyCars;
    private double speed;

    int carWidth = 40;
    int carHeight = 80;

    public GenerateEnemyCar(Pane pane) {
        this.pane = pane;
        this.enemyCars = new ArrayList<>();
        this.speed = 20;
    }

    public void start() {
        int[] randomPosition = {40, 140, 240, 320};
        Timeline carGenTimeLine = new Timeline();
        List<String> carImages = Arrays.asList(
                "images/truck.png",
                "images/car_red.png",
                "images/car_white.png",
                "images/car2.png"
        );


        KeyFrame kf = new KeyFrame(Duration.seconds(2), actionEvent -> {
            try {
                String randomImagePath = carImages.get((int) (Math.random() * carImages.size()));
                if (randomImagePath.equals("images/truck.png")){
                    carWidth = 60;
                    carHeight = 120;
                }else {
                    carWidth = 40;
                    carHeight = 80;
                }
                EnemyCar enemyCar = new EnemyCar(randomPosition[(int) (Math.random() * 4)], 0, carWidth, carHeight, new Image(new FileInputStream(randomImagePath)), speed);
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

    public void setEnemyCarsSpeed(double speed) {
        this.speed = speed;
        for (EnemyCar enemyCar : enemyCars) {
            enemyCar.setSpeed(speed);
        }
    }

    public double getSpeed() {
        return speed;
    }

    public List<EnemyCar> getEnemyCars() {
        return enemyCars;
    }
}



