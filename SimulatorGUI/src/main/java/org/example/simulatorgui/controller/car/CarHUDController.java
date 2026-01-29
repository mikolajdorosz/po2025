package org.example.simulatorgui.controller.car;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.simulatorgui.model.components.GearboxType;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.car.ICarListener;

public class CarHUDController implements ICarListener {
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
        if (this.car != null) this.car.removeListener(this);
        this.car = car;
        if (this.car != null) {
            car.addListener(this);
            bindControls();
            refresh();
        }
    }
    private void bindControls() {
        boolean isAutomatic = car.getGearbox().getType().equals(GearboxType.AUTOMATIC);
        clutchButton.setDisable(isAutomatic);
        gearUpButton.setDisable(isAutomatic);
        gearDownButton.setDisable(isAutomatic);
    }

    // ===================== UPDATE =====================
    public void refresh() {
        if (car == null) return;
        rpmLabel.setText(String.valueOf(car.getEngine().getRpm()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        toggleControls();
    }
    private void toggleControls() {
        boolean readyToGo = car.getStartingPosition() == null || car.getCurrentTarget() == null;
        carIgnitionButton.setDisable(readyToGo);
        clutchButton.setDisable(readyToGo);
        gearDownButton.setDisable(readyToGo);
        gearUpButton.setDisable(readyToGo);
        brakeButton.setDisable(readyToGo);
        gasButton.setDisable(readyToGo);
    }

    // ===================== ACTIONS =====================
    @FXML private void onCarIgnition() {
        if (car == null) return;
        if (car.getRunning()) car.turnOff();
        else car.turnOn();
        refresh();
    }
    @FXML private void onGearDown() {
        if (car == null) return;
        if (!car.getGearbox().getType().equals(GearboxType.MANUAL)) return;
        car.getGearbox().gearDown();
        refresh();
        gearDownButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML private void onGearDownRelease() {
        if (car == null) return;
        refresh();
        gearDownButton.getStyleClass().setAll("btn", "btn-orange");
    }
    @FXML private void onClutch() {
        if (car == null) return;
        if (!car.getGearbox().getType().equals(GearboxType.MANUAL)) return;
        if (car.getGearbox().getClutch().getPressed()) car.getGearbox().getClutch().release();
        else car.getGearbox().getClutch().press();
        refresh();
        setButtonStyle(clutchButton, car.getGearbox().getClutch().getPressed());
    }
    @FXML private void onGas() {
        if (car == null) return;
        car.setGasPressed(true);
        gasButton.getStyleClass().setAll("btn", "btn-grey");
        refresh();
    }
    @FXML private void onGasRelease() {
        if (car == null) return;
        car.setGasPressed(false);
        gasButton.getStyleClass().setAll("btn", "btn-blue");
        refresh();
    }
    @FXML private void onBrake() {
        if (car == null) return;
        car.setBrakePressed(true);
        brakeButton.getStyleClass().setAll("btn", "btn-grey");
        refresh();
    }
    @FXML private void onBrakeRelease() {
        if (car == null) return;
        car.setBrakePressed(false);
        brakeButton.getStyleClass().setAll("btn", "btn-orange");
        refresh();
    }
    @FXML private void onGearUp() {
        if (car == null) return;
        if (!car.getGearbox().getType().equals(GearboxType.MANUAL)) return;
        car.getGearbox().gearUp();
        refresh();
        gearUpButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML private void onGearUpRelease() {
        if (car == null) return;
        refresh();
        gearUpButton.getStyleClass().setAll("btn", "btn-blue");
    }
    private void setButtonStyle(Button btn, boolean isActive) {
        btn.getStyleClass().setAll("btn", isActive ? "btn-grey" : "btn-blue");
    }
    @Override public void onCarUpdated(Car car) { Platform.runLater(this::refresh); }
}
