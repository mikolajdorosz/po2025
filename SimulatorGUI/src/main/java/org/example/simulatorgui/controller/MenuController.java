package org.example.simulatorgui.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.simulatorgui.controller.form.CarController;
import org.example.simulatorgui.controller.form.EngineController;
import simulator.Car;

import java.io.IOException;

public class MenuController {
    @FXML private Button startRaceButton;
    @FXML private VBox raceCarsContainer;
    @FXML private Separator raceSeparator;
    @FXML private ComboBox<Car> storedCarsComboBox;
    @FXML private ListView<Car> raceCarsListView;
    private final ObservableList<Car> storedCars = FXCollections.observableArrayList();     // Change in ObservableList automatically updates GUI
    private final ObservableList<Car> raceCars = FXCollections.observableArrayList();
    private Stage stage;
    //private MainController mainController;

    public ComboBox<Car> getStoredCarsComboBox() {
        return storedCarsComboBox;
    }
    public ObservableList<Car> getStoredCarsList() {
        return storedCars;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
//    public void setMainController(MainController mainController) {
//        this.mainController = mainController;
//    }

    @FXML
    private void initialize() {
        storedCarsComboBox.setItems(storedCars);
        raceCarsListView.setItems(raceCars);
        raceListHandler();
        windowSizeHandler();
        updatePlayersCar();
        startButtonHandler();
    }

    private void updatePlayersCar() {
        raceCarsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        raceCarsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getModel());
                    if (item.getPlayerControlled()) setStyle("-fx-font-weight: bold; -fx-text-fill: #10b981;");
                    else setStyle("");
                }
            }
        });
    }
    private void startButtonHandler() {
        startRaceButton.disableProperty().bind(     // disable/enable start button
                Bindings.size(raceCarsListView.getItems()).lessThan(2)
        );
    }
    private void raceListHandler() {

        BooleanBinding raceNotEmpty = Bindings.isNotEmpty(raceCarsListView.getItems());     // show/hide race car list
        raceCarsContainer.visibleProperty().bind(raceNotEmpty);
        raceCarsContainer.managedProperty().bind(raceNotEmpty);
        raceSeparator.visibleProperty().bind(raceNotEmpty);
        raceSeparator.managedProperty().bind(raceNotEmpty);
    }
    private void windowSizeHandler() {
        raceCarsListView.getItems().addListener(       // adjust window size
                (ListChangeListener<Car>) change -> {
                    if (stage != null) {
                        stage.sizeToScene();
                    }
                }
        );
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onNewCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/add_car.fxml"));
            Parent root = loader.load();  // loads AddCarController
            AddCarController addCarController = loader.getController();
            addCarController.setMenuController(this);  // set menuController BEFORE loading CarController
            addCarController.showCarForm();             // now CarController sees menuController

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("CarSimulator");
            stage.setMinWidth(1200);
            stage.setMinHeight(600);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            for (Car c : raceCarsListView.getItems()) {
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
        raceCars.clear();
    }
    @FXML
    private void onStartRace() {
//        if (mainController != null) {
//            mainController.setRaceCars(raceCars);
//        }
    }
}
