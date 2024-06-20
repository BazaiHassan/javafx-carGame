package com.hbazai.carcrashprj;


import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class CustomAlert extends Dialog<ButtonType> {

    public CustomAlert(String title, String message) {
        setTitle(title);

        // Create components
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        VBox content = new VBox(10);
        content.getChildren().add(label);
        content.setStyle("-fx-background-color: #336699; -fx-padding: 20px;");

        getDialogPane().setContent(content);

        // Style the dialog pane
        getDialogPane().getStylesheets().add(
                getClass().getResource("custom-alert.css").toExternalForm());
        getDialogPane().getStyleClass().add("custom-dialog-pane");

        // Set stage style to transparent
        initStyle(StageStyle.TRANSPARENT);

        // Add OK button to close the dialog
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
    }
}

