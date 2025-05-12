package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

public class WebViews extends View {

    private final WebView webView;
    private static final String GMAPS_API_KEY = "AIzaSyAW5Xeqj0f17oA1--K59t1plRbW44cvFlY";
    private static final double AVANI_LAT = -29.2;
    private static final double AVANI_LON = 27.5;

    public WebViews() {
        webView = new WebView();
        webView.setPrefSize(800, 600);

        // Load Google Maps centered on Avani location
        loadGoogleMaps();

        setCenter(new StackPane(webView));
    }

    private void loadGoogleMaps() {
        // Build the Google Maps URL without string formatting issues
        String gmapsUrl = "https://www.google.com/maps/embed/v1/view" +
                "?key=" + GMAPS_API_KEY +
                "&center=" + AVANI_LAT + "," + AVANI_LON +
                "&zoom=14" +
                "&maptype=roadmap";

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body, html, #map-container {" +
                "    margin: 0;" +
                "    padding: 0;" +
                "    width: 100%;" +
                "    height: 100%;" +
                "    overflow: hidden;" +
                "}" +
                "iframe {" +
                "    border: none;" +
                "    width: 100%;" +
                "    height: 100%;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div id=\"map-container\">" +
                "<iframe src=\"" + gmapsUrl + "\" allowfullscreen></iframe>" +
                "</div>" +
                "</body>" +
                "</html>";

        webView.getEngine().loadContent(htmlContent);
    }
}