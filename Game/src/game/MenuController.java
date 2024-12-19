package game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuController implements Initializable {
    @FXML
    private ImageView buttonStart;
    @FXML
    private ImageView exitButton;

    private GameButton startButton;
    private GameButton exitBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up mouse click event for the start button
        startButton = new StartButton(buttonStart);
        exitBtn = new ExitButton(exitButton);
        buttonStart.setOnMouseClicked(this::handleStartButtonClick);
    }    
    
    private void handleStartButtonClick(MouseEvent event) {
    try {
        // Load the game FXML
        Parent gameRoot = FXMLLoader.load(getClass().getResource("play.fxml"));
        
        // Get the current stage
        Stage stage = (Stage) buttonStart.getScene().getWindow();
        
        // Create a new scene with the game view
        Scene gameScene = new Scene(gameRoot);
        
        // Set the scene to the stage
        stage.setScene(gameScene);
        
        // Set focus on the root pane of the new scene
        gameScene.getRoot().requestFocus();
        
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error loading game scene: " + e.getMessage());
    }
}
    @FXML
    private void handleExitButtonClick(MouseEvent event) {
        // Menutup aplikasi
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); // Menutup jendela
    }
}