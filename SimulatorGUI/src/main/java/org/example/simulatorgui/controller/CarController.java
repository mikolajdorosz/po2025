package org.example.simulatorgui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import simulator.*;

public class CarController {
    @FXML
    private TitledPane carPane;
    @FXML
    private TextField carModelTextField;
    @FXML
    private TextField carPlateNumberTextField;
    @FXML
    private TextField carWeightTextField;
    @FXML
    private TextField carVMAXTextField;
    @FXML
    private Button startCarButton;
    @FXML
    private Button turnOffCarButton;

    private Car activeCar;
    public void setActiveCar(Car car) {
        this.activeCar = car;
        System.out.println("Active car set to: " + car);
    }
    public Car getActiveCar() {
        return activeCar;
    }

    @FXML
    public void onStartCar() {
        if (activeCar != null) activeCar.start();
    }
    @FXML
    public void onTurnOffCar() {
        if (activeCar != null) activeCar.turnOff();
    }

    private EngineController engineController;
    private GearboxController gearboxController;

    public void setComponentsControllers(EngineController engineController, GearboxController gearboxController) {
        this.engineController = engineController;
        this.gearboxController = gearboxController;
    }

    public Car getCarFromInput() {
        String plateNumber = carPlateNumberTextField.getText();
        String model = carModelTextField.getText();
        double weight = Double.parseDouble(carWeightTextField.getText());
        int vMax = Integer.parseInt(carVMAXTextField.getText());
        Position position = new Position(0 , 0);
        Engine engine = engineController.getEngineFromInput();
        Gearbox gearbox = gearboxController.getGearboxFromInput();

        return new Car(plateNumber, model, weight, vMax, position, engine, gearbox);
    }
}
