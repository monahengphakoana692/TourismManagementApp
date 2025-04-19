package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class MapViews extends View {

    private final MapView mapView = new MapView();
    private final ObservableList<String> audioPoints = FXCollections.observableArrayList();

    public MapViews() {
        // Set initial position (Maseru, Lesotho)
        mapView.setCenter(new MapPoint(-29.3167, 27.4833));
        mapView.setZoom(12);

        // Example list of points (optional)
        audioPoints.addAll(
                "Thaba-Bosiu: Mountain Fortress",
                "Maletsunyane Falls: Spectacular Waterfall",
                "Maseru Mall: Shopping Center"
        );

        ListView<String> pointsList = new ListView<>(audioPoints);
        pointsList.setPrefWidth(200);

        HBox layout = new HBox(10, mapView, pointsList);
        layout.setAlignment(Pos.CENTER);

        setCenter(layout);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lesotho Tour Guide");
        appBar.getActionItems().add(MaterialDesignIcon.GPS_FIXED.button(e -> centerOnLocation()));
    }

    private void centerOnLocation() {
        // Optional: Use Gluon Attach LocationService (if needed)
        // LocationService.create().ifPresent(service -> {
        //     service.getCurrentPosition().ifPresent(position -> {
        //         mapView.setCenter(new MapPoint(position.getLatitude(), position.getLongitude()));
        //     });
        // });
        mapView.setCenter(new MapPoint(-29.3167, 27.4833)); // Fallback to Maseru
    }
}