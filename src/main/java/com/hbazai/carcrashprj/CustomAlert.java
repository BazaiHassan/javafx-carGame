package com.hbazai.carcrashprj;


import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class CustomAlert extends Dialog<ButtonType> {

    public CustomAlert(String title, String message) {
        setTitle(title);


        Label label = new Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        VBox content = new VBox(10);
        content.getChildren().add(label);
        content.setStyle("-fx-background-color: #336699; -fx-padding: 20px;");

        getDialogPane().setContent(content);


        getDialogPane().getStylesheets().add(
                getClass().getResource("custom-alert.css").toExternalForm());
        getDialogPane().getStyleClass().add("custom-dialog-pane");

        initStyle(StageStyle.TRANSPARENT);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
    }
}

