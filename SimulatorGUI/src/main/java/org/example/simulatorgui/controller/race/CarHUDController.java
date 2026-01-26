package org.example.simulatorgui.controller.race;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.simulatorgui.model.car.Car;

public class CarHUDController {
    @FXML private Label rpmLabel;
    @FXML private Label speedLabel;
    @FXML private Label gearLabel;
    @FXML private Button carIgnitionButton;
    @FXML private Button clutchButton;
    @FXML private Button gearDownButton;
    @FXML private Button gearUpButton;
    @FXML private Button brakeButton;
    @FXML private Button gasButton;

    private Car car;

    // ===================== SETUP =====================
    public Car getCar() { return car; }
    public void setCar(Car car) {
        this.car = car;
        bindControls();
        refresh();
    }
    private void bindControls() {
        boolean isAutomatic = car.getGearbox().getType().equals("automatic");
        clutchButton.setDisable(isAutomatic);
        gearUpButton.setDisable(isAutomatic);
        gearDownButton.setDisable(isAutomatic);
    }

    // ===================== UPDATE =====================
    public void refresh() {
        if (car == null) return;
        Platform.runLater(() -> {
            rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
            speedLabel.setText(String.valueOf(car.getSpeed()));
            gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
            if (car.getFinalScore() != 0) disableAllControls();
        });
    }
    private void disableAllControls() {
        carIgnitionButton.setDisable(true);
        clutchButton.setDisable(true);
        gearDownButton.setDisable(true);
        gearUpButton.setDisable(true);
        brakeButton.setDisable(true);
        gasButton.setDisable(true);
    }

    // ===================== ACTIONS =====================
    @FXML private void onCarIgnition() {
        if (car.getRunning()) car.turnOff();
        else car.turnOn();
        if (car.getPlayerControlled() && car.getGearbox().getType().equals("automatic")) car.getGearbox().setCurrentGear(1);
        setButtonStyle(carIgnitionButton, car.getRunning());
        refresh();
    }
    @FXML private void onGearDown() {
        if (!car.getGearbox().getType().equals("manual")) return;
        car.getGearbox().gearDown();
        refresh();
        gearDownButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML private void onGearDownRelease() { gearDownButton.getStyleClass().setAll("btn", "btn-orange"); }
    @FXML private void onClutch() {
        if (!car.getGearbox().getType().equals("manual")) return;
        if (car.getGearbox().getClutch().getPressed()) car.getGearbox().getClutch().release();
        else car.getGearbox().getClutch().press();
        setButtonStyle(clutchButton, car.getGearbox().getClutch().getPressed());
    }
    @FXML private void onGas() {
        if (!car.getRunning()) return;
        car.setGasPressed(true);
        gasButton.getStyleClass().setAll("btn", "btn-grey");
        refresh();
    }
    @FXML private void onGasRelease() {
        if (!car.getRunning()) return;
        car.setGasPressed(false);
        gasButton.getStyleClass().setAll("btn", "btn-blue");
        refresh();
    }
    @FXML private void onBrake() {
        if (!car.getRunning()) return;
        car.setBrakePressed(true);
        brakeButton.getStyleClass().setAll("btn", "btn-grey");
        refresh();
    }
    @FXML private void onBrakeRelease() {
        if (!car.getRunning()) return;
        car.setBrakePressed(false);
        brakeButton.getStyleClass().setAll("btn", "btn-orange");
        refresh();
    }
    @FXML private void onGearUp() {
        if (!car.getGearbox().getType().equals("manual")) return;
        car.getGearbox().gearUp();
        refresh();
        gearUpButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML private void onGearUpRelease() { gearUpButton.getStyleClass().setAll("btn", "btn-blue"); }
    private void setButtonStyle(Button btn, boolean isActive) {
        btn.getStyleClass().setAll("btn", isActive ? "btn-grey" : "btn-blue");
    }

    // ===================== INPUT =====================
    public void registerInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (car.getFinalScore() != 0) return;
            switch (e.getCode()) {
                case W -> onGas();
                case S -> onBrake();
                case L -> onGearUp();
                case K -> onGearDown();
                case E -> onCarIgnition();
                case SHIFT -> {
                    if (car.getGearbox().getType().equals("automatic")) return;
                    car.getGearbox().getClutch().press();
                    clutchButton.getStyleClass().setAll("btn", "btn-grey");
                }
            }
        });
        scene.setOnKeyReleased(e -> {
            if (car.getFinalScore() != 0) return;
            switch (e.getCode()) {
                case W -> onGasRelease();
                case S -> onBrakeRelease();
                case L -> onGearUpRelease();
                case K -> onGearDownRelease();
                case SHIFT -> {
                    if (car.getGearbox().getType().equals("automatic")) return;
                    car.getGearbox().getClutch().release();
                    clutchButton.getStyleClass().setAll("btn", "btn-blue");
                }
            }
        });
    }
}
