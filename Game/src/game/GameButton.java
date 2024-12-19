package game;

import javafx.scene.image.ImageView;

public abstract class GameButton {
    protected ImageView imageView;

    public GameButton(ImageView imageView) {
        this.imageView = imageView;
        setUpClickHandler();
    }

    protected abstract void handleClick();

    private void setUpClickHandler() {
        imageView.setOnMouseClicked(e -> handleClick());
    }

    public ImageView getImageView() {
        return imageView;
    }
}
