package org.example.simulatorgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import simulator.*;


public class MainController {
    @FXML
    private Button addCarButton;
    @FXML
    private Button deleteCarButton;
    @FXML
    private ComboBox<Car> carComboBox;

    private final ObservableList<Car> cars = FXCollections.observableArrayList();

    @FXML
    public void onAddCar(ActionEvent actionEvent) {
        Car car = carPaneController.getCarFromInput();
        cars.add(car);
        carComboBox.getSelectionModel().select(car);
        System.out.println("Added car: " + car);
    }
    @FXML
    public void onDeleteCar(ActionEvent actionEvent) {
        Car selected = carComboBox.getValue();
        if (selected != null) {
            cars.remove(selected);
            carPaneController.setActiveCar(null);
            gearboxPaneController.setActiveGearbox(null);
        }
        System.out.println("Deleted car: " + selected);
    }

    // Controllers
    @FXML
    private CarController carPaneController;
    @FXML
    private GearboxController gearboxPaneController;
    @FXML
    private EngineController enginePaneController;
    @FXML
    private ClutchController clutchPaneController;

    @FXML
    private void initialize() {
        gearboxPaneController.setClutchController(clutchPaneController);
        carPaneController.setComponentsControllers(enginePaneController, gearboxPaneController);

        carComboBox.setItems(cars);
        carComboBox.valueProperty().addListener((obs, oldCar, newCar) -> {
            if (newCar != null) {
                carPaneController.setActiveCar(newCar);
                gearboxPaneController.setActiveGearbox(newCar.getGearbox());
                System.out.println("Selected car: " + newCar);
            }
        });
    }
}
