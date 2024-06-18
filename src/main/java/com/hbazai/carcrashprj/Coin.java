package com.hbazai.carcrashprj;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Coin extends ScreenObject {

    private static Pane mainPane;


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
}
