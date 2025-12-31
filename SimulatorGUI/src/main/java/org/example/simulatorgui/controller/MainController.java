package org.example.simulatorgui.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.maincomponent.CarHUDController;
import org.example.simulatorgui.controller.maincomponent.CarTileController;
import simulator.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML private Pane raceTrackPane;
    @FXML private VBox topbarContainer;
    @FXML private VBox competitorsContainer;
    @FXML private VBox hudContainer;
    private final ObservableList<Car> raceCars = FXCollections.observableArrayList();
    private final Map<Car, ImageView> carViews = new HashMap<>();
    private final Map<Car, CarTileController> carTileControllers = new HashMap<>();
    private RaceSetupController raceSetupController;
    private AnimationTimer gameLoop;
    private RaceEngine raceEngine;
    private Race race;
    private double scaleX;
    private double scaleY;

    public void setRaceSetupController(RaceSetupController raceSetupController) { this.raceSetupController = raceSetupController; }

    @FXML
    private void initialize() throws IOException {
        raceCars.add(new Car(
                "KR123",
                "GT-R",
                1500,
                320,
                new Position(0, 0),
                new Engine(8000, "V6", 220, 5000),
                new Gearbox(6, "manual", "gearbox", 120, 3000,
                        new Clutch("clutch", 50, 1000))
        ));
        raceCars.getFirst().setPlayerControlled(true);

        for (int i = 1; i <= 3; i++) {
            Car aiCar = new Car(
                    "AI" + i,
                    "AI Racer " + i,
                    1400 + i * 10,
                    300,
                    new Position(0, 0),
                    new Engine(7000, "V6", 200, 4000),
                    new Gearbox(6, "automatic", "gearbox", 100, 3000,
                            new Clutch("clutch", 0, 0)) // automatic, no real clutch
            );
            raceCars.add(aiCar);
        }
        renderComponents();
        Platform.runLater(() -> renderTrack());
        startRace();
    }

    private void renderComponents() throws IOException {
        loadTopbar();
        loadPlayerHUD();
        for (Car car : raceCars) {
            CarTileController controller = loadCarTile(car);
            carTileControllers.put(car, controller);
        }
    }
    private void loadTopbar() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/topbar-main.fxml")
        );
        Node topbar = loader.load();
        topbarContainer.getChildren().setAll(topbar);
    }
    private CarTileController loadCarTile(Car car) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/car-tile-main.fxml")
        );
        Node tile = loader.load();
        CarTileController controller = loader.getController();
        controller.setCar(car);
        car.addListener(controller);
        competitorsContainer.getChildren().add(tile);
        return controller;
    }
    private void loadPlayerHUD() throws IOException {
        Car playersCar = raceCars.stream().filter(Car::getPlayerControlled).findFirst().orElseThrow(() -> new IllegalStateException("No player controlled car"));
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/car-hud-main.fxml")
        );
        Node hud = loader.load();
        CarHUDController controller = loader.getController();
        controller.setCar(playersCar);
        playersCar.addListener(controller);
        Platform.runLater(() -> controller.registerInput(hud.getScene()));
        hudContainer.getChildren().setAll(hud);
    }
    private void renderTrack() {
        Pane smallPane = raceSetupController.getRaceTrackPane();            // original positions
        scaleX = raceTrackPane.getWidth() / smallPane.getWidth();
        scaleY = raceTrackPane.getHeight() / smallPane.getHeight();

        Position start = raceSetupController.getStartPosition();
        if (start != null) placeImg(start, "start.png");
        Position finish = raceSetupController.getFinishPosition();
        if (finish != null)  placeImg(finish, "finish.png");
        for (Position cp : raceSetupController.getCheckpointPositions()) placeImg(cp, "checkpoint.png");
        for (Car car : raceCars) {
            car.setPosition(new Position(start.getX(), start.getY()));
            ImageView carImg = placeImg(car.getPosition(), "car.png");
            carViews.put(car, carImg);
        }
    }
    private ImageView placeImg(Position pos, String imgPath) {
        ImageView img = new ImageView(new Image(
                getClass().getResource("/org/example/simulatorgui/images/" + imgPath).toExternalForm()
        ));
        img.setFitWidth(40);
        img.setFitHeight(40);
        img.setLayoutX(pos.getX() * scaleX);
        img.setLayoutY(pos.getY() * scaleY);
        raceTrackPane.getChildren().add(img);
        return img;
    }

    private void startRace() {
        race = new Race(raceCars, raceSetupController.getStartPosition(), raceSetupController.getFinishPosition(), raceSetupController.getCheckpointPositions());
        raceEngine = new RaceEngine(race);
        raceEngine.start();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                raceEngine.update();
                renderCars();
                updateCarTiles();
            }
        };
        gameLoop.start();
    }
    private void renderCars() {
        for (Car car : raceCars) {
            ImageView view = carViews.get(car);
            if (view == null) continue;
            view.setLayoutX(car.getPosition().getX() * scaleX);
            view.setLayoutY(car.getPosition().getY() * scaleY);
        }
    }
    private void updateCarTiles() {
        Map<Car, Integer> positions = race.computeCarPositions();
        for (Car car : raceCars) {
            carTileControllers.get(car).updateCarTile(car, positions.get(car));
        }
    }
}
