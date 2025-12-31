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
    private RaceSetupController raceSetupController;
    private final Map<Car, ImageView> carViews = new HashMap<>();
    private AnimationTimer gameLoop;
    private double scaleX;
    private double scaleY;
    private static final double TIME_SCALE = 0.25; // 25% speed
    private final Map<Car, CarTileController> carTileControllers = new HashMap<>();


    public void setRaceSetupController(RaceSetupController raceSetupController) {
        this.raceSetupController = raceSetupController;
    }

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

        // AI cars (automatic)
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
        // Start all AI cars automatically
        for (Car car : raceCars) {
            if (!car.getPlayerControlled()) {
                car.start(); // engine is running
                car.getGearbox().setCurrentGear(1); // start in first gear
            }
        }


        renderComponents();
        Platform.runLater(() -> renderTrack());
        startRace();
    }

    private void renderComponents() throws IOException {
        Node topbar = loadTopbar();
        Node hud = loadPlayerHUD();
        topbarContainer.getChildren().setAll(topbar);
        hudContainer.getChildren().setAll(hud);

        for (Car car : raceCars) {
            CarTileController controller = loadCarTile(car);
            carTileControllers.put(car, controller);
        }

        Platform.runLater(() -> {
            Scene scene = hudContainer.getScene();
            CarHUDController controller =
                    (CarHUDController) hud.getProperties().get("controller");
            controller.registerInput(scene);
        });
    }
    private Node loadTopbar() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/topbar-main.fxml")
        );
        return loader.load();
    }
    private CarTileController loadCarTile(Car car) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/car-tile-main.fxml")
        );
        Node node = loader.load();
        CarTileController controller = loader.getController();
        controller.setCar(car);
        competitorsContainer.getChildren().add(node);
        return controller;
    }
    private Node loadPlayerHUD() throws IOException {
        Car playersCar = raceCars.stream().filter(Car::getPlayerControlled).findFirst().orElseThrow(() -> new IllegalStateException("No player controlled car"));
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/maincomponent/car-hud-main.fxml")
        );
        Node hud = loader.load();
        CarHUDController controller = loader.getController();
        controller.setCar(playersCar);
        Platform.runLater(() -> controller.registerInput(hud.getScene()));
        controller.startHUDLoop();
        hud.getProperties().put("controller", controller);
        return hud;
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
        //carViews.clear();
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
        gameLoop = new AnimationTimer() {
            long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double delta = (now - last) / 1e9;
                //delta *= TIME_SCALE;
                last = now;
                updateCars(delta);
                updateCarTimes(delta);
                updateCarTiles();
                renderCars();
                // check if race finished
                if (isRaceFinished()) {
                    stop(); // stops the AnimationTimer
                    System.out.println("Race finished!");
                }
            }
        };
        gameLoop.start();
    }
    private void updateCarTimes(double dt) {
        for (Car car : raceCars) {
            car.updateRaceTime(dt);
        }
    }

    private void renderCars() {
        for (Car car : raceCars) {
            ImageView view = carViews.get(car);
            Position p = car.getPosition();

            view.setLayoutX(p.getX() * scaleX);
            view.setLayoutY(p.getY() * scaleY);
        }
    }
    private void updateCars(double delta) {
        ArrayList<Position> checkpoints = raceSetupController.getCheckpointPositions();
        Position finish = raceSetupController.getFinishPosition();

        for (Car car : raceCars) {
            updateAICar(car, delta);
            Position target = getTargetForCar(car, checkpoints, finish);
            car.goTo(delta, target);
            if (hasReached(car.getPosition(), target)) car.advanceCheckpoint();
        }
    }
    private Position getTargetForCar(Car car, ArrayList<Position> checkpoints, Position finish) {
        int index = car.getCurrentCheckpointIndex();
        if (index < checkpoints.size()) return checkpoints.get(index);
        return finish;
    }
    private boolean hasReached(Position a, Position b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }
    private boolean isRaceFinished() {
        Position finish = raceSetupController.getFinishPosition();
        for (Car car : raceCars) {
            if (hasReached(car.getPosition(), finish)) {
                if (!car.hasFinished()) {
                    car.setFinished(true);
                    car.turnOff();
                }
            }
        }

        // race finished if all cars have finished
        return raceCars.stream().allMatch(Car::hasFinished);
    }

    private void updateAICar(Car car, double dt) {
        if (car.getPlayerControlled()) return;

        ArrayList<Position> checkpoints = raceSetupController.getCheckpointPositions();
        Position finish = raceSetupController.getFinishPosition();
        Position target = getTargetForCar(car, checkpoints, finish);

        double dx = target.getX() - car.getPosition().getX();
        double dy = target.getY() - car.getPosition().getY();
        double distance = Math.sqrt(dx*dx + dy*dy);

        // simple AI gas
        car.setIsGasPressed(distance > 1);

        // Automatic clutch (for visuals only)
        car.getGearbox().getClutch().release();

        // Automatic gear shifting based on RPM
        int currentGear = car.getGearbox().getCurrentGear();
        int maxGear = car.getGearbox().getGearsNumber();
        boolean gearChanged = false;

        if (car.getEngine().getRPM() > car.getEngine().getMaxRPM() * 0.8 && currentGear < maxGear) {
            int oldGear = currentGear;
            car.getGearbox().gearUp(car.getEngine());
            gearChanged = true;
        } else if (car.getEngine().getRPM() < car.getEngine().getMaxRPM() * 0.3 && currentGear > 1) {
            int oldGear = currentGear;
            car.getGearbox().gearDown(car.getEngine());
            gearChanged = true;
        }

        // Update the AI car tile immediately after gear change
        if (gearChanged) {
            CarTileController controller = carTileControllers.get(car);
            if (controller != null) {
                // position argument can be 0 or recomputed if you want
                controller.updateCarTile(car, 0);
            }
        }
    }

    private void updateCarTiles() {
        // Compute race positions based on distance to finish
        Position finish = raceSetupController.getFinishPosition();
        raceCars.sort((c1, c2) -> {
            double d1 = distanceToFinish(c1.getPosition(), finish);
            double d2 = distanceToFinish(c2.getPosition(), finish);
            return Double.compare(d1, d2); // closer = higher position
        });

        for (int i = 0; i < raceCars.size(); i++) {
            Car car = raceCars.get(i);
            CarTileController controller = carTileControllers.get(car);
            if (controller != null) {
                controller.updateCarTile(car, i + 1); // position = i + 1
            }
        }
    }

    private double distanceToFinish(Position pos, Position finish) {
        double dx = finish.getX() - pos.getX();
        double dy = finish.getY() - pos.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
