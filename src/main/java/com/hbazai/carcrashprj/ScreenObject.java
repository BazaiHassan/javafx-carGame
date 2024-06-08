package com.hbazai.carcrashprj;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ScreenObject extends ImageView {
    public ScreenObject(
            int x,
            int y,
            int width,
            int height,
            Image img
    ) {
        setLayoutX(x);
        setLayoutY(y);
        setFitWidth(width);
        setFitHeight(height);
        setImage(img);
    }

    public void addToPane(Pane pane){
        pane.getChildren().add(this);
    }

    public void removeFromPane(Pane pane){
        pane.getChildren().remove(this);
    }
}
