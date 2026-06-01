package GUI;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class SpeechBubbleHelper {

    public enum Placement{
        LEFT,
        RIGHT,
        ABOVE,
        BELOW
    }

    private final Node anchor;
    private final String clickText;
    private final String helpText;

    private Popup clickPopup;
    private Popup helpPopup;
    private PauseTransition clickDelay;
    private PauseTransition helpAutoHideDelay;

    private boolean helpShowing = false;
    private boolean flipped = true;

    private double clickXAdjustment = 0;
    private double clickYAdjustment = 0;

    private double helpXAdjustment = 0;
    private double helpYAdjustment = 0;

    private Placement clickPlacement = Placement.LEFT;
    private Placement helpPlacement = Placement.LEFT;

    private double gap = 8;

    public SpeechBubbleHelper(Node anchor, String clickText, String helpText){
        this.anchor = anchor;
        this.clickText = clickText;
        this.helpText = helpText;
    }

    public void setClickAdjustment(double x, double y) {
        this.clickXAdjustment = x;
        this.clickYAdjustment = y;
    }

    public void setHelpAdjustment(double x, double y) {
        this.helpXAdjustment = x;
        this.helpYAdjustment = y;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public void showClickBubble() {
        if (clickPopup == null) {
            clickPopup = createSpeechPopup(clickText, 115, 60, 13, true);
        }

        showPopup(clickPopup, 115, 60, clickPlacement, clickXAdjustment, clickYAdjustment);
    }

    public void showClickBubbleAfterDelay(double seconds){
        if(clickDelay != null){
            clickDelay.stop();
        }

        clickDelay = new PauseTransition(Duration.seconds(seconds));
        clickDelay.setOnFinished(event -> {
            if(!helpShowing){
                showClickBubble();
            }
        });
        clickDelay.play();
    }

    public void toggleHelpBubble() {
        if (clickDelay != null) {
            clickDelay.stop();
        }

        if (helpAutoHideDelay != null) {
            helpAutoHideDelay.stop();
        }

        if (helpShowing) {
            helpShowing = false;
            hideHelpBubble();
            return;
        }

        helpShowing = true;
        hideClickBubble();

        if (helpPopup == null) {
            helpPopup = createSpeechPopup(helpText, 260, 170, 12, false);
        }

        showPopup(helpPopup, 260, 170, helpPlacement, helpXAdjustment, helpYAdjustment);
        hideHelpBubbleAfterDelay(15);
    }

    private void hideHelpBubbleAfterDelay(double seconds){
        helpAutoHideDelay = new PauseTransition(Duration.seconds(seconds));
        helpAutoHideDelay.setOnFinished(event -> {
            helpShowing = false;
            hideHelpBubble();
        });
        helpAutoHideDelay.play();
    }

    public void hideAll() {
        helpShowing = false;

        if (clickDelay != null) {
            clickDelay.stop();
        }

        if (helpAutoHideDelay != null) {
            helpAutoHideDelay.stop();
        }

        hideClickBubble();
        hideHelpBubble();
    }

    private void hideClickBubble() {
        if (clickPopup != null) {
            clickPopup.hide();
        }
    }

    private void hideHelpBubble() {
        if (helpPopup != null) {
            helpPopup.hide();
        }
    }

    private void showPopup(Popup popup, double width, double height, Placement placement, double xAdjustment, double yAdjustment){
        Bounds bounds = anchor.localToScreen(anchor.getBoundsInLocal());

        if (bounds == null) {
            return;
        }

        double x = bounds.getMinX();
        double y = bounds.getMinY();

        switch (placement) {
            case LEFT:
                x = bounds.getMinX() - width - gap;
                y = bounds.getMinY() + (bounds.getHeight() - height) / 2;
                break;
            case RIGHT:
                x = bounds.getMaxX() + gap;
                y = bounds.getMinY() + (bounds.getHeight() - height) / 2;
                break;
            case ABOVE:
                x = bounds.getMinX() + (bounds.getWidth() - width) / 2;
                y = bounds.getMinY() - height - gap;
                break;
            case BELOW:
                x = bounds.getMinX() + (bounds.getWidth() - width) / 2;
                y = bounds.getMaxY() + gap;
                break;
        }

        popup.show(anchor, x + xAdjustment, y + yAdjustment);
    }

    private Popup createSpeechPopup(String text, double width, double height, double fontSize, boolean centerText) {
        Popup popup = new Popup();

        popup.setHideOnEscape(true);

        Pane pane = new Pane();
        pane.setPrefSize(width, height);

        ImageView bubbleImage = new ImageView(
                new Image(getClass().getResource("/Images/speechBubble.png").toExternalForm())
        );

        bubbleImage.setFitWidth(width);
        bubbleImage.setFitHeight(height);
        bubbleImage.setPreserveRatio(false);
        bubbleImage.setScaleX(flipped ? -1 : 1);

        Label bubbleText = new Label(text);
        bubbleText.setWrapText(true);
        bubbleText.setStyle(
                "-fx-text-fill: #3A5870;" +
                        "-fx-font-family: Georgia;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: " + fontSize + "px;"
        );

        if (centerText) {
            bubbleText.setLayoutX(width * 0.12);
            bubbleText.setLayoutY(height * 0.14);
            bubbleText.setPrefWidth(width * 0.65);
            bubbleText.setPrefHeight(height * 0.55);
            bubbleText.setAlignment(Pos.CENTER);
        } else {
            if (flipped) {
                bubbleText.setLayoutX(width * 0.12);
            } else {
                bubbleText.setLayoutX(width * 0.18);
            }

            bubbleText.setLayoutY(height * 0.20);
            bubbleText.setPrefWidth(width * 0.65);
            bubbleText.setAlignment(Pos.TOP_LEFT);
        }

        pane.getChildren().addAll(bubbleImage, bubbleText);
        popup.getContent().add(pane);

        return popup;
    }

    public void setClickPlacement(Placement placement) {
        this.clickPlacement = placement;
    }

    public void setHelpPlacement(Placement placement) {
        this.helpPlacement = placement;
    }
}
