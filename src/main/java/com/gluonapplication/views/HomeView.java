package com.gluonapplication.views;

import com.gluonapplication.MultiMediaView;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static com.gluonapplication.GluonApplication.FullVideo;

public class HomeView extends View {

    private TextField searchBar = new TextField();
    private boolean searchActive = false;
    private FullVideoView fullVideoView;
    Label subtitle;


    public HomeView() {
        // Main scrollable container
        ScrollPane rootPane = new ScrollPane();
        rootPane.setFitToWidth(true);
        rootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootPane.setStyle("-fx-background-color: #fafafa;");

        // Main content container
        VBox mainContent = new VBox(16);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(16));
        mainContent.setStyle("-fx-background-color: #fafafa;");

        // Header section
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 16, 24, 16));

        Label title = new Label("Tour Lesotho");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #212121;");

        Label subtitle = new Label("Discover the Mountain Kingdom");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #757575;");

        header.getChildren().addAll(title, subtitle);

        // Content cards
        VBox cardsContainer = new VBox(16);
        cardsContainer.setPadding(new Insets(8));

        cardsContainer.getChildren().addAll(
                createDestinationCard("The Maletsunyane Waterfall", "/MaleFalls.jpeg"),
                createDestinationCard("Thaba Bosiu", "/thabaBosiu.jpg"),
                createDestinationCard("The Katse Dam Lesotho", "/katse.png")
        );

        mainContent.getChildren().addAll(header, cardsContainer);
        rootPane.setContent(mainContent);
        setCenter(rootPane);

        // Initialize search bar
        searchBar.setPromptText("Search destinations...");
        searchBar.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-border-radius: 20; -fx-padding: 8 16; -fx-font-size: 14px;");
        searchBar.setMaxWidth(200);
        searchBar.setOnKeyPressed(Event->{
            String searched = searchBar.getText().toLowerCase();

            if (searched.equals("maletsunyane falls") || searched.equals("katse dam")) {
                String viewName = searched.replace(" ", "") + "View"; // e.g. "maletsunyane falls" -> "maletsunyane fallsView"

                FullVideoView fullVideoView = new FullVideoView();
                fullVideoView.setTitle(searched);
                fullVideoView.setVideoUrl(searched.equals("maletsunyane falls") ? "/FallVideo.mp4" : getKatse(searched));

                try {
                    getAppManager().addViewFactory(viewName, () -> fullVideoView);
                    getAppManager().switchView(viewName);
                } catch (IllegalArgumentException e) {
                    // View already exists, just switch to it
                    getAppManager().switchView(viewName);
                }
            } else {
                if (searchActive) {
                    subtitle.setText("Searching things for you...");
                } else {
                    subtitle.setText("Discover the Mountain Kingdom");
                }
            }
        });
    }

    private String getKatse(String searched)
    {
        if(searched.equals("katse dam"))
        {
            return "/katse.mp4";
        } else if (searched.equals("thaba bosiu"))
        {
            return "/katse.mp4";
        }

        return "";
    }


    private VBox createDestinationCard(String title, String imageUrl) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        // Create the media view
        MultiMediaView mediaView = new MultiMediaView();
        mediaView.setImageUrl(imageUrl);
        mediaView.getImageView().setFitWidth(360);
        mediaView.getImageView().setPreserveRatio(true);
        mediaView.getImageView().setStyle("-fx-background-radius: 8 8 0 0;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #212121; " +
                "-fx-padding: 12 16;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(mediaView.getImageView(), titleLabel);

        // Add hover effects
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 4); -fx-cursor:hand;");
            card.setScaleX(1.02);
            card.setScaleY(1.02);
            card.setTranslateY(-2); // Slight lift effect
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2); -fx-cursor:hand;");
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            card.setTranslateY(0);
        });

        // Add click effect
        card.setOnMousePressed(e -> {
            card.setScaleX(0.98);
            card.setScaleY(0.98);
        });

        card.setOnMouseReleased(e -> {
            card.setScaleX(1.02);
            card.setScaleY(1.02);
        });

        return card;
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        if (searchActive) {
            appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                searchActive = false;
                updateAppBar(appBar);
            }));
            appBar.setTitleText("");
            appBar.getActionItems().clear();
            appBar.getActionItems().addAll(searchBar);
        } else {
            appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
            appBar.setTitleText("Home");
            appBar.getActionItems().clear();
            appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> {
                searchActive = true;
                updateAppBar(appBar);
                searchBar.requestFocus();
            }));
        }
        appBar.setStyle("-fx-background-color: #3F51B5;"); // Material Indigo 500
    }
}