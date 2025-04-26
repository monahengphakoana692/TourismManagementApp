package com.gluonapplication.views;

import com.gluonapplication.MultiMediaView;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FullImageView extends View {

    private final String title;
    private final String imageUrl;

    public FullImageView(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;

        // Create the view content
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // Create full-size image view
        MultiMediaView fullMediaView = new MultiMediaView();
        fullMediaView.setImageUrl(imageUrl);
        fullMediaView.getImageView().setFitWidth(800); // Large size for full view
        fullMediaView.getImageView().setPreserveRatio(true);
        fullMediaView.getImageView().setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        // Create title label
        Label fullTitle = new Label(title);
        fullTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        fullTitle.setStyle("-fx-text-fill: #333;");

        root.getChildren().addAll(fullTitle, fullMediaView.getImageView());
        setCenter(root);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        /*appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e ->
                //getAppManager().goBack()
        ));*/
        appBar.setTitleText("IMAGE DETAIL");
    }
}
