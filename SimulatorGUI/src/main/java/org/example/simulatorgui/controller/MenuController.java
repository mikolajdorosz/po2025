package org.example.simulatorgui.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import simulator.Car;

import java.io.IOException;

public class MenuController {

    @FXML private VBox emptyStateBox;
    @FXML private VBox mainBox;

    @FXML private ComboBox<Car> storedCarsComboBox;
    @FXML private ListView<Car> raceCarsListView;

    private final ObservableList<Car> storedCars = FXCollections.observableArrayList();     // Change in ObservableList automatically updates GUI
    private final ObservableList<Car> raceCars = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        FilteredList<Car> availableCars = new FilteredList<>(storedCars);       // Filter for ObservableList
        availableCars.setPredicate(car -> !raceCars.contains(car));     // If there is no car in race, then set it as available
        storedCarsComboBox.setItems(availableCars);
        raceCarsListView.setItems(raceCars);

        mainBox.visibleProperty().bind(Bindings.isNotEmpty(storedCars));    // Visibility of mainBox and emptyStateBox is only dependent from storedCars content
        emptyStateBox.visibleProperty().bind(Bindings.isEmpty(storedCars));
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onCreateCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/add_car.fxml"));
            Stage stage = new Stage();  // Window
            stage.setScene(new Scene(loader.load()));   // Window content
            stage.setTitle("Add new car");
            stage.setMinWidth(1200);
            stage.setMinHeight(600);
//            AddCarController controller = loader.getController();
//            controller.setOnCarCreated(storedCars::add);    // Add new car to storedCars
            stage.show();   // Creating window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    @FXML
    private void onCarSelected() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            raceCars.add(selected);
            storedCarsComboBox.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void onRemoveFromRace() {
        Car selected = raceCarsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            raceCars.remove(selected);
        }
    }

    private AddCarController addCarController;



    @FXML
    private void onDeleteCar() {
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            storedCars.remove(selected);
            raceCars.remove(selected);
        }
    }

    @FXML
    private void onStartRace(ActionEvent event) {
        if (raceCars.size() < 2) {
            showAlert("Wyścig wymaga minimum 2 samochodów");
            return;
        }

        System.out.println("START WYŚCIGU: " + raceCars);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/simulatorgui/view/main.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 600);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
            stage.setTitle("Car Simulator – Race");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}
