package com.gluonapplication.views;

import com.gluonapplication.MultiMediaView;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

//import static jdk.internal.org.jline.terminal.Terminal.MouseTracking.Button;

public class HomeView extends View
{
    private String imageHolderID = "imageHolders";
    TextField searchBar = new TextField();
    String searcedWord = null;
    boolean isSearch = false;
    String imageUrl = null;

    public HomeView()
    {
        {
            try {
                searchBar.setPromptText("search");
                searchBar.setStyle("-fx-background-color:white; -fx-border-radius:20px; -fx-border-width:2px;");
                searchBar.setOnKeyPressed(keyEvent -> {

                    searcedWord = searchBar.getText();

                    showFullView(TourDescriptor(searcedWord), imageUrl);
                });
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }


        getStylesheets().add(HomeView.class.getResource("primary.css").toExternalForm());

        // Main container with white background
        ScrollPane rootPane = new ScrollPane();
        rootPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Main content container
        VBox mainContent = new VBox(15);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(20));
        mainContent.setMaxWidth(800);

        // Magazine header
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);

        Label title = new Label("Tour Lesotho");
        title.setId("title");

        Label subtitle = new Label("Venice: Life On The Edge");
        subtitle.setFont(Font.font("Georgia", FontPosture.ITALIC, 24));
        subtitle.setTextFill(Color.DARKGRAY);

        header.getChildren().addAll(title, subtitle);

        // Your original grid (unchanged structure)
        VBox gridContainer = new VBox(20);

        // First row
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER);

        VBox column1 = createImageCard("The  Fall that was not caught", "/MaleFalls.jpeg");
        column1.setId(imageHolderID);
        row1.getChildren().addAll(column1);

        // Second row
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER);

        HBox row3 = new HBox(20);
        row3.setAlignment(Pos.CENTER);

        VBox column3 = createImageCard("The falling river now", "/MaleFalls.jpeg");
        column3.setId(imageHolderID);
        row2.getChildren().addAll(column3);

        VBox column4 = createImageCard("The falling river", "/MaleFalls.jpeg");
        column4.setId(imageHolderID);
        row3.getChildren().addAll(column4);


        gridContainer.getChildren().addAll(row1, row2,row3);

        // Magazine footer
        Label footer = new Label("Touring guide!");
        footer.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        footer.setTextFill(Color.BLACK);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(20, 50, 0, 0));

        mainContent.getChildren().addAll(header, gridContainer, footer);
        //rootPane.getChildren().add(mainContent);
        rootPane.setContent(mainContent);
        setCenter(rootPane);
    }

    private VBox createImageCard(String title, String imageUrl) {
        this.imageUrl = imageUrl;
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        card.setId("image-card");

        MultiMediaView mediaView = new MultiMediaView();
        mediaView.setImageUrl(imageUrl);
        mediaView.getImageView().setFitWidth(240);
        mediaView.getImageView().setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        Label label = new Label(title);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.BLACK);

        card.getChildren().addAll(mediaView.getImageView(), label);

        // Add click handler to show full view
        card.setOnMouseClicked(e -> showFullView(title, imageUrl));

        return card;
    }

    @Override
    protected void updateAppBar(AppBar appBar)
    {

        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                {
                        getAppManager().getDrawer().open();
                  if(isSearch==true)
                  {
                      appBar.getActionItems().clear();
                      appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(new EventHandler<ActionEvent>() {
                          @Override
                          public void handle(ActionEvent e) {
                          }
                      }));

                      isSearch=false;
                  }

                }
        ));
        appBar.setTitleText("HOME");
        appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e ->
                {
                    appBar.getActionItems().clear();
                    appBar.setNavIcon(MaterialDesignIcon.MENU.button(new EventHandler<ActionEvent>()
                         {
                             @Override
                             public void handle(ActionEvent e) {
                                 HomeView.this.getAppManager().getDrawer().open();
                             }
                         }
                    ));
                    appBar.setTitleText("HOME");
                appBar.getActionItems().add(searchBar);
                    isSearch = true;
                }


        ));
    }

    private void showFullView(String title, String imageUrl) {
        // Create and show the full image view
        FullImageView fullImageView = new FullImageView(TourDescriptor(title), imageUrl);
        getAppManager().addViewFactory(title.replaceAll("\\s+", ""), () -> fullImageView);
        getAppManager().switchView(title.replaceAll("\\s+", ""));
    }

    private String TourDescriptor(String title)
    {
        String[] details = {
                "this is the one time description and \n we are going to make it in like thank you so much!",
                "this is the for life coding , when you are \n acquire skills like nobody s business",
                "My third guy is holding on like no one has \n ever got it before, so thank you "

        };
        String specifics = null;

        if(title.equals("The  Fall that was not caught"))
        {
            specifics = details[0];
            this.imageUrl = "/MaleFalls.jpeg";

        }else if(title.equals("The falling river now"))
        {
            specifics = details[1];
        }else if(title.equals("The falling river"))
        {
            specifics = details[2];
        }else if(title.equals("Quiz")){
            System.out.println("Quiz");
        }

        return specifics;
    }

}