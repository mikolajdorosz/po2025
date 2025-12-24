package org.example.simulatorgui.controller.competition;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import simulator.Car;
import simulator.Position;
import java.util.HashMap;
import java.util.Map;

public class TrackController {
    @FXML private Label timeLabel;
    @FXML private Pane trackPane;
    private Position startPosition;
    private Position finishPosition;
    private final Map<Car, ImageView> carViews = new HashMap<>();
    private AnimationTimer gameLoop;
    private ImageView startFlag;
    private ImageView finishFlag;
    private enum SelectionMode { NONE, START, FINISH }
    private SelectionMode selectionMode = SelectionMode.NONE;
    private ObservableList<Car> raceCars;

    public void setRaceCars(ObservableList<Car> raceCars) {
        this.raceCars = raceCars;
    }

    @FXML
    public void initialize() {
        trackPane.setOnMouseClicked(e -> {
            if (selectionMode == SelectionMode.START) {
                startPosition = new Position(e.getX(), e.getY());
                placeStartFlag();
            }
            if (selectionMode == SelectionMode.FINISH) {
                finishPosition = new Position(e.getX(), e.getY());
                placeFinishFlag();
            }
            selectionMode = SelectionMode.NONE;
        });
    }

    private void placeStartFlag() {
        if (startFlag == null) {
            startFlag = createFlag("/org/example/simulatorgui/images/start.png");
            trackPane.getChildren().add(startFlag);
        }
        startFlag.setLayoutX(startPosition.getX());
        startFlag.setLayoutY(startPosition.getY());
    }
    private void placeFinishFlag() {
        if (finishFlag == null) {
            finishFlag = createFlag("/org/example/simulatorgui/images/finish.png");
            trackPane.getChildren().add(finishFlag);
        }
        finishFlag.setLayoutX(finishPosition.getX());
        finishFlag.setLayoutY(finishPosition.getY());
    }
    private ImageView createFlag(String path) {
        ImageView flag = new ImageView(
                new Image(getClass().getResource(path).toExternalForm())
        );
        flag.setFitWidth(40);
        flag.setFitHeight(40);
        return flag;
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onPlaceStart(ActionEvent actionEvent) {
        selectionMode = SelectionMode.START;
    }
    @FXML
    private void onPlaceFinish(ActionEvent actionEvent) {
        selectionMode = SelectionMode.FINISH;

    }
    @FXML
    private void onRace() {
        if (startPosition == null || finishPosition == null) {
            System.out.println("Set start and finish first!");
            return;
        }

        spawnCars();
        startRace();
    }
    private void spawnCars() {
        trackPane.getChildren().removeIf(node -> node instanceof ImageView && node != startFlag && node != finishFlag);
        carViews.clear();
        for (Car car : raceCars) {
            car.getPosition().setX(startPosition.getX());
            car.getPosition().setY(startPosition.getY());
            ImageView view = new ImageView(
                    new Image(getClass().getResource("/org/example/simulatorgui/images/car.png").toExternalForm())
            );
            view.setFitWidth(40);
            view.setFitHeight(20);
            trackPane.getChildren().add(view);
            carViews.put(car, view);
        }
    }
    private void startRace() {
        for (Car car : raceCars) {
            if (!car.getPlayerControlled()) car.start();
        }
        gameLoop = new AnimationTimer() {
            long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double delta = (now - last) / 1e9;
                last = now;
                updateCars(delta);
                renderCars();
            }
        };
        gameLoop.start();
    }
    private void updateCars(double delta) {
        for (Car car : raceCars) {
            if (!car.getPlayerControlled()) {
                car.getEngine().increaseRPM(); // AI
            }
            car.goTo(delta, finishPosition);
        }
    }
    private void renderCars() {
        for (Car car : raceCars) {
            ImageView view = carViews.get(car);
            Position p = car.getPosition();
            view.setLayoutX(p.getX());
            view.setLayoutY(p.getY());
        }
    }

    @FXML
    private void onPause(ActionEvent actionEvent) {
    }
    @FXML
    private void onRestart(ActionEvent actionEvent) {
    }
    @FXML
    private void onExit(ActionEvent actionEvent) {
    }
}
