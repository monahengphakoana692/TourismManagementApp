package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FullVideoView extends View {

    private final String title;
    private String videoUrl;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private StackPane mediaContainer;
    private VBox root;

    public FullVideoView(String title) {
        this.title = title;

        // Initialize UI components
        createUI();
    }

    private void createUI() {
        // Create the view content
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // Create media view placeholder
        mediaView = new MediaView();
        mediaView.setFitWidth(400);  // Larger size for full view
        mediaView.setFitHeight(180);
        //mediaView.setPreserveRatio(true);
        mediaView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        // Create video controls
        Button playButton = new Button("▶ Play");
        playButton.setStyle("-fx-background-radius: 5; -fx-font-size: 16; -fx-background-color: #4285f4; -fx-text-fill: white;");
        playButton.setOnAction(e -> togglePlayback(playButton));

        mediaContainer = new StackPane(mediaView, playButton);
        StackPane.setAlignment(playButton, Pos.BOTTOM_CENTER);

        // Create title label
        Label fullTitle = new Label(title);
        fullTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        fullTitle.setStyle("-fx-text-fill: #333;");

        root.getChildren().addAll(mediaContainer, fullTitle);
        setCenter(root);
    }

    private void togglePlayback(Button playButton) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playButton.setText("▶ Play");
            } else {
                mediaPlayer.play();
                playButton.setText("❚❚ Pause");
            }
        }
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;

        // Clean up previous media player if exists
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        // Initialize new media player
        try {
            Media media = new Media(getClass().getResource(videoUrl).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);


            // Auto-play when URL is set
            mediaPlayer.play();

            // Update the play button text
            Button playButton = (Button) mediaContainer.getChildren().get(1);
            playButton.setText("❚❚ Pause");

        } catch (Exception e) {
            System.err.println("Error loading media: " + e.getMessage());
            // Show error message
            Label errorLabel = new Label("Could not load video");
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
            root.getChildren().add(1, errorLabel);
        }
    }

    @Override
    protected void updateAppBar(AppBar appBar)
    {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
            // Clean up media player when going back
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            getAppManager().goHome();
        }));
        appBar.setTitleText("Video tour Details");
    }
}