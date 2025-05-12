package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;
import javafx.scene.shape.Circle;
import java.util.HashMap;
import java.util.Map;

public class WebViewTry extends View {
    private final StackPane root = new StackPane();
    private final Map<String, MediaPlayer> mediaPlayers = new HashMap<>();
    private ImageView mapImageView;
    private Canvas landmarksCanvas;

    // Map dimensions
    private static final double MAP_WIDTH = 800;
    private static final double MAP_HEIGHT = 600;

    // Lesotho coordinates
    private static final double MIN_LON = 27.0;
    private static final double MAX_LON = 29.5;
    private static final double MIN_LAT = -30.8;
    private static final double MAX_LAT = -28.5;

    // Landmark data
    private final Map<String, MapLocation> locations = new HashMap<>();

    public WebViewTry() {
        initialize();
    }

    private void initialize() {
        root.setStyle("-fx-background-color: #f0f0f0;");
        setCenter(root);

        // Load map image
        try {
            mapImageView = new ImageView(new Image(getClass().getResourceAsStream("/katse.png")));
            mapImageView.setPreserveRatio(true);
            mapImageView.setFitWidth(MAP_WIDTH);

            // Create canvas for landmarks
            landmarksCanvas = new Canvas(MAP_WIDTH, MAP_HEIGHT);

            root.getChildren().addAll(mapImageView, landmarksCanvas);

            // Add landmarks
            addLandmarks();
            drawLandmarks();
            setupInteractions();

        } catch (Exception e) {
            System.err.println("Error loading map image: " + e.getMessage());
            // Fallback - create empty map with just landmarks
            landmarksCanvas = new Canvas(MAP_WIDTH, MAP_HEIGHT);
            root.getChildren().add(landmarksCanvas);
            drawLesothoOutline();
            addLandmarks();
            drawLandmarks();
            setupInteractions();
        }
    }

    private void drawLesothoOutline() {
        GraphicsContext gc = landmarksCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        gc.setFill(Color.LIGHTGREEN);
        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(2);

        double[] xPoints = {lonToX(27.0), lonToX(29.5), lonToX(29.5), lonToX(27.0)};
        double[] yPoints = {latToY(-28.5), latToY(-28.5), latToY(-30.8), latToY(-30.8)};

        gc.fillPolygon(xPoints, yPoints, 4);
        gc.strokePolygon(xPoints, yPoints, 4);
    }

    private void addLandmarks() {
        // Corrected image paths (assuming .jpg extension)
        addLandmark("Katse Dam", -29.335, 28.507,
                "/FallVideo.mp4", "/Avani.jpg", "/Falls.mp3");
        addLandmark("Maletsunyane Falls", -29.892, 28.093,
                "/FallVideo.mp4", "/maleFalls.jpg", "/Falls.mp3");
        addLandmark("Thaba-Bosiu", -29.366, 27.707,
                null, "/thabaBosiu.jpg", null);
        addLandmark("Sani Pass", -29.586, 29.286,
                null, "/saniPass.jpg", null);
        addLandmark("Maseru", -29.310, 27.480,
                null, "/maseru.jpg", null);
    }

    private void addLandmark(String name, double lat, double lon,
                             String videoPath, String imagePath, String audioPath) {
        double screenX = lonToX(lon);
        double screenY = latToY(lat);

        locations.put(name, new MapLocation(name, lat, lon, screenX, screenY,
                videoPath, imagePath, audioPath));

        // Add individual tooltips for each landmark
        Circle marker = new Circle(screenX, screenY, 8, Color.TRANSPARENT);
        Tooltip.install(marker, new Tooltip(name));
        root.getChildren().add(marker);
    }

    private void drawLandmarks() {
        GraphicsContext gc = landmarksCanvas.getGraphicsContext2D();

        for (MapLocation loc : locations.values()) {
            // Draw marker
            gc.setFill(Color.RED);
            gc.fillOval(loc.screenX - 8, loc.screenY - 8, 16, 16);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeOval(loc.screenX - 8, loc.screenY - 8, 16, 16);

            // Draw name label
            gc.setFill(Color.BLACK);
            gc.fillText(loc.name, loc.screenX + 10, loc.screenY);
        }
    }

    private double lonToX(double longitude) {
        return ((longitude - MIN_LON) / (MAX_LON - MIN_LON)) * MAP_WIDTH;
    }

    private double latToY(double latitude) {
        return MAP_HEIGHT - ((latitude - MIN_LAT) / (MAX_LAT - MIN_LAT)) * MAP_HEIGHT;
    }

    private void setupInteractions() {
        landmarksCanvas.setOnMouseMoved(e -> {
            boolean overLandmark = false;
            for (MapLocation loc : locations.values()) {
                double dx = e.getX() - loc.screenX;
                double dy = e.getY() - loc.screenY;
                if (Math.sqrt(dx*dx + dy*dy) < 10) {
                    overLandmark = true;
                    break;
                }
            }
            landmarksCanvas.setCursor(overLandmark ? Cursor.HAND : Cursor.DEFAULT);
        });

        landmarksCanvas.setOnMouseClicked(e -> {
            for (MapLocation loc : locations.values()) {
                double dx = e.getX() - loc.screenX;
                double dy = e.getY() - loc.screenY;
                if (Math.sqrt(dx*dx + dy*dy) < 10) {
                    showLocationContent(loc);
                    break;
                }
            }
        });
    }

    private void showLocationContent(MapLocation loc) {
        // Clear previous media
        root.getChildren().removeIf(node ->
                node instanceof MediaView ||
                        (node instanceof ImageView && node != mapImageView));

        // Stop all media players
        mediaPlayers.values().forEach(MediaPlayer::stop);
        mediaPlayers.clear();

        // Show location image
        if (loc.imagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(loc.imagePath));
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(250);
                imageView.setTranslateX(20);
                imageView.setTranslateY(20);
                root.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Error loading location image: " + loc.imagePath);
            }
        }

        // Play video
        if (loc.videoPath != null) {
            try {
                Media media = new Media(getClass().getResource(loc.videoPath).toString());
                MediaPlayer player = new MediaPlayer(media);
                mediaPlayers.put(loc.name + "_video", player);

                MediaView mediaView = new MediaView(player);
                mediaView.setPreserveRatio(true);
                mediaView.setFitWidth(300);
                mediaView.setTranslateX(20);
                mediaView.setTranslateY(280);
                root.getChildren().add(mediaView);

                player.play();
                player.setCycleCount(MediaPlayer.INDEFINITE);
            } catch (Exception e) {
                System.err.println("Error loading video: " + loc.videoPath);
            }
        }

        // Play audio
        if (loc.audioPath != null) {
            try {
                Media audio = new Media(getClass().getResource(loc.audioPath).toString());
                MediaPlayer player = new MediaPlayer(audio);
                mediaPlayers.put(loc.name + "_audio", player);
                player.play();
                player.setCycleCount(MediaPlayer.INDEFINITE);
            } catch (Exception e) {
                System.err.println("Error loading audio: " + loc.audioPath);
            }
        }
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> System.out.println("Menu")));
        appBar.setTitleText("Lesotho Tourism Map");
        appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> System.out.println("Search")));
    }

    private static class MapLocation {
        final String name;
        final double lat, lon;
        final double screenX, screenY;
        final String videoPath, imagePath, audioPath;

        public MapLocation(String name, double lat, double lon,
                           double screenX, double screenY,
                           String videoPath, String imagePath, String audioPath) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
            this.screenX = screenX;
            this.screenY = screenY;
            this.videoPath = videoPath;
            this.imagePath = imagePath;
            this.audioPath = audioPath;
        }
    }
}