package com.gluonapplication;

import com.gluonapplication.views.*;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class GluonApplication extends Application {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String MAP_VIEW = "Map View";
    public static final String TOURS_VIEW = "Tours View";
    public static final String QUIZ_VIEW = "Quiz View";
    public static final String SETTINGS_VIEW = "Settings View";
    public static final String FullVideo = "Video View";

    private final AppManager appManager = AppManager.initialize(this::postInit);

    @Override
    public void init()
    {
        System.out.println("Checking DLL load...");
        System.load("C:\\arcgis-runtime-sdk-java-100.15.0\\jniLibs\\WIN64\\runtimecore.dll");
        System.out.println("DLL loaded successfully!");
        appManager.addViewFactory(PRIMARY_VIEW, HomeView::new);
        appManager.addViewFactory(MAP_VIEW, MapViews::new);
        appManager.addViewFactory(TOURS_VIEW, WebViews::new);
        appManager.addViewFactory(QUIZ_VIEW, QuizView::new);
        appManager.addViewFactory(FullVideo, () -> {
            FullVideoView view = new FullVideoView();
            // Default settings if needed
            return view;
        });
        //appManager.addViewFactory(SETTINGS_VIEW, SettingsView::new);

        DrawerManager.buildDrawer(appManager);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setResizable(false);
        appManager.start(primaryStage);
    }

    private void postInit(Scene scene)
    {
        Swatch.LIME.assignTo(scene); // Changed to blue for a travel app feel
        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonApplication.class.getResourceAsStream("/lesotho.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}