package game;

import javafx.scene.image.ImageView;

public class StartButton extends GameButton {

    public StartButton(ImageView imageView) {
        super(imageView);
    }

    @Override
    protected void handleClick() {
        System.out.println("Start Button Clicked!"); // Replace with actual start game logic
    }
}
