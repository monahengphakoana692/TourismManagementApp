package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import javafx.concurrent.Worker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebViews extends View {

    private final WebView webView;

    public WebViews() {
        webView = new WebView();
        webView.setPrefSize(800, 600);

        // Initialize error handlers
        initializeWebViewHandlers();

        // Create and load landmarks
        List<Landmark> landmarks = createLandmarks();
        loadLeafletMap(landmarks);

        StackPane root = new StackPane(webView);
        setCenter(root);
    }

    private void initializeWebViewHandlers() {
        // General WebView error handler
        webView.getEngine().setOnError(event -> {
            String errorType = event.getException() != null ?
                    " (" + event.getException().getClass().getSimpleName() + ")" : "";
            showAlert("Web Error", "A web content error occurred" + errorType + ": " + event.getMessage());
        });

        // JavaScript alert handler
        webView.getEngine().setCreatePopupHandler(config -> null);

        // JavaScript confirmation handler
        webView.getEngine().setConfirmHandler(message -> {
            showAlert("Confirmation", message);
            return true;
        });

        // Initialize JavaScript-Java bridge when page loads
        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("jsBridge", new JSBridge());
            }
        });
    }

    private List<Landmark> createLandmarks() {
        List<Landmark> landmarks = new ArrayList<>();

        // Validate resources exist before adding landmarks
        try {
            landmarks.add(new Landmark(
                    "Thaba Bosiu",
                    -29.3667,
                    27.7167,
                    "historic",
                    validateResource("/Thabana.mp3"),
                    validateResource("/thabaBosiu.jpg"),
                    "#3498db",
                    "Maseru, Lesotho",
                    validateResource("/BosiuV.mp4")
            ));

            landmarks.add(new Landmark(
                    "Maletsunyane Falls",
                    -29.8377,
                    28.5333,
                    "waterfall",
                    validateResource("/Falls.mp3"),
                    validateResource("/maleFalls.jpeg"),
                    "#2ecc71",
                    "Semonkong, Lesotho",
                    validateResource("/FallVideo.mp4")
            ));

            landmarks.add(new Landmark(
                    "Katse Dam",
                    -29.2606,
                    28.4889,
                    "dam",
                    validateResource("/KaseDam.mp3"),
                    validateResource("/katse.png"),
                    "#e74c3c",
                    "Katse, Lesotho",
                    validateResource("/katse.mp4")
            ));
        } catch (Exception e) {
            showAlert("Initialization Error", "Failed to load application resources: " + e.getMessage());
            e.printStackTrace();
        }

        return landmarks;
    }

    private String validateResource(String path) throws Exception {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            throw new Exception("Resource not found: " + path);
        }
        return resource.toExternalForm();
    }

    private void loadLeafletMap(List<Landmark> landmarks) {
        try (InputStream is = getClass().getResourceAsStream("/maps.html")) {
            if (is == null) {
                throw new Exception("Map template (maps.html) not found in resources");
            }

            String html = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            String landmarksJson = convertLandmarksToJson(landmarks);
            html = html.replace("/*LANDMARKS_PLACEHOLDER*/", "var landmarks = " + landmarksJson + ";");

            webView.getEngine().loadContent(html);
        } catch (Exception e) {
            showAlert("Loading Error", "Failed to load map content: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String convertLandmarksToJson(List<Landmark> landmarks) {
        StringBuilder sb = new StringBuilder("[");
        for (Landmark landmark : landmarks) {
            sb.append(String.format(
                    "{\"name\":\"%s\",\"lat\":%f,\"lng\":%f,\"type\":\"%s\"," +
                            "\"audio\":\"%s\",\"image\":\"%s\",\"color\":\"%s\"," +
                            "\"location\":\"%s\",\"video\":\"%s\"},",
                    landmark.getName(),
                    landmark.getLatitude(),
                    landmark.getLongitude(),
                    landmark.getType(),
                    landmark.getAudio(),
                    landmark.getImage(),
                    landmark.getColor(),
                    landmark.getLocation(),
                    landmark.getVideo()
            ));
        }
        if (!landmarks.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lesotho Tour Guide");
        appBar.getActionItems().add(MaterialDesignIcon.EXPLORE.button(e -> System.out.println("Explore")));
    }

    // JavaScript-Java bridge class
    public class JSBridge {
        public void showAlert(String title, String message) {
            WebViews.this.showAlert(title, message);
        }

        public void logError(String type, String message) {
            System.err.println(type + " Error: " + message);
        }
    }

    // Landmark data class
    private static class Landmark {
        private final String name;
        private final double latitude;
        private final double longitude;
        private final String type;
        private final String audio;
        private final String image;
        private final String color;
        private final String location;
        private final String video;

        public Landmark(String name, double latitude, double longitude,
                        String type, String audio, String image, String color,
                        String location, String video) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.type = type;
            this.audio = audio;
            this.image = image;
            this.color = color;
            this.location = location;
            this.video = video;
        }

        public String getName() { return name; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getType() { return type; }
        public String getAudio() { return audio; }
        public String getImage() { return image; }
        public String getColor() { return color; }
        public String getLocation() { return location; }
        public String getVideo() { return video; }
    }
}