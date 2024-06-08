package com.hbazai.carcrashprj;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;

public class HelloController {
    @FXML
    private Pane mainPane;

    @FXML
    protected void startBtnAction() throws FileNotFoundException {
        ScreenManager sm = new ScreenManager(mainPane);
        sm.start();
    }
}