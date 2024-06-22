package com.hbazai.carcrashprj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScoresController {

    @FXML
    private TableView<User> scoresTable;

    @FXML
    private TableColumn<User, Integer> placeColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, Integer> scoreColumn;

    @FXML
    private Button backButton;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        loadUsersFromFile("users.txt");
        scoresTable.setItems(userList);


        backButton.setOnAction(event -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            HelloApplication main = new HelloApplication();
            try {
                main.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadUsersFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int place = 1;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(", ");
                if (userDetails.length == 3) {
                    String username = userDetails[0].split(": ")[1].trim();
                    int score = Integer.parseInt(userDetails[2].split(": ")[1].trim());
                    userList.add(new User(place++, username, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
