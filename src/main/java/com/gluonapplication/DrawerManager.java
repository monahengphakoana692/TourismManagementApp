package com.gluonapplication;

import com.gluonhq.attach.lifecycle.LifecycleService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.image.Image;

import static com.gluonapplication.GluonApplication.*;
import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class DrawerManager {

    public static void buildDrawer(AppManager app) {
        NavigationDrawer drawer = app.getDrawer();

        NavigationDrawer.Header header = new NavigationDrawer.Header("Lesotho Tour Guide",
                "Explore the Mountain Kingdom",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/lesotho.png"))));
        drawer.setHeader(header);


        final Item homeItem = new ViewItem("Home", MaterialDesignIcon.HOME.graphic(), HOME_VIEW);
        final Item mapItem = new ViewItem("Map", MaterialDesignIcon.MAP.graphic(), MAP_VIEW);
        final Item toursItem = new ViewItem("Trivia/Quiz", MaterialDesignIcon.ASSESSMENT.graphic(), QUIZ_VIEW);
        final Item quizItem = new ViewItem("Quizzes", MaterialDesignIcon.MAP.graphic(), TOURS_VIEW);
        final Item settingsItem = new ViewItem("Settings", MaterialDesignIcon.SETTINGS.graphic(), SETTINGS_VIEW);

        drawer.getItems().addAll(homeItem, mapItem,quizItem, toursItem, settingsItem);

        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
    }
}