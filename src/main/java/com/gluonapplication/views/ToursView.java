package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ToursView extends View {
    private StackPane root;
    private Map<String, MediaPlayer> audioPlayers = new HashMap<>();
    private Map<String, MediaPlayer> videoPlayers = new HashMap<>();
    private Stage primaryStage;

    public ToursView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize();
    }

    private void initialize() {
        // Load map image
        Image mapImage = new Image(getClass().getResourceAsStream("/katse.png"));
        ImageView mapView = new ImageView(mapImage);
        mapView.setPreserveRatio(true);

        root = new StackPane(mapView);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Set up the scene
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Interactive Tours Map");

        // Add sample landmarks (coordinates should match your map)
        addLandmark(150, 200, "Eiffel Tower",
                "/Avanii.mp4",
                "/Avani.jpeg",
                "/Avanini.mp3");

        addLandmark(400, 180, "Colosseum",
                "/Avanii.mp4",
                "/Avani.jpeg",
                "/Avanini.mp3");

        addLandmark(600, 250, "Great Pyramid",
                "/Avanii.mp4",
                "/Avani.jpeg",
                "/Avanini.mp3");

        // Make sure resources are properly released when window closes
        primaryStage.setOnCloseRequest(event -> {
            cleanupMediaPlayers();
        });
    }

    private void addLandmark(double x, double y, String name,
                             String videoPath, String imagePath, String audioPath) {
        // Create hotspot (semi-transparent for better UX)
        Circle hotspot = new Circle(x, y, 20, Color.rgb(200, 0, 0, 0.3));
        hotspot.setStroke(Color.rgb(150, 0, 0, 0.7));
        hotspot.setStrokeWidth(2);
        hotspot.setCursor(Cursor.HAND);

        // Add tooltip
        Tooltip.install(hotspot, new Tooltip(name));

        // Set hover behavior
        hotspot.setOnMouseEntered(e -> showLandmarkContent(name, videoPath, imagePath, audioPath, x, y));
        hotspot.setOnMouseExited(e -> hideLandmarkContent(name));

        root.getChildren().add(hotspot);
    }

    private void showLandmarkContent(String name, String videoPath, String imagePath,
                                     String audioPath, double x, double y) {
        // Show image (if available)
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(250);
                imageView.setTranslateX(x + 40);
                imageView.setTranslateY(y - 40);
                imageView.setUserData("image_" + name);
                root.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Error loading image: " + imagePath);
            }
        }

        // Play video (if available)
        if (videoPath != null && !videoPath.isEmpty()) {
            try {
                Media media = new Media(getClass().getResource(videoPath).toString());
                MediaPlayer player = new MediaPlayer(media);
                videoPlayers.put(name, player);

                MediaView mediaView = new MediaView(player);
                mediaView.setPreserveRatio(true);
                mediaView.setFitWidth(300);
                mediaView.setTranslateX(x + 50);
                mediaView.setTranslateY(y - 50);
                mediaView.setUserData("video_" + name);

                root.getChildren().add(mediaView);
                player.play();

                // Loop the video
                player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
            } catch (Exception e) {
                System.err.println("Error loading video: " + videoPath);
            }
        }

        // Play audio (if available)
        if (audioPath != null && !audioPath.isEmpty()) {
            try {
                Media audio = new Media(getClass().getResource(audioPath).toString());
                MediaPlayer audioPlayer = new MediaPlayer(audio);
                audioPlayers.put(name, audioPlayer);
                audioPlayer.play();

                // Loop the audio
                audioPlayer.setOnEndOfMedia(() -> audioPlayer.seek(Duration.ZERO));
            } catch (Exception e) {
                System.err.println("Error loading audio: " + audioPath);
            }
        }
    }

    private void hideLandmarkContent(String name) {
        // Remove image and video views
        root.getChildren().removeIf(node ->
                node.getUserData() != null &&
                        (node.getUserData().equals("image_" + name) ||
                                node.getUserData().equals("video_" + name)));

        // Stop and remove audio player
        if (audioPlayers.containsKey(name)) {
            audioPlayers.get(name).stop();
            audioPlayers.remove(name);
        }

        // Stop video player (but keep reference for reuse)
        if (videoPlayers.containsKey(name)) {
            videoPlayers.get(name).pause();
        }
    }

    private void cleanupMediaPlayers() {
        // Stop all media players when closing
        audioPlayers.values().forEach(MediaPlayer::dispose);
        videoPlayers.values().forEach(MediaPlayer::dispose);
        audioPlayers.clear();
        videoPlayers.clear();
    }

    public StackPane getView() {
        return root;
    }
}