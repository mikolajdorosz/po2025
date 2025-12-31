package org.example.simulatorgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.simulatorgui.Main;
import org.example.simulatorgui.controller.addcarform.CarComponentsController;
import simulator.Car;
import simulator.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RaceSetupController {
    @FXML private ComboBox<Car> storedCarsComboBox;
    @FXML private ListView<Car> raceCarsListView;
    @FXML private Pane raceTrackPane;
    @FXML private Button startRaceButton;
    private final ObservableList<Car> storedCars = FXCollections.observableArrayList();     // Change in ObservableList automatically updates GUI
    private final ObservableList<Car> raceCars = FXCollections.observableArrayList();
    private enum SelectionMode { NONE, START, CHECKPOINT, FINISH }
    private SelectionMode selectionMode = SelectionMode.NONE;
    private final ArrayList<Position> checkpointPositions = new ArrayList<>();
    private Position startPosition;
    private Position finishPosition;

    public Pane getRaceTrackPane() {
        return raceTrackPane;
    }
    public ComboBox<Car> getStoredCarsComboBox() {
        return storedCarsComboBox;
    }
    public ObservableList<Car> getStoredCarsList() {
        return storedCars;
    }
    public Position getStartPosition() {
        return startPosition;
    }
    public ArrayList<Position> getCheckpointPositions() {
        return checkpointPositions;
    }
    public Position getFinishPosition() {
        return finishPosition;
    }

    @FXML
    private void initialize() {
        storedCarsComboBox.setItems(storedCars);
        raceCarsListView.setItems(raceCars);
        markPlayersCar();
        startButtonState();
        enableMouseClicks();
    }

    private void markPlayersCar() {
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

    private void enableMouseClicks() {
        raceTrackPane.setOnMouseClicked(e -> {
            if (selectionMode == SelectionMode.START) {
                startPosition = new Position(e.getX(), e.getY());
                placeStartFlag(startPosition);
            }
            if (selectionMode == SelectionMode.CHECKPOINT) {
                checkpointPositions.add(new Position(e.getX(), e.getY()));
                placeCheckpoints();
            }
            if (selectionMode == SelectionMode.FINISH) {
                finishPosition = new Position(e.getX(), e.getY());
                placeFinishFlag(finishPosition);
            }
            selectionMode = SelectionMode.NONE;
        });
    }
    private void placeStartFlag(Position position) {
        ImageView startFlag = createFlag("start.png");
        raceTrackPane.getChildren().add(startFlag);
        startFlag.setLayoutX(position.getX());
        startFlag.setLayoutY(position.getY());
    }
    private void placeCheckpoints() {
        for (Position pos : checkpointPositions) {
            ImageView checkpoint = createFlag("checkpoint.png");
            checkpoint.setLayoutX(pos.getX());
            checkpoint.setLayoutY(pos.getY());
            raceTrackPane.getChildren().add(checkpoint);
        }
    }
    private void placeFinishFlag(Position position) {
        ImageView finishFlag = createFlag("finish.png");
        raceTrackPane.getChildren().add(finishFlag);
        finishFlag.setLayoutX(position.getX());
        finishFlag.setLayoutY(position.getY());
    }
    private ImageView createFlag(String path) {
        ImageView flag = new ImageView(
                new Image(getClass().getResource("/org/example/simulatorgui/images/" + path).toExternalForm())
        );
        flag.setFitWidth(20);
        flag.setFitHeight(20);
        return flag;
    }

    private void startButtonState() {
//        startRaceButton.disableProperty().bind(     // disable/enable start button
//                Bindings.size(raceCarsListView.getItems()).lessThan(2)
//        );
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onNewCar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/add-car.fxml"));
        Parent root = loader.load();                                // loads fxml
        AddCarController addCarController = loader.getController(); // gets controller
        addCarController.setRaceSetupController(this);              // set RaceSetupController BEFORE loading CarComponentsController
        CarComponentsController carComponentsController = addCarController.showForm("car-components-form.fxml", addCarController.getCarComponentsForm());
        carComponentsController.setAddCarController(addCarController);
        addCarController.setCarComponentsController(carComponentsController);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CarSimulator");
        stage.setMinWidth(1200);
        stage.setMinHeight(600);
        stage.show();
    }
    @FXML
    private void onDeleteCar() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            storedCars.remove(selected);
            raceCars.remove(selected);
        }
    }
    @FXML
    private void onAddToRace(ActionEvent actionEvent) {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            raceCars.add(selected);
            storedCars.remove(selected);
        }
        storedCarsComboBox.setButtonCell(new ListCell<>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select a car");
                else setText(item.toString());
            }
        });
    }
    @FXML
    private void onSetPlayerCar() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            for (Car c : raceCars) {
                if (c.getPlayerControlled()) c.setPlayerControlled(false);
            }
            selected.setPlayerControlled(true);
            raceCarsListView.refresh();
        }
    }
    @FXML
    private void onRemoveFromRace() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            raceCars.remove(selected);
            storedCars.add(selected);
        }
    }
    @FXML
    private void onClearList() {
        storedCars.addAll(raceCars);
        raceCars.clear();
    }
    @FXML
    private void onPlaceStart() {
        selectionMode = SelectionMode.START;
    }
    @FXML
    private void onPlaceCheckpoint() {
        selectionMode = SelectionMode.CHECKPOINT;
    }
    @FXML
    private void onPlaceFinish() {
        selectionMode = SelectionMode.FINISH;
    }
    @FXML
    private void onClearTrack() {
        checkpointPositions.clear();
        raceTrackPane.getChildren().clear();
    }
    @FXML
    private void onStartRace() throws IOException {
        closeWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/main.fxml"));
        loader.setControllerFactory(param -> {
            MainController controller = new MainController();
            controller.setRaceSetupController(this); // inject BEFORE initialize() is called
            return controller;
        });
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CarSimulator");
        stage.setMaximized(true);
        stage.show();
    }
    private void closeWindow() {
        Stage stage = (Stage) raceTrackPane.getScene().getWindow();
        stage.close();
    }
}
