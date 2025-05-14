package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebViews extends View {

    private final WebView webView;

    public WebViews() {
        webView = new WebView();
        webView.setPrefSize(800, 600);

        // Create Lesotho landmarks with multimedia
        List<Landmark> landmarks = new ArrayList<>();
        landmarks.add(new Landmark(
                "Thaba Bosiu",
                -29.3667,
                27.7167,
                "historic",
                getClass().getResource("/Thabana.mp3").toExternalForm(),
                getClass().getResource("/thabaBosiu.jpg").toExternalForm(),
                "#3498db",
                "Maseru, Lesotho",
                getClass().getResource("/BosiuV.mp4").toExternalForm()  // Local video file
        ));
        landmarks.add(new Landmark(
                "Maletsunyane Falls",
                -29.8377,
                28.5333,
                "waterfall",
                getClass().getResource("/Falls.mp3").toExternalForm(),
                getClass().getResource("/maleFalls.jpeg").toExternalForm(),
                "#2ecc71",
                "Semonkong, Lesotho",
                getClass().getResource("/FallVideo.mp4").toExternalForm()
        ));
        landmarks.add(new Landmark(
                "Katse Dam",
                -29.2606,
                28.4889,
                "dam",
                getClass().getResource("/KaseDam.mp3").toExternalForm(),
                getClass().getResource("/katse.png").toExternalForm(),
                "#e74c3c",
                "Katse, Lesotho",
                getClass().getResource("/katse.mp4").toExternalForm()
        ));

        loadLeafletMap(landmarks);

        StackPane root = new StackPane(webView);
        setCenter(root);
    }

    private void loadLeafletMap(List<Landmark> landmarks) {
        try (InputStream is = getClass().getResourceAsStream("/maps.html")) {
            if (is == null) {
                System.err.println("maps.html not found in resources!");
                return;
            }

            String html = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            String landmarksJson = convertLandmarksToJson(landmarks);
            html = html.replace("/*LANDMARKS_PLACEHOLDER*/", "var landmarks = " + landmarksJson + ";");

            webView.getEngine().loadContent(html);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading maps.html content");
        }
    }

    private String convertLandmarksToJson(List<Landmark> landmarks) {
        StringBuilder sb = new StringBuilder("[");
        for (Landmark landmark : landmarks) {
            sb.append(String.format(
                    "{" +
                            "\"name\":\"%s\"," +
                            "\"lat\":%f," +
                            "\"lng\":%f," +
                            "\"type\":\"%s\"," +
                            "\"audio\":\"%s\"," +
                            "\"image\":\"%s\"," +
                            "\"color\":\"%s\"," +
                            "\"location\":\"%s\"," +
                            "\"video\":\"%s\"" +
                            "},",
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
        // Getters
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

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lesotho Tour Guide");
        appBar.getActionItems().add(MaterialDesignIcon.EXPLORE.button(e -> System.out.println("Explore")));
    }
}