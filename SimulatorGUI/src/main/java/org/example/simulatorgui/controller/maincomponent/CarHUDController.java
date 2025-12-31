package org.example.simulatorgui.controller.maincomponent;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import simulator.Car;

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
    private AnimationTimer hudLoop;

    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
        disableClutch();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> updateHUD());
    }

    public void startHUDLoop() {
        hudLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //if (!car.getIsRunning()) return;
                updateHUD();
            }
        };
        hudLoop.start();
    }
    private void updateHUD() {
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onCarIgnition() {
        if (car.getIsRunning()) car.turnOff();
        else car.start();
        carIgnitionButton = setButtonStyle(carIgnitionButton, car.getIsRunning());
    }
    @FXML
    private void onGearDown() {
        int oldGear = car.getGearbox().getCurrentGear();
        car.getGearbox().gearDown(car.getEngine());
        car.onGearChanged(oldGear, car.getGearbox().getCurrentGear());
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        gearDownButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML
    private void onGearDownRelease() {
        gearDownButton.getStyleClass().setAll("btn", "btn-orange");
    }
    @FXML
    private void onClutch() {
        if (!car.getGearbox().getType().equals("manual")) return;
        if (car.getGearbox().getClutch().getIsPressed()) car.getGearbox().getClutch().release();
        else car.getGearbox().getClutch().press();
        clutchButton = setButtonStyle(clutchButton, car.getGearbox().getClutch().getIsPressed());
    }
    @FXML
    private void onGas() {
        if (!car.getIsRunning()) return;
        car.setIsGasPressed(true);
        gasButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML
    private void onGasRelease() {
        if (!car.getIsRunning()) return;
        car.setIsGasPressed(false);
        gasButton.getStyleClass().setAll("btn", "btn-blue");
    }
    @FXML
    private void onBrake() {
        if (!car.getIsRunning()) return;
        car.setIsBrakePressed(true);
        brakeButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML
    private void onBrakeRelease() {
        if (!car.getIsRunning()) return;
        car.setIsBrakePressed(false);
        brakeButton.getStyleClass().setAll("btn", "btn-orange");
    }
    @FXML
    private void onGearUp() {
        int oldGear = car.getGearbox().getCurrentGear();
        car.getGearbox().gearUp(car.getEngine());
        car.onGearChanged(oldGear, car.getGearbox().getCurrentGear());
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        gearUpButton.getStyleClass().setAll("btn", "btn-grey");
    }
    @FXML
    private void onGearUpRelease() {
        gearUpButton.getStyleClass().setAll("btn", "btn-blue");
    }

    private Button setButtonStyle(Button btn, boolean isActive) {
        if (isActive) btn.getStyleClass().setAll("btn", "btn-grey");
        else btn.getStyleClass().setAll("btn", "btn-blue");
        return btn;
    }
    private void disableClutch() {
        clutchButton.setDisable(car.getGearbox().getType().equals("automatic"));
    }
    public void registerInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
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
