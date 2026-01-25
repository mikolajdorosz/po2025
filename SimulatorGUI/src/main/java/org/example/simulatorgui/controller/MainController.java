package org.example.simulatorgui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.Main;
import org.example.simulatorgui.config.RaceConfig;
import org.example.simulatorgui.controller.maincomponent.CarHUDController;
import org.example.simulatorgui.controller.maincomponent.CarTileController;
import org.example.simulatorgui.controller.maincomponent.TopbarController;
import org.example.simulatorgui.renderer.RaceRenderer;
import org.example.simulatorgui.session.RaceSession;
import simulator.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainController implements IRaceExitHandler {
    @FXML private Pane raceTrackPane;
    @FXML private VBox topbarContainer;
    @FXML private VBox competitorsContainer;
    @FXML private VBox hudContainer;

    private final RaceConfig config;
    private final CarRepository carRepository;
    private final Map<String, CarTileController> carTileControllers = new HashMap<>();
    private RaceRenderer raceRenderer;
    private RaceSession raceSession;

    public MainController(RaceConfig config) {
        this.config = config;
        this.carRepository = new CarRepository(config);
    }

    @FXML private void initialize() throws IOException {
        loadTopbar();
        loadPlayerHUD();
        loadCarTiles();
        raceRenderer = new RaceRenderer(raceTrackPane);
        raceSession = new RaceSession();
        Platform.runLater(this::startRace);
    }
    private void loadTopbar() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/topbar-main.fxml")
        );
        Node topbar = loader.load();
        TopbarController controller = loader.getController();
        controller.setOnExit(this::onExitRequested);
        topbarContainer.getChildren().setAll(topbar);
    }
    private void loadCarTiles() throws IOException {
        for (Car car : carRepository.getRaceCars()) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/simulatorgui/view/maincomponent/car-tile-main.fxml")
            );
            Node tile = loader.load();
            CarTileController controller = loader.getController();
            controller.setCar(car);
            car.addListener(() ->
                    controller.refresh(
                            raceSession.getRaceEngine()
                                    .getRace()
                                    .computeCarPositions()
                                    .getOrDefault(car, 1)
                    )
            );
            competitorsContainer.getChildren().add(tile);
            carTileControllers.put(car.getPlateNumber(), controller);
        }
    }
    private void loadPlayerHUD() throws IOException {
        Car playersCar = carRepository.getRaceCars()
                .stream()
                .filter(Car::getPlayerControlled)
                .findFirst()
                .orElse(null);

        if (playersCar == null) return;
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/car-hud-main.fxml")
        );
        Node hud = loader.load();
        CarHUDController controller = loader.getController();
        controller.setCar(playersCar);
        Platform.runLater(() -> controller.registerInput(hud.getScene()));
        hudContainer.getChildren().setAll(hud);
    }
    private void startRace() {
        raceRenderer.renderTrack(
                config.getReferenceTrackPane(),
                config.getStartPosition(),
                config.getFinishPosition(),
                config.getCheckpointPositions(),
                carRepository.getRaceCars()
        );
        raceSession.getRaceEngine().createRace(
                carRepository.getRaceCars(),
                config.getStartPosition(),
                config.getFinishPosition(),
                config.getCheckpointPositions()
        );
        raceSession.start(
                carRepository.getRaceCars(),
                this::onUpdate,
                this::onRaceFinished
        );
    }
    private void onUpdate() {
        raceRenderer.renderCars(carRepository.getRaceCars());
        updateCarTiles();
    }
    private void updateCarTiles() {
        var positions = raceSession
                .getRaceEngine()
                .getRace()
                .computeCarPositions();
        for (Car car : carRepository.getRaceCars()) {
            CarTileController controller = carTileControllers.get(car.getPlateNumber());
            if (controller == null) continue;
            int position = positions.getOrDefault(car, 1);
            controller.refresh(position);
        }
    }
    private void onRaceFinished() { raceSession.stop(); }
    public void onExitRequested() {
        if (raceSession != null) raceSession.stop();
        try {
            Main.openRaceSetupWindow(
                    "/org/example/simulatorgui/view/race-setup.fxml",
                    new Stage(),
                    config
            );
        } catch (IOException e) { e.printStackTrace(); }
    }
}
