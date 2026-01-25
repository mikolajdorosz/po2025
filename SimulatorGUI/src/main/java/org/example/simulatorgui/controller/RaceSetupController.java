package org.example.simulatorgui.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.addcarform.CarComponentsController;
import simulator.Car;
import simulator.Position;
import org.example.simulatorgui.config.*;

import java.io.IOException;
import java.util.ArrayList;

public class RaceSetupController implements ICarRepository {
    private enum SelectionMode { NONE, START, CHECKPOINT, FINISH }

    @FXML private ComboBox<Car> storedCarsComboBox;
    @FXML private ListView<Car> raceCarsListView;
    @FXML private Pane raceTrackPane;
    @FXML private Button placeStartButton;
    @FXML private Button placeCheckpointButton;
    @FXML private Button placeFinishButton;
    @FXML private Button startRaceButton;

    private ObservableList<Car> storedCars = FXCollections.observableArrayList();     // Change in ObservableList automatically updates GUI
    private ObservableList<Car> raceCars = FXCollections.observableArrayList();
    private ArrayList<Position> checkpointPositions = new ArrayList<>();
    private SelectionMode selectionMode = SelectionMode.NONE;
    private Position startPosition;
    private Position finishPosition;
    private BooleanBinding startButtonDisableBinding;

    @Override public ObservableList<Car> getRaceCars() { return raceCars; }
    @Override public ObservableList<Car> getStoredCars() { return storedCars; }
    @Override public boolean isDuplicatePlate(String plateNumber) {
        return raceCars.stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plateNumber))
                || storedCars.stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plateNumber));
    }
    @Override public void addCar(Car car) { storedCars.add(car); }
    @Override public void selectCar(Car car) { storedCarsComboBox.getSelectionModel().select(car); }

    // ===================== INITIALIZATION =====================
    @FXML private void initialize() {
        storedCarsComboBox.setItems(storedCars);
        raceCarsListView.setItems(raceCars);

        configureRaceCarsList();
        restoreFlags();
        enableTrackInteraction();
        setupValidation();
        setDefaultComboBoxValue();
    }
    private void configureRaceCarsList() {
        raceCarsListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);        // Ensures selecting only one item
        raceCarsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item.getPlayerControlled()) setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");
                    else setStyle("");
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
        if (startPosition != null) return;
        startPosition = new Position(x, y);
        placeFlag("start.png", startPosition);
        startButtonDisableBinding.invalidate();
        resetButtons();
    }
    private void placeCheckpoint(double x, double y) {
        checkpointPositions.add(new Position(x, y));
        redrawCheckpoints();
    }
    private void placeFinish(double x, double y) {
        if (finishPosition != null) return;
        finishPosition = new Position(x, y);
        placeFlag("finish.png", finishPosition);
        startButtonDisableBinding.invalidate();
        resetButtons();
    }
    private void restoreFlags() {
        if (startPosition != null) placeFlag("start.png", startPosition);
        if (finishPosition != null) placeFlag("finish.png", finishPosition);
        redrawCheckpoints();
    }
    private void redrawCheckpoints() {
        raceTrackPane.getChildren().removeIf(
                n -> n instanceof ImageView && "checkpoint".equals(n.getUserData())
        );
        for (Position p : checkpointPositions) {
            ImageView cp = placeFlag("checkpoint.png", p);
            cp.setUserData("checkpoint");
        }
    }
    private ImageView placeFlag(String img, Position pos) {
        ImageView flag = new ImageView(
                new Image(getClass()
                        .getResource("/org/example/simulatorgui/images/" + img)
                        .toExternalForm())
        );
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
            { bind(raceCars); }
            @Override
            protected boolean computeValue() {
                int size = raceCars.size();
                return size < 2 || size > 6
                        || startPosition == null
                        || finishPosition == null;
            }
        };
        startRaceButton.disableProperty().bind(startButtonDisableBinding);
    }

    // ===================== ACTIONS =====================
    @FXML private void onNewCar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/add-car.fxml"));
        Parent root = loader.load();                                // loads fxml
        AddCarController addCarController = loader.getController(); // gets controller
        addCarController.setCarRepository(new CarRepository(storedCars, raceCars));
        CarComponentsController carComponentsController = addCarController.showForm("car-components-form.fxml", addCarController.getCarComponentsForm());
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
            raceCars.add(selected);
            storedCars.remove(selected);
        }
        setDefaultComboBoxValue();
    }
    @FXML private void onDeleteCar() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            storedCars.remove(selected);
            raceCars.remove(selected);
        }
        setDefaultComboBoxValue();
    }
    @FXML private void onTogglePlayerControlled() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.getPlayerControlled()) selected.setPlayerControlled(false);
            else {
                for (Car c : raceCars) c.setPlayerControlled(false);
                selected.setPlayerControlled(true);
            }
            raceCarsListView.refresh();
        }
    }
    @FXML private void onRemoveFromRace() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            raceCars.remove(selected);
            storedCars.add(selected);
        }
    }
    @FXML private void onClearList() {
        storedCars.addAll(raceCars);
        raceCars.clear();
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
        highlight(placeStartButton); // resets all
    }
    @FXML private void onClearTrack() {
        startPosition = null;
        finishPosition = null;
        checkpointPositions.clear();
        raceTrackPane.getChildren().clear();
        startButtonDisableBinding.invalidate();
    }

    // ===================== NAVIGATION =====================
    @FXML private void onStartRace() throws IOException {
        closeWindow();
        RaceConfig config = new RaceConfig(
                raceCars,
                storedCars,
                startPosition,
                finishPosition,
                checkpointPositions,
                raceTrackPane
        );
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/main.fxml"));
        loader.setControllerFactory(_ -> new MainController(config));
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
        storedCarsComboBox.setButtonCell(new ListCell<>() {     // default value for empty ComboBox
            @Override protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select car");
                else setText(item.toString());
            }
        });
    }
}
