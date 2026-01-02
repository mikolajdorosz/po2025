package org.example.simulatorgui.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.maincomponent.CarHUDController;
import org.example.simulatorgui.controller.maincomponent.CarTileController;
import org.example.simulatorgui.controller.maincomponent.TopbarController;
import simulator.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML private Pane raceTrackPane;
    @FXML private VBox topbarContainer;
    @FXML private VBox competitorsContainer;
    @FXML private VBox hudContainer;
    private ObservableList<Car> raceCars = FXCollections.observableArrayList();
    private final Map<Car, ImageView> carViews = new HashMap<>();
    private final Map<String, CarTileController> carTileControllers = new HashMap<>();
    private RaceSetupController raceSetupController;
    private AnimationTimer gameLoop;
    private RaceEngine raceEngine;
    private double scaleX;
    private double scaleY;

    public RaceSetupController getRaceSetupController() { return raceSetupController; }
    public AnimationTimer getGameLoop() { return gameLoop; }
    public RaceEngine getRaceEngine() { return raceEngine; }

    public void setRaceCars(ObservableList<Car> raceCars) { this.raceCars = raceCars; }
    public void setRaceSetupController(RaceSetupController raceSetupController) { this.raceSetupController = raceSetupController; }
    public void setGameLoop(AnimationTimer gameLoop) { this.gameLoop = gameLoop; }

    @FXML
    private void initialize() throws IOException {
        loadTopbar();
        loadPlayerHUD();
        for (Car car : raceCars) {
            CarTileController controller = loadCarTile(car);
            carTileControllers.put(car.getPlateNumber(), controller);
        }
        Platform.runLater(() -> renderTrack());
        startRace();
    }
    private void loadTopbar() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/topbar-main.fxml")
        );
        Node topbar = loader.load();
        TopbarController controller = loader.getController();
        controller.setMainController(this);
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
        Car playersCar = raceCars.stream().filter(Car::getPlayerControlled).findFirst().orElse(null);
        if (playersCar == null) return;
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
        for (int i = 1; i <= raceCars.size(); i++) {
            Car car = raceCars.get(i-1);
            car.setCurrentPosition(new Position(start.getX(), start.getY()));
            ImageView carImg = placeImg(car.getCurrentPosition(), "cars/car" + i + ".png");
            car.setCarImageView(carImg);
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
        raceEngine = new RaceEngine();
        raceEngine.createRace(raceCars, raceSetupController.getStartPosition(), raceSetupController.getFinishPosition(), raceSetupController.getCheckpointPositions());
        raceEngine.start();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                raceEngine.update();
                renderCars();
                updateCarTiles();
                if (raceEngine.getRace().ended()) {
                    raceEngine.stop();
                    stopRace();
                }
            }
        };
        gameLoop.start();
    }
    private void renderCars() {
        for (Car car : raceCars) {
            ImageView view = carViews.get(car);
            if (view == null) continue;
            view.setLayoutX(car.getCurrentPosition().getX() * scaleX);
            view.setLayoutY(car.getCurrentPosition().getY() * scaleY);
        }
    }
    private void updateCarTiles() {
        Map<Car, Integer> positions = raceEngine.getRace().computeCarPositions();
        for (Car car : raceCars) {
            CarTileController controller = carTileControllers.get(car.getPlateNumber());
            Integer position = positions.get(car);
            if (controller == null || position == null) continue;
            controller.updateCarTile(car, position);
        }
    }
    public void stopRace() { if (gameLoop != null) gameLoop.stop(); }
}
