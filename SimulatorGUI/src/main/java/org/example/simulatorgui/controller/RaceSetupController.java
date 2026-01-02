package org.example.simulatorgui.controller;

import javafx.beans.binding.BooleanBinding;
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
import org.example.simulatorgui.controller.addcarform.CarComponentsController;
import simulator.Car;
import simulator.Position;

import java.io.IOException;
import java.util.ArrayList;

public class RaceSetupController {
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
    private SelectionMode selectionMode;
    private Position startPosition;
    private Position finishPosition;
    private BooleanBinding startButtonDisableBinding;

    public ComboBox<Car> getStoredCarsComboBox() { return storedCarsComboBox; }
    public Pane getRaceTrackPane() { return raceTrackPane; }
    public ObservableList<Car> getStoredCars() { return storedCars; }
    public ObservableList<Car> getRaceCars() { return raceCars; }
    public ArrayList<Position> getCheckpointPositions() { return checkpointPositions; }
    public Position getStartPosition() { return startPosition; }
    public Position getFinishPosition() { return finishPosition; }

    public void setRaceCars(ObservableList<Car> raceCars) { this.raceCars = raceCars; }
    public void setStoredCars(ObservableList<Car> storedCars) { this.storedCars = storedCars; }
    public void setCheckpointPositions(ArrayList<Position> checkpoints) { this.checkpointPositions = new ArrayList<>(checkpoints); }
    public void setStartPosition(Position startposition) { this.startPosition = startposition; }
    public void setFinishPosition(Position finishPosition) { this.finishPosition = finishPosition; }

    @FXML
    private void initialize() {
        storedCarsComboBox.setItems(storedCars);
        raceCarsListView.setItems(raceCars);
        markPlayersCar();
        placeFlags();
        enableMouseClicks();
        validate();
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
    private void placeFlags() {
        if (startPosition != null) placeStartFlag(startPosition);
        if (!checkpointPositions.isEmpty()) placeCheckpoints();
        if (finishPosition != null) placeFinishFlag(finishPosition);
    }
    private void enableMouseClicks() {
        raceTrackPane.setOnMouseClicked(e -> {
            if (selectionMode == SelectionMode.START && startPosition == null) {
                startPosition = new Position(e.getX(), e.getY());
                startButtonDisableBinding.invalidate();
                placeStartFlag(startPosition);
                placeStartButton.getStyleClass().setAll("btn", "btn-blue");
            }
            if (selectionMode == SelectionMode.CHECKPOINT) {
                checkpointPositions.add(new Position(e.getX(), e.getY()));
                placeCheckpoints();
            }
            if (selectionMode == SelectionMode.FINISH && finishPosition == null) {
                finishPosition = new Position(e.getX(), e.getY());
                startButtonDisableBinding.invalidate();
                placeFinishFlag(finishPosition);
                placeFinishButton.getStyleClass().setAll("btn", "btn-red");
            }
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
    private void validate() {
        startButtonDisableBinding = new BooleanBinding() {
            { super.bind(raceCarsListView.getItems()); }
            @Override
            protected boolean computeValue() {
                int size = raceCarsListView.getItems().size();
                return size < 2 || size > 6
                        || startPosition == null
                        || finishPosition == null;
            }
        };
        startRaceButton.disableProperty().bind(startButtonDisableBinding);
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
        stage.setTitle("CarSimulator - Builder");
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
        setDefaultComboBoxValue();
    }
    @FXML
    private void onAddToRace() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            raceCars.add(selected);
            storedCars.remove(selected);
        }
        setDefaultComboBoxValue();
    }
    @FXML
    private void onTogglePlayerControlled() {
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
        placeStartButton.getStyleClass().setAll("btn", "btn-grey");
        placeCheckpointButton.getStyleClass().setAll("btn", "btn-orange");
        placeFinishButton.getStyleClass().setAll("btn", "btn-red");
    }
    @FXML
    private void onPlaceCheckpoint() {
        if (selectionMode != SelectionMode.CHECKPOINT) {
            selectionMode = SelectionMode.CHECKPOINT;
            placeCheckpointButton.getStyleClass().setAll("btn", "btn-grey");
        } else {
            selectionMode = SelectionMode.NONE;
            placeCheckpointButton.getStyleClass().setAll("btn", "btn-orange");
        }
        placeStartButton.getStyleClass().setAll("btn", "btn-blue");
        placeFinishButton.getStyleClass().setAll("btn", "btn-red");
    }
    @FXML
    private void onPlaceFinish() {
        selectionMode = SelectionMode.FINISH;
        placeStartButton.getStyleClass().setAll("btn", "btn-blue");
        placeCheckpointButton.getStyleClass().setAll("btn", "btn-orange");
        placeFinishButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML
    private void onClearTrack() {
        startPosition = null;
        finishPosition = null;
        startButtonDisableBinding.invalidate();
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
            controller.setRaceCars(raceCars);
            return controller;
        });
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CarSimulator - Race");
        stage.setMaximized(true);
        stage.show();
    }
    private void closeWindow() {
        Stage stage = (Stage) raceTrackPane.getScene().getWindow();
        stage.close();
    }
    private void setDefaultComboBoxValue() {
        storedCarsComboBox.setButtonCell(new ListCell<>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select car");
                else setText(item.toString());
            }
        });
    }
}
