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
import org.example.simulatorgui.dialog.CarWizardDialog;
import simulator.Car;

import java.io.IOException;

public class MenuController {

    // UI
    @FXML private ComboBox<Car> availableCarsComboBox;
    @FXML private ListView<Car> raceCarsListView;
    @FXML private VBox emptyStateBox;
    @FXML private VBox mainBox;

    // DATA
    private final ObservableList<Car> allCars = FXCollections.observableArrayList();
    private final ObservableList<Car> raceCars = FXCollections.observableArrayList();

    private FilteredList<Car> availableCars;

    @FXML
    private void initialize() {

        // ComboBox = wszystkie - te już w wyścigu
        availableCars = new FilteredList<>(allCars);
        availableCars.setPredicate(car -> !raceCars.contains(car));

        availableCarsComboBox.setItems(availableCars);
        raceCarsListView.setItems(raceCars);

        // Widoczność UI zależna od stanu
        mainBox.visibleProperty().bind(Bindings.isNotEmpty(allCars));
        emptyStateBox.visibleProperty().bind(Bindings.isEmpty(allCars));
    }

    // ===================== ACTIONS =====================

    @FXML
    private void onCarSelected() {
        Car selected = availableCarsComboBox.getValue();
        if (selected != null) {
            raceCars.add(selected);
            availableCarsComboBox.getSelectionModel().clearSelection();
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
    private void onCreateCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/add_car.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dodaj nowy samochód");

            AddCarController controller = loader.getController();
            controller.setOnCarCreated(car -> {
                allCars.add(car); // automatyczne dodanie do listy w MenuController
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteCar() {
        Car selected = availableCarsComboBox.getValue();
        if (selected != null) {
            allCars.remove(selected);
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
