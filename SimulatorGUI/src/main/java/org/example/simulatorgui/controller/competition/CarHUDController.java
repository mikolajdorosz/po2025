package org.example.simulatorgui.controller.competition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import simulator.Car;

public class CarHUDController {
    @FXML private Label rpmLabel;
    @FXML private Label speedLabel;
    @FXML private Label gearLabel;
    private Car car;


    public void setCar(Car car) {
        this.car = car;
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getCurrentSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onCarIgnition() {
        if (car.getIsRunning()) car.turnOff();
        else car.start();
    }
    @FXML
    private void onGearDown() {
        car.getGearbox().gearDown();
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
    }
    @FXML
    private void onClutch() {
        if (car.getGearbox().getType().equals("manual")) {
            if (car.getGearbox().getClutch().getIsPressed()) car.getGearbox().getClutch().release();
            else car.getGearbox().getClutch().press();
        }
    }
    @FXML
    private void onGas() {
        car.getEngine().increaseRPM();
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getCurrentSpeed()));
    }
    @FXML
    private void onBrake() {
        car.getEngine().decreaseRPM();
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getCurrentSpeed()));
    }
    @FXML
    private void onGearUp() {
        car.getGearbox().gearUp();
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
    }
}
