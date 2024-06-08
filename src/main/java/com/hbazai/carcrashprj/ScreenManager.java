package com.hbazai.carcrashprj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ScreenManager {
    private Pane mainPane;
    private List<ImageView> hearts; // List to store heart images
    private List<Fire> bullets; // List to store bullets
    private List<ImageView> fuels;
    private Timeline collisionCheckTimeline;
    private int passedOrDestroyedCount = 0; // Counter for passed or destroyed enemy cars
    private boolean gameOver = false;
    private GenerateEnemyCar generateEnemyCar;

    public ScreenManager(Pane mainPane) {
        this.mainPane = mainPane;
        this.hearts = new ArrayList<>(); // Initialize the list
        this.bullets = new ArrayList<>(); // Initialize the bullet list
        this.fuels = new ArrayList<>();
    }

    public void start() throws FileNotFoundException {
        addRoad();
        addHeart();
        addFuel();
        consumeFuel();
        GasStation gasStation = new GasStation(0, 0, 50, 50, new Image(new FileInputStream("images/gas-station.png")));
        Timeline fuelAddTimeLine = new Timeline(new KeyFrame(Duration.seconds(10), actionEvent -> {
            try {
                addRandomGasStation(gasStation,mainPane, 5); // Add a random gas station
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }));

        fuelAddTimeLine.setCycleCount(Timeline.INDEFINITE);
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

                if (isCollideWithGas(playerCar,gasStation )){
                    try {
                        addFuel();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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

    private void addFuel() throws FileNotFoundException {
        int numberOfFuel = 5;

        for (int i = 0; i < numberOfFuel; i++) {
            Image img = new Image(new FileInputStream("images/fuel.png"));
            ImageView fuelImg = new ImageView(img);
            fuelImg.setLayoutX(i * 20 + 280);
            fuelImg.setLayoutY(10);
            fuelImg.setFitHeight(20);
            fuelImg.setFitWidth(20);
            fuels.add(fuelImg); // Add fuel to the list
            mainPane.getChildren().add(fuelImg);
        }
    }

    private void removeHeart() {
        if (!hearts.isEmpty()) {
            ImageView heart = hearts.remove(hearts.size() - 1);
            mainPane.getChildren().remove(heart);
        }
    }

    private void consumeFuel() {
        Timeline fuelTimeLine = new Timeline(new KeyFrame(Duration.seconds(20), actionEvent -> {
            if (fuels.size() > 0) {
                ImageView fuel = fuels.remove(fuels.size() - 1);
                mainPane.getChildren().remove(fuel);
            } else {
                Platform.runLater(() -> {
                    showGameOverAlert();
                    stopGame();
                });
            }
        }));
        fuelTimeLine.setCycleCount(Timeline.INDEFINITE);
        fuelTimeLine.play();
    }

    private boolean isCollision(PlayerCar playerCar, EnemyCar enemyCar) {
        if (playerCar.getBoundsInParent().intersects(enemyCar.getBoundsInParent())) {
            mainPane.getChildren().remove(enemyCar); // Remove enemy car from the pane
            return true;
        }
        return false;
    }

    private boolean isCollideWithGas(PlayerCar playerCar, GasStation gasStation){
        if (playerCar.getBoundsInParent().intersects(gasStation.getBoundsInParent())){
            mainPane.getChildren().remove(gasStation);
            return true;
        }
        return false;
    }

    private void showGameOverAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Game Over! You lost.");
        alert.showAndWait();
    }

    private void stopGame() {
        gameOver = true;
        collisionCheckTimeline.stop();
        // Optionally stop other game-related timelines and disable any other game logic
    }

    private void fireBullet(PlayerCar playerCar) throws FileNotFoundException {
        // Get the current position of the player car in the scene coordinate system
        Bounds playerBounds = playerCar.localToScene(playerCar.getBoundsInLocal());
        double bulletX = playerBounds.getMinX() + playerCar.getWidth() / 2 - 7.5; // Adjusting for larger bullet
        double bulletY = playerBounds.getMinY() - 40; // Adjusting for larger bullet

        // Create a larger bullet with a red color
        Fire bullet = new Fire(
                (int) bulletX,
                (int) bulletY,
                15, // Increase bullet width
                30, // Increase bullet height
                new Image(new FileInputStream("images/bullet.png")) // Use red_bullet image
        );
        bullets.add(bullet);
        mainPane.getChildren().add(bullet);

        // Animate the bullet to move upwards more slowly
        Timeline bulletTimeline = new Timeline(new KeyFrame(Duration.millis(60), event -> {
            bullet.setLayoutY(bullet.getLayoutY() - 20); // Update bullet's layout Y position
            if (bullet.getLayoutY() < 0) {
                mainPane.getChildren().remove(bullet);
                bullets.remove(bullet);
            }
        }));
        bulletTimeline.setCycleCount(Timeline.INDEFINITE);
        bulletTimeline.play();
    }

    private void increaseEnemyCarSpeed(GenerateEnemyCar generateEnemyCar) {
        // Double the speed of new and existing enemy cars
        int newSpeed = generateEnemyCar.getSpeed() / 2;
        System.out.println(newSpeed);
        generateEnemyCar.setEnemyCarsSpeed(newSpeed);
    }

    private void addRandomGasStation(GasStation gasStation,Pane pane, int speed) throws FileNotFoundException {
        gasStation.showRandomlyOnScreen(mainPane);
        gasStation.moveDown(pane, speed);
    }
}
