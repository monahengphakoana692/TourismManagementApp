package com.gluonapplication.views;


import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.location.LocationDataSource.Location;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapViews extends View {

    private final MapView mapView = new MapView();
    private final ObservableList<Hotspot> hotspots = FXCollections.observableArrayList();

    public MapViews() {
        initializeMap();
        loadHotspots();
        createHotspotLayer();
        setCenter(new StackPane(mapView));
    }

    private void initializeMap() {
        mapView.setCenter(new MapPoint(-29.3167, 27.4833)); // Maseru
        mapView.setZoom(16);
    }

    private void loadHotspots() {
        hotspots.add(new Hotspot("1", "Avani Maseru",
                "The most exciting hotel in lesotho",
                new MapPoint(100, 100),
                "/Avani.jpeg",
                "/Avanini.mp3",
                null));

        hotspots.add(new Hotspot("2", "Maletsunyane Falls",
                "Highest single-drop waterfall in Southern Africa",
                new MapPoint(-29.986, 28.987),
                "/MaleFalls.jpeg",
                "/Falls.mp3",
                "/FallVideo.mp4"));

        hotspots.add(new Hotspot("3", "Katse Dam",
                "Water facility in lesotho",
                new MapPoint(-29.986, 28.987),
                "/katse.png",
                "/Falls.mp3",
                "/katse.mp4"));
    }

    private void createHotspotLayer() {
        MapLayer layer = new MapLayer() {
            @Override
            protected void layoutLayer() {
                getChildren().clear();

                // For testing, use simple local coordinates (x,y)
                // These will place markers at fixed positions on the map component
                double[][] testCoordinates = {
                        {100, 100},  // x, y position for first marker
                        {200, 150},  // second marker
                        {300, 200}   // third marker
                };

                for (int i = 0; i < hotspots.size(); i++) {
                    Hotspot hotspot = hotspots.get(i);
                    Node marker = createMarker(hotspot);
                    getChildren().add(marker);

                    // Use test coordinates instead of geographic ones
                    double x = testCoordinates[i][0];
                    double y = testCoordinates[i][1];

                    // Position the marker (adjust offset as needed)
                    marker.setTranslateX(x - 15);
                    marker.setTranslateY(y - 15);

                    // Handle click event
                    marker.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
                            showHotspotContent(hotspot);
                        }
                    });
                }
            }
        };
        mapView.addLayer(layer);
    }



    private Location getCurrentLocation() {
        try {
            // 1. Get coordinates from IP-API
            String json = new Scanner(
                    new URL("http://ip-api.com/json").openStream(), "UTF-8")
                    .useDelimiter("\\A").next();

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            double lat = obj.get("lat").getAsDouble();
            double lon = obj.get("lon").getAsDouble();

            // 2. Create ArcGIS Point (WGS84 coordinate system)
            Point point = new Point(lon, lat, SpatialReferences.getWgs84());

            // 3. Create ArcGIS Location (simplest constructor)
            return new Location(point, Double.NaN, Double.NaN, Double.NaN, false);

        } catch (Exception e) {
            System.err.println("Failed to get location: " + e.getMessage());

            // Return default location (New York) if failed
            Point defaultPoint = new Point(-29.9028, 28.0944, SpatialReferences.getWgs84());
            return new Location(defaultPoint, Double.NaN, Double.NaN, Double.NaN, false);
        }
    }

    private Node createMarker(Hotspot hotspot) {
        // Create a stack pane to hold both image and label
        StackPane markerPane = new StackPane();

        try {
            URL imageUrl = getClass().getResource(hotspot.getImagePath());
            if (imageUrl != null) {
                ImageView marker = new ImageView(new Image(imageUrl.toExternalForm()));
                marker.setFitWidth(30);
                marker.setFitHeight(30);

                // Create label for the name
                Label nameLabel = new Label(hotspot.getTitle());
                nameLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-background-color: rgba(255,255,255,0.7);");
                nameLabel.setTranslateY(20); // Position below the icon

                markerPane.getChildren().addAll(marker, nameLabel);
                Tooltip.install(markerPane, new Tooltip(hotspot.getTitle()));
                return markerPane;
            }
        } catch (Exception e) {
            System.err.println("Error loading marker image: " + e.getMessage());
        }

        // Fallback marker with name
        Circle circle = new Circle(15, Color.RED);
        Label nameLabel = new Label(hotspot.getTitle());
        nameLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        nameLabel.setTranslateY(20);

        markerPane.getChildren().addAll(circle, nameLabel);
        Tooltip.install(markerPane, new Tooltip(hotspot.getTitle()));
        return markerPane;
    }

    private VBox createTooltipContent(Hotspot hotspot) {
        VBox tooltipContent = new VBox(5);
        tooltipContent.setPadding(new Insets(5));

        Label titleLabel = new Label(hotspot.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label descLabel = new Label(hotspot.getDescription());
        descLabel.setStyle("-fx-font-size: 12px;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(200);

        tooltipContent.getChildren().addAll(titleLabel, descLabel);
        return tooltipContent;
    }

    private void showHotspotContent(Hotspot hotspot) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitleText(hotspot.getTitle());

        List<VBox> contentHolder = new ArrayList<>(); // <== move it here to refresh each time
        int[] currentIndex = {0};

        // First page: image + audio
        VBox contentPage1 = new VBox(10);
        contentPage1.setPadding(new Insets(10));

        // Image
        URL imageUrl = getClass().getResource(hotspot.getImagePath());
        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);
            contentPage1.getChildren().add(imageView);
        }

        // Audio
        if (hotspot.getAudioPath() != null) {
            URL audioUrl = getClass().getResource(hotspot.getAudioPath());
            if (audioUrl != null) {
                Media audioMedia = new Media(audioUrl.toExternalForm());
                MediaPlayer audioPlayer = new MediaPlayer(audioMedia);
                MediaView audioView = new MediaView(audioPlayer);

                Button playAudio = new Button("▶ Play Audio");
                Button pauseAudio = new Button("⏸ Pause Audio");
                playAudio.setOnAction(e -> audioPlayer.play());
                pauseAudio.setOnAction(e -> audioPlayer.pause());
                HBox audioControls = new HBox(10, playAudio, pauseAudio);

                contentPage1.getChildren().addAll(audioView, audioControls);
            } else {
                contentPage1.getChildren().add(new Label("Audio not available"));
            }
        }

        contentPage1.getChildren().add(new Label(hotspot.getDescription()));
        contentHolder.add(contentPage1);

        // Second page: video
        if (hotspot.getVideoPath() != null) {
            VBox contentPage2 = new VBox(10);
            contentPage2.setPadding(new Insets(10));

            URL videoUrl = getClass().getResource(hotspot.getVideoPath());
            if (videoUrl != null) {
                Media videoMedia = new Media(videoUrl.toExternalForm());
                MediaPlayer videoPlayer = new MediaPlayer(videoMedia);
                MediaView videoView = new MediaView(videoPlayer);

                videoView.setFitWidth(300);
                videoView.setPreserveRatio(true);

                Button playVideo = new Button("▶ Play Video");
                Button pauseVideo = new Button("⏸ Pause Video");
                playVideo.setOnAction(e -> videoPlayer.play());
                pauseVideo.setOnAction(e -> videoPlayer.pause());
                HBox videoControls = new HBox(10, playVideo, pauseVideo);

                contentPage2.getChildren().addAll(videoView, videoControls);
            } else {
                contentPage2.getChildren().add(new Label("Video not available"));
            }

            contentHolder.add(contentPage2);
        }

        // Add navigation
        Button backBtn = new Button("← Back");
        Button nextBtn = new Button("Next →");

        HBox navButtons = new HBox(10, backBtn, nextBtn);
        VBox wrapper = new VBox(10, contentHolder.get(currentIndex[0]), navButtons);

        backBtn.setDisable(true);
        if (contentHolder.size() <= 1) nextBtn.setDisable(true);

        backBtn.setOnAction(e -> {
            wrapper.getChildren().set(0, contentHolder.get(--currentIndex[0]));
            backBtn.setDisable(currentIndex[0] == 0);
            nextBtn.setDisable(false);
        });

        nextBtn.setOnAction(e -> {
            wrapper.getChildren().set(0, contentHolder.get(++currentIndex[0]));
            nextBtn.setDisable(currentIndex[0] == contentHolder.size() - 1);
            backBtn.setDisable(false);
        });

        dialog.setContent(wrapper);
        dialog.showAndWait();
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lesotho Tour Guide");
        appBar.getActionItems().add(MaterialDesignIcon.EXPLORE.button(e -> System.out.println("Explore")));


    }
}


