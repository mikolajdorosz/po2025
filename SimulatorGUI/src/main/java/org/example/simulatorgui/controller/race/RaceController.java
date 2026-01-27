package org.example.simulatorgui.controller.race;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.repo.CarRepository;
import org.example.simulatorgui.model.race.Race;
import org.example.simulatorgui.model.race.RaceSetup;
import org.example.simulatorgui.app.Main;
import org.example.simulatorgui.model.race.RaceConfig;
import org.example.simulatorgui.render.RaceRenderer;
import org.example.simulatorgui.model.engine.RaceSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceController {
    @FXML private Pane raceTrackPane;
    @FXML private VBox topbarContainer;
    @FXML private VBox competitorsContainer;
    @FXML private VBox hudContainer;

    private final RaceConfig config;
    private final CarRepository carRepository;
    private final Map<String, CarController> carTileControllers = new HashMap<>();
    private RaceRenderer raceRenderer;
    private RaceSession raceSession;

    public RaceController(RaceConfig config) {
        this.config = config;
        this.carRepository = new CarRepository(config);
    }

    @FXML private void initialize() throws IOException {
        loadTopbar();
        loadCarTiles();
        loadPlayerHUD();
        raceRenderer = new RaceRenderer(raceTrackPane);
        raceSession = new RaceSession();
        Platform.runLater(this::startRace);
    }
    private void loadTopbar() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/race/topbar.fxml")
        );
        Node topbar = loader.load();
        TopbarController controller = loader.getController();
        controller.setOnExit(this::onExitRequested);
        topbarContainer.getChildren().setAll(topbar);
    }
    private void loadCarTiles() throws IOException {
        for (Car car : carRepository.getRaceCars()) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/simulatorgui/view/race/car.fxml")
            );
            Node tile = loader.load();
            CarController controller = loader.getController();
            controller.setCar(car);
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
                getClass().getResource("/org/example/simulatorgui/view/race/car-hud.fxml")
        );
        Node hud = loader.load();
        CarHUDController controller = loader.getController();
        controller.setCar(playersCar);
        hudContainer.getChildren().setAll(hud);
        Platform.runLater(() -> controller.registerInput(hud.getScene()));
    }
    // BRIDGE WITH MODEL
    private void startRace() {
        raceRenderer.renderTrack(
                config.getReferenceTrackPane(),
                config.getStartPosition(),
                config.getFinishPosition(),
                config.getCheckpointPositions(),
                carRepository.getRaceCars()
        );
        raceSession.start(
                buildRaceSetup(config),
                this::onUpdate,
                this::onRaceFinished
        );
    }
    private RaceSetup buildRaceSetup(RaceConfig config) {
        return new RaceSetup(
                List.copyOf(config.getRaceCars()),
                config.getStartPosition(),
                config.getFinishPosition(),
                List.copyOf(config.getCheckpointPositions())
        );
    }
    private void onUpdate() {
        raceRenderer.renderCars(carRepository.getRaceCars());
        updateCarTiles();
    }
    private void updateCarTiles() {
        Race race = raceSession.getRaceEngine().getRace();
        if (race == null) return;
        Map<Car, Integer> positions = race.computeCarPositions();
        for (Car car : carRepository.getRaceCars()) {
            CarController controller = carTileControllers.get(car.getPlateNumber());
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
                    "/org/example/simulatorgui/view/main-view.fxml",
                    new Stage(),
                    config
            );
        } catch (IOException e) { e.printStackTrace(); }
    }
}
