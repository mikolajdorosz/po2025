package org.example.simulatorgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import org.example.simulatorgui.controller.form.CarController;
import org.example.simulatorgui.controller.form.ClutchController;
import org.example.simulatorgui.controller.form.EngineController;
import org.example.simulatorgui.controller.form.GearboxController;
import simulator.*;

import java.io.IOException;


public class MainController {
    @FXML
    private Button addCarButton;
    @FXML
    private Button deleteCarButton;
    @FXML
    private ComboBox<Car> carComboBox;
    @FXML
    private ImageView carImageView;

    private AddCarController addCarController;
    private final ObservableList<Car> cars = FXCollections.observableArrayList();

    public static void addCarToList(String model, String registration, double weight, int speed) {
    }

    @FXML
    public void onAddCar(ActionEvent actionEvent) throws IOException {
//        Car car = carPaneController.getCarFromInput();
//        cars.add(car);
//        carComboBox.getSelectionModel().select(car);
//        System.out.println("Added car: " + car);
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
        System.out.println("HelloController initialized");
        // Load and set the car image
//        Image carImage = new Image(Objects.requireNonNull(
//                getClass().getResource("/images/car.png")
//        ).toExternalForm());
//        System.out.println("Image width: " + carImage.getWidth() + ", height: " + carImage.getHeight());
//        carImageView.setImage(carImage);
//        carImageView.setFitWidth(30); // Set appropriate dimensions for your image
//        carImageView.setFitHeight(20);
//        carImageView.setTranslateX(0);
//        carImageView.setTranslateY(0);

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
