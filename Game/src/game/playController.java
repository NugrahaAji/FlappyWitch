package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class playController {
    
    @FXML private StackPane rootPane;
    @FXML private Pane gamePane;
    @FXML private ImageView playerImage;
    @FXML private ImageView backgroundImage;
    @FXML private Label scoreLabel;
    @FXML private ImageView gameOverImage; 
    @FXML private ImageView restartButton;
    @FXML private ImageView menuButton;
    @FXML private Image topVineImage;
    @FXML private Image bottomVineImage;

    private ArrayList<ImageView> vineImages;
    private AnimationTimer gameLoop;
    private Random random;

    private double velocityX = -4;
    private double velocityY = 0;
    private double gravity = 1;
    private boolean gameOver = false;
    private double score = 0;

    private static final int BOARD_WIDTH = 360;
    private static final int BOARD_HEIGHT = 640;
    private static final int VINE_SPAWN_INTERVAL = 1500; // milliseconds
    private long lastVineSpawnTime = 0;

    public void initialize() {
        random = new Random();
        vineImages = new ArrayList<>();

        // Initialize player position
        playerImage.setLayoutX(BOARD_WIDTH / 8);
        playerImage.setLayoutY(BOARD_HEIGHT / 2);

        // Make root pane focusable
        rootPane.setFocusTraversable(true);

        // Set up key handler for the whole window
        setKeyHandler();

        // Initialize labels
        initializeLabels();

        // Set up button actions
        setButtonActions();

        // Start game
        startGame();
    }
    
    // Getter and Setter for vineImages
    public ArrayList<ImageView> getVineImages() {
        return vineImages;
    }

    public void setVineImages(ArrayList<ImageView> vineImages) {
        this.vineImages = vineImages;
    }

    // Getter and Setter for gameLoop
    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    public void setGameLoop(AnimationTimer gameLoop) {
        this.gameLoop = gameLoop;
    }

    // Getter and Setter for random
    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    // Getter and Setter for velocityX
    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    // Getter and Setter for velocityY
    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    // Getter and Setter for gravity
    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    // Getter and Setter for gameOver
    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    // Getter and Setter for lastVineSpawnTime
    public long getLastVineSpawnTime() {
        return lastVineSpawnTime;
    }

    public void setLastVineSpawnTime(long lastVineSpawnTime) {
        this.lastVineSpawnTime = lastVineSpawnTime;
    }

    // Getters for constants (optional since they're final)
    public static int getBoardWidth() {
        return BOARD_WIDTH;
    }

    public static int getBoardHeight() {
        return BOARD_HEIGHT;
    }

    public static int getVineSpawnInterval() {
        return VINE_SPAWN_INTERVAL;
    }
    
    
    private void setKeyHandler() {
        rootPane.setOnKeyPressed(e -> {
            if (!gameOver && e.getCode() == KeyCode.SPACE) {
                handleSpacePress();
            }
        });
    }

    private void startGame() {
        if (getGameLoop() == null) {
            setGameLoop(new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (now - getLastVineSpawnTime() > getVineSpawnInterval() * 1_000_000) {
                        placeVines();
                        setLastVineSpawnTime(now);
                    }

                    update();

                    scoreLabel.toFront();
                    if (isGameOver()) {
                        restartButton.setVisible(true);
                        menuButton.setVisible(true);
                        restartButton.toFront();
                        gameOverImage.toFront();
                        menuButton.toFront();
                    } else {
                        restartButton.setVisible(false);
                        menuButton.setVisible(false);
                        gameOverImage.setVisible(false);
                        scoreLabel.setLayoutY(10);
                        scoreLabel.setVisible(true);
                    }
                }
            });
        }
        getGameLoop().start();
    }

    private void update() {
        if (isGameOver()) {
            return; 
        }

        // Update player position
        setVelocityY(getVelocityY() + getGravity());
        double newY = playerImage.getLayoutY() + getVelocityY();
        playerImage.setLayoutY(Math.max(newY, 0));

        // Update vines
        for (int i = getVineImages().size() - 1; i >= 0; i--) {
            ImageView vine = getVineImages().get(i);
            vine.setLayoutX(vine.getLayoutX() + getVelocityX());

            // Check collision
            if (checkCollision(playerImage, vine)) {
                setGameOver(true);
                showGameOver();
                return;
            }

            // Remove vines that are off screen
            if (vine.getLayoutX() + vine.getFitWidth() < 0) {
                gamePane.getChildren().remove(vine);
                getVineImages().remove(i);
                // Increment score when passing vines
                if (i % 2 == 0) {
                    setScore(getScore() + 1);
                    updateScore();
                }
            }
        }

        // Check if player hits ground
        if (playerImage.getLayoutY() > getBoardHeight()) {
            setGameOver(true);
            showGameOver();
        }
    }

    @FXML
   
    private void updateScore() {
        scoreLabel.setText("Score: " + (int) getScore());
    }

    private void handleSpacePress() {
        setVelocityY(-9);
    }

    private boolean checkCollision(ImageView player, ImageView vine) {
        return player.getBoundsInParent().intersects(vine.getBoundsInParent());
    }

    private void placeVines() {
        int vineHeight = 512;
        int vineWidth = 64;
        int openingSpace = getBoardHeight() / 4;
        int randomVineY = (int) (0 - vineHeight / 4 - getRandom().nextDouble() * (vineHeight / 2));

        // Create top vine
        ImageView topVine = new ImageView(topVineImage);
        topVine.setFitWidth(vineWidth);
        topVine.setFitHeight(vineHeight);
        topVine.setLayoutX(getBoardWidth());
        topVine.setLayoutY(randomVineY);

        // Create bottom vine
        ImageView bottomVine = new ImageView(bottomVineImage);
        bottomVine.setFitWidth(vineWidth);
        bottomVine.setFitHeight(vineHeight);
        bottomVine.setLayoutX(getBoardWidth());
        bottomVine.setLayoutY(randomVineY + vineHeight + openingSpace);

        // Add vines to game
        gamePane.getChildren().addAll(topVine, bottomVine);
        getVineImages().add(topVine);
        getVineImages().add(bottomVine);
    }

    @FXML
    private void returnToMenu() {
        try {
            // Load the menu FXML
            Parent menuRoot = FXMLLoader.load(getClass().getResource("menu.fxml"));

            // Get the current stage
            Stage stage = (Stage) menuButton.getScene().getWindow();

            // Create a new scene with the menu view
            Scene menuScene = new Scene(menuRoot);

            // Set the scene to the stage
            stage.setScene(menuScene);

            // Set focus on the root pane of the new scene
            menuScene.getRoot().requestFocus();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for debugging
            System.err.println("Error loading game scene: " + e.getMessage());
        }
    }
}
