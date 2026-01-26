package org.example.simulatorgui.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.form.AddCarController;
import org.example.simulatorgui.controller.form.CarComponentsController;
import org.example.simulatorgui.controller.race.RaceController;
import simulator.Car;
import simulator.Position;
import org.example.simulatorgui.config.*;

import java.io.IOException;

public class RaceSetupController {
    private enum SelectionMode { NONE, START, CHECKPOINT, FINISH }

    @FXML private ComboBox<Car> storedCarsComboBox;
    @FXML private ListView<Car> raceCarsListView;
    @FXML private Pane raceTrackPane;
    @FXML private Button placeStartButton;
    @FXML private Button placeCheckpointButton;
    @FXML private Button placeFinishButton;
    @FXML private Button startRaceButton;

    private final RaceConfig config;
    private final CarRepository carRepository;
    private SelectionMode selectionMode = SelectionMode.NONE;
    private BooleanBinding startButtonDisableBinding;

    public RaceSetupController(RaceConfig config) {
        this.config = config;
        this.carRepository = new CarRepository(config);
    }

    // ===================== INITIALIZATION =====================
    @FXML private void initialize() {
        storedCarsComboBox.setItems(config.getStoredCars());
        raceCarsListView.setItems(config.getRaceCars());

        configureRaceCarsList();
        restoreFlags();
        enableTrackInteraction();
        setupValidation();
        setDefaultComboBoxValue();
    }
    private void configureRaceCarsList() {
        raceCarsListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
        raceCarsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    setStyle(item.getPlayerControlled() ? "-fx-font-weight: bold; -fx-text-fill: blue;" : "");
                }
            }
        });
    }

    // ===================== TRACK =====================
    private void enableTrackInteraction() {
        raceTrackPane.setOnMouseClicked(e -> {
            switch (selectionMode) {
                case START -> placeStart(e.getX(), e.getY());
                case CHECKPOINT -> placeCheckpoint(e.getX(), e.getY());
                case FINISH -> placeFinish(e.getX(), e.getY());
            }
        });
    }
    private void placeStart(double x, double y) {
        if (config.getStartPosition() != null) return;
        config.setStartPosition(new Position(x, y));
        placeFlag("start.png", config.getStartPosition());
        startButtonDisableBinding.invalidate();
        resetButtons();
    }
    private void placeCheckpoint(double x, double y) {
        config.addCheckpoint(new Position(x, y));
        redrawCheckpoints();
    }
    private void placeFinish(double x, double y) {
        if (config.getFinishPosition() != null) return;
        config.setFinishPosition(new Position(x, y));
        placeFlag("finish.png", config.getFinishPosition());
        startButtonDisableBinding.invalidate();
        resetButtons();
    }
    private void restoreFlags() {
        if (config.getStartPosition() != null) placeFlag("start.png", config.getStartPosition());
        if (config.getFinishPosition() != null) placeFlag("finish.png", config.getFinishPosition());
        redrawCheckpoints();
    }
    private void redrawCheckpoints() {
        raceTrackPane.getChildren().removeIf(
                n -> n instanceof ImageView && "checkpoint".equals(n.getUserData())
        );
        for (Position p : config.getCheckpointPositions()) {
            ImageView cp = placeFlag("checkpoint.png", p);
            cp.setUserData("checkpoint");
        }
    }
    private ImageView placeFlag(String img, Position pos) {
        ImageView flag = new ImageView(new Image(
                getClass().getResource("/org/example/simulatorgui/images/" + img).toExternalForm()
        ));
        flag.setFitWidth(20);
        flag.setFitHeight(20);
        flag.setLayoutX(pos.getX());
        flag.setLayoutY(pos.getY());
        raceTrackPane.getChildren().add(flag);
        return flag;
    }

    // ===================== VALIDATION =====================
    private void setupValidation() {
        startButtonDisableBinding = new BooleanBinding() {
            { bind(config.getRaceCars()); }
            @Override
            protected boolean computeValue() {
                int size = config.getRaceCars().size();
                return size < 2 || size > 6
                        || config.getStartPosition() == null
                        || config.getFinishPosition() == null;
            }
        };
        startRaceButton.disableProperty().bind(startButtonDisableBinding);
    }

    // ===================== ACTIONS =====================
    @FXML private void onNewCar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/add-car.fxml"));
        Parent root = loader.load();

        AddCarController addCarController = loader.getController();
        addCarController.setCarRepository(carRepository);

        CarComponentsController carComponentsController =
                addCarController.showForm("car-components-form.fxml", addCarController.getCarComponentsForm());

        carComponentsController.setAddCarController(addCarController);
        addCarController.setCarComponentsController(carComponentsController);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CarSimulator - Builder");
        stage.setMinWidth(1200);
        stage.setMinHeight(600);
        stage.show();
    }
    @FXML private void onAddToRace() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            carRepository.getRaceCars().add(selected);
            carRepository.getStoredCars().remove(selected);
        }
        setDefaultComboBoxValue();
    }
    @FXML private void onDeleteCar() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            carRepository.getStoredCars().remove(selected);
            carRepository.getRaceCars().remove(selected);
        }
        setDefaultComboBoxValue();
    }
    @FXML private void onTogglePlayerControlled() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.getPlayerControlled()) selected.setPlayerControlled(false);
            else {
                carRepository.getRaceCars().forEach(c -> c.setPlayerControlled(false));
                selected.setPlayerControlled(true);
            }
            raceCarsListView.refresh();
        }
    }
    @FXML private void onRemoveFromRace() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            carRepository.getRaceCars().remove(selected);
            carRepository.getStoredCars().add(selected);
        }
    }
    @FXML private void onClearList() {
        carRepository.getStoredCars().addAll(carRepository.getRaceCars());
        carRepository.getRaceCars().clear();
    }

    // ===================== MODE SELECTION =====================
    @FXML private void onPlaceStart() {
        selectionMode = SelectionMode.START;
        highlight(placeStartButton);
    }
    @FXML private void onPlaceCheckpoint() {
        selectionMode = selectionMode == SelectionMode.CHECKPOINT
                ? SelectionMode.NONE
                : SelectionMode.CHECKPOINT;
        highlight(placeCheckpointButton);
    }
    @FXML private void onPlaceFinish() {
        selectionMode = SelectionMode.FINISH;
        highlight(placeFinishButton);
    }
    private void highlight(Button active) {
        placeStartButton.getStyleClass().setAll("btn", "btn-blue");
        placeCheckpointButton.getStyleClass().setAll("btn", "btn-orange");
        placeFinishButton.getStyleClass().setAll("btn", "btn-red");
        active.getStyleClass().setAll("btn", "btn-grey");
    }
    private void resetButtons() {
        selectionMode = SelectionMode.NONE;
        highlight(placeStartButton);
    }
    @FXML private void onClearTrack() {
        config.setStartPosition(null);
        config.setFinishPosition(null);
        config.clearCheckpoints();
        raceTrackPane.getChildren().clear();
        startButtonDisableBinding.invalidate();
    }

    // ===================== NAVIGATION =====================
    @FXML private void onStartRace() throws IOException {
        closeWindow();
        config.setReferenceTrackPane(raceTrackPane);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/main.fxml"));
        loader.setControllerFactory(_ -> new RaceController(config));
        Parent root = loader.load();
        Stage raceStage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        raceStage.setScene(scene);
        raceStage.setTitle("CarSimulator - Race");
        raceStage.setMaximized(true);
        raceStage.show();
    }
    private void closeWindow() {
        Stage stage = (Stage) raceTrackPane.getScene().getWindow();
        stage.close();
    }
    private void setDefaultComboBoxValue() {
        storedCarsComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? "Select car" : item.toString());
            }
        });
    }
}
