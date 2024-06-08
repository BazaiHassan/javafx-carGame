package com.hbazai.carcrashprj;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerCar extends ScreenObject {
    private int width;
    private int height;

    public PlayerCar(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
