package game;

import javafx.scene.image.ImageView;

public class ExitButton extends GameButton {

    public ExitButton(ImageView imageView) {
        super(imageView);
    }

    @Override
    protected void handleClick() {
        System.out.println("Exit Button Clicked!"); // Replace with actual exit logic
        System.exit(0); // Example exit logic
    }
}
