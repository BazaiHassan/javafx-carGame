package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.hbazai.carcrashprj.Fuel.consumeFuel;

public class ScreenManager {
    private Pane mainPane;
    private List<ImageView> hearts;
    private List<Fire> bullets;
    private List<ImageView> fuels;
    private Timeline collisionCheckTimeline;
    private int passedOrDestroyedCount = 0;
    private boolean gameOver = false;
    private GenerateEnemyCar generateEnemyCar;
    private int numberOfFuel = 5;
    private Label label = new Label();

    public ScreenManager(Pane mainPane) {
        this.mainPane = mainPane;
        this.hearts = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.fuels = new ArrayList<>();
    }

    public void start() throws FileNotFoundException {
        Fuel.initialize(mainPane);
        Coin.initialize(mainPane);
        addRoad();
        addHeart();

        try {
            Fuel.addFuel(numberOfFuel);
            Coin.addCoinCounter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        consumeFuel();
        GasStation gasStation = new GasStation(0, 0, 50, 50, new Image(new FileInputStream("images/gas-station.png")));
        Coin coin = new Coin(0, 0, 50, 50, new Image(new FileInputStream("images/coin.png")));
        addLabelToPane(mainPane,label,String.valueOf(Constants.SCORE),320,40);
        Timeline fuelAddTimeLine = new Timeline(new KeyFrame(Duration.seconds(10), actionEvent -> {
            try {
                addRandomGasStation(gasStation, mainPane, 5);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }));

        Timeline coinAddTimeLine = new Timeline(new KeyFrame(Duration.seconds(10), actionEvent -> {
            try {
                addRandomCoin(coin, mainPane, 7);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }));

        fuelAddTimeLine.setCycleCount(Timeline.INDEFINITE);
        coinAddTimeLine.setCycleCount(Timeline.INDEFINITE);
        coinAddTimeLine.play();
        fuelAddTimeLine.play();
        PlayerCar playerCar = new PlayerCar(180, 450, 50, 100, new Image(new FileInputStream("images/car1.png")));
        playerCar.addToPane(mainPane);
        generateEnemyCar = new GenerateEnemyCar(mainPane);
        generateEnemyCar.start();
        List<EnemyCar> enemyCars = generateEnemyCar.getEnemyCars();

        mainPane.requestFocus();
        mainPane.setOnKeyPressed(keyEvent -> {
            if (gameOver) return; // Do nothing if game is over
            if (keyEvent.getCode() == KeyCode.RIGHT && playerCar.getLayoutX() < 400) {
                playerCar.setX(playerCar.getX() + 10);
            }
            if (keyEvent.getCode() == KeyCode.LEFT && playerCar.getLayoutX() >= 100) {
                playerCar.setX(playerCar.getX() - 10);
            }
            if (keyEvent.getCode() == KeyCode.SPACE) {
                try {
                    fireBullet(playerCar);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create a Timeline to check for collisions
        collisionCheckTimeline = new Timeline(new KeyFrame(Duration.millis(60), actionEvent -> {
            for (int i = 0; i < enemyCars.size(); i++) {
                EnemyCar car = enemyCars.get(i);
                if (isCollision(playerCar, car)) {
                    removeHeart();
                    mainPane.getChildren().remove(car); // Remove enemy car from the pane
                    enemyCars.remove(i); // Remove enemy car from the list
                    i--; // Adjust index after removal
                    passedOrDestroyedCount++; // Increment counter
                    if (passedOrDestroyedCount >= 20) {
                        increaseEnemyCarSpeed(generateEnemyCar);
                    }
                    if (hearts.isEmpty()) {
                        Platform.runLater(() -> {
                            showGameOverAlert();
                            stopGame();
                        });
                        return; // Exit the method to prevent further processing
                    }
                }

                if (isCollideWithGas(playerCar, gasStation)) {
                    try {
                        Fuel.addFuel(numberOfFuel);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if(isCollideWithCoin(playerCar, coin )){
                    Constants.SCORE++;
                    addLabelToPane(mainPane,label,String.valueOf(Constants.SCORE),320,40);
                }
            }
            // Check for bullet and enemy car collisions
            for (int i = 0; i < bullets.size(); i++) {
                Fire bullet = bullets.get(i);
                for (int j = 0; j < enemyCars.size(); j++) {
                    EnemyCar car = enemyCars.get(j);
                    if (bullet.getBoundsInParent().intersects(car.getBoundsInParent())) {
                        try {
                            // Create an explosion at the position of the enemy car
                            Explosion explosion = new Explosion(
                                    (int) car.getLayoutX(),
                                    (int) car.getLayoutY(),
                                    70, // Adjust width as needed
                                    70, // Adjust height as needed
                                    new Image(new FileInputStream("images/explosion.png")) // Provide path to explosion image
                            );

                            // Add the explosion to the mainPane
                            mainPane.getChildren().add(explosion);

                            // Remove bullet, enemy car, and adjust index
                            mainPane.getChildren().remove(bullet);
                            mainPane.getChildren().remove(car);
                            bullets.remove(i);
                            enemyCars.remove(j);
                            i--;
                            j--;

                            // Schedule removal of explosion image after 500 milliseconds
                            Timeline removeExplosionTimeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
                                mainPane.getChildren().remove(explosion);
                            }));
                            removeExplosionTimeline.play();

                            passedOrDestroyedCount++; // Increment counter
                            if (passedOrDestroyedCount % 10 == 0) {
                                increaseEnemyCarSpeed(generateEnemyCar);
                            }

                            break; // Bullet hit an enemy car, no need to check other cars
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }));

        collisionCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        collisionCheckTimeline.play();
    }

    private void addRoad() throws FileNotFoundException {
        Image img = new Image(new FileInputStream("images/road.png"));
        ImageView backgroundImg = new ImageView(img);
        backgroundImg.setLayoutX(0);
        backgroundImg.setLayoutY(0);
        backgroundImg.setFitHeight(600);
        backgroundImg.setFitWidth(400);
        mainPane.getChildren().add(backgroundImg);
    }

    private void addHeart() throws FileNotFoundException {
        int numberOfLife = 3;

        for (int i = 0; i < numberOfLife; i++) {
            Image img = new Image(new FileInputStream("images/player-heart.png"));
            ImageView heartImg = new ImageView(img);
            heartImg.setLayoutX(i * 40 + 20);
            heartImg.setLayoutY(0);
            heartImg.setFitHeight(40);
            heartImg.setFitWidth(40);
            hearts.add(heartImg); // Add heart to the list
            mainPane.getChildren().add(heartImg);
        }
    }


    private void removeHeart() {
        if (!hearts.isEmpty()) {
            ImageView heart = hearts.remove(hearts.size() - 1);
            mainPane.getChildren().remove(heart);
        }
    }


    private boolean isCollision(PlayerCar playerCar, EnemyCar enemyCar) {
        if (playerCar.getBoundsInParent().intersects(enemyCar.getBoundsInParent())) {
            mainPane.getChildren().remove(enemyCar); // Remove enemy car from the pane
            return true;
        }
        return false;
    }

    private boolean isCollideWithGas(PlayerCar playerCar, GasStation gasStation) {
        if (playerCar.getBoundsInParent().intersects(gasStation.getBoundsInParent())) {
            mainPane.getChildren().remove(gasStation);
            return true;
        }
        return false;
    }

    private boolean isCollideWithCoin(PlayerCar playerCar, Coin coin){
        if (playerCar.getBoundsInParent().intersects(coin.getBoundsInParent())) {
            mainPane.getChildren().remove(coin);
            return true;
        }
        return false;
    }

    private void showGameOverAlert() {
        // Show Custom Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Game Over! You lost.");
        alert.showAndWait();
    }

    private void stopGame() {
        gameOver = true;
        collisionCheckTimeline.stop();

        try {
            saveScore(Constants.USERNAME, Constants.SCORE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void fireBullet(PlayerCar playerCar) throws FileNotFoundException {
        // Get the current position of the player car in the scene coordinate system
        Bounds playerBounds = playerCar.localToScene(playerCar.getBoundsInLocal());
        double bulletX = playerBounds.getMinX() + playerCar.getWidth() / 2 - 7.5; // Adjusting for larger bullet
        double bulletY = playerBounds.getMinY() - 40; // Adjusting for larger bullet

        Fire bullet = new Fire(
                (int) bulletX,
                (int) bulletY,
                15,
                30,
                new Image(new FileInputStream("images/bullet.png"))
        );
        bullets.add(bullet);
        mainPane.getChildren().add(bullet);

        Timeline bulletTimeline = new Timeline(new KeyFrame(Duration.millis(60), event -> {
            bullet.setLayoutY(bullet.getLayoutY() - 20);
            if (bullet.getLayoutY() < 0) {
                mainPane.getChildren().remove(bullet);
                bullets.remove(bullet);
            }
        }));
        bulletTimeline.setCycleCount(Timeline.INDEFINITE);
        bulletTimeline.play();
    }

    private void increaseEnemyCarSpeed(GenerateEnemyCar generateEnemyCar) {
        double newSpeed = generateEnemyCar.getSpeed() * 0.6;
        System.out.println(newSpeed);
        generateEnemyCar.setEnemyCarsSpeed(newSpeed);
    }

    private void addRandomGasStation(GasStation gasStation, Pane pane, int speed) throws FileNotFoundException {
        gasStation.showRandomlyOnScreen(mainPane);
        gasStation.moveDown(pane, speed);
    }

    private void addRandomCoin(Coin coin, Pane pane, int speed) throws FileNotFoundException {
        coin.showRandomlyOnScreen(mainPane);
        coin.moveDown(pane, speed);
    }

    private void saveScore(String username, int newScore) throws FileNotFoundException {

        File inputFile = new File("users.txt");
        List<String> lines = new ArrayList<>();

        // Read all lines into a list
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 3) {
                    String savedUsername = parts[0].trim().substring("Username: ".length());
                    if (savedUsername.equals(username)) {
                        line = String.format("Username: %s, Password: %s, Score: %d",
                                username,
                                parts[1].trim().substring("Password: ".length()),
                                newScore);
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addLabelToPane(Pane pane,Label label, String labelText, double x, double y) {
        label.setText(labelText);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setTextFill(Color.RED);
        label.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        if (!pane.getChildren().contains(label)) {
            pane.getChildren().add(label);
        }
    }
}
