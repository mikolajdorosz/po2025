package org.example.simulatorgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.competition.CarHUDController;
import org.example.simulatorgui.controller.competition.CarParamsController;
import org.example.simulatorgui.controller.competition.TrackController;
import simulator.*;

import java.io.IOException;

public class MainController {
    @FXML private VBox competitorsContainer;
    @FXML private VBox trackHUDContainer;
    private final ObservableList<Car> raceCars = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        raceCars.add(new Car(
                "KR123",
                "GT-R",
                1500,
                320,
                new Position(0, 0),
                new Engine(8000, "V6", 220, 5000),
                new Gearbox(6, "Manual", "manual", 120, 3000)
        ));
        raceCars.add(new Car(
                "KR456",
                "Supra",
                1480,
                310,
                new Position(0, 0),
                new Engine(7800, "V6", 210, 4800),
                new Gearbox(6, "Manual", "manual", 120, 3000)
        ));
        raceCars.get(0).setPlayerControlled(true);

        showRaceCars();
        loadTrackAndHUD();
    }

    private void showRaceCars() {
        competitorsContainer.getChildren().clear();
        for (Car car : raceCars) {
            competitorsContainer.getChildren().add(createCarParamsView(car));
        }
    }
    private Node createCarParamsView(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/simulatorgui/view/competition/car_params_competition.fxml")
            );
            Node node = loader.load();
            CarParamsController controller = loader.getController();
            controller.setCar(car);
            return node;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load car params view", e);
        }
    }

    private void loadTrackAndHUD() {
        trackHUDContainer.getChildren().clear();
        Node trackView = loadTrack();
        Node hudView = loadPlayerHUD();
        trackHUDContainer.getChildren().add(trackView);
        trackHUDContainer.getChildren().add(hudView);
    }
    private Node loadTrack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/simulatorgui/view/competition/track_competition.fxml")
            );
            Node track = loader.load();
            TrackController trackController = loader.getController();
            trackController.setRaceCars(raceCars);
            return track;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Track", e);
        }
    }
    private Node loadPlayerHUD() {
        Car playerCar = raceCars.stream()
                .filter(Car::getPlayerControlled)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player controlled car"));
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/simulatorgui/view/competition/car_hud_competition.fxml")
            );
            Node hud = loader.load();
            CarHUDController controller = loader.getController();
            controller.setCar(playerCar);
            return hud;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Car HUD", e);
        }
    }
}
