package org.example.simulatorgui.controller.competition;

import javafx.animation.AnimationTimer;
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

    private Car car;
    private boolean isGasPressed = false;
    private boolean isBrakePressed = false;
    private AnimationTimer hudLoop;

    public void startHUDLoop() {
        hudLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!car.getIsRunning()) return;

                if (isGasPressed) {
                    car.getEngine().increaseRPM();
                }

                if (isBrakePressed) {
                    car.getEngine().decreaseRPM();
                }

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

    public void setCar(Car car) {
        this.car = car;
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
    }

    public void registerInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> {
                    isGasPressed = true;
                    isBrakePressed = false;
                }
                case S -> {
                    isBrakePressed = true;
                    isGasPressed = false;
                }
                case E -> car.getGearbox().gearUp(car.getEngine());
                case Q -> car.getGearbox().gearDown(car.getEngine());
            }
        });
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W -> isGasPressed = false;
                case S -> isBrakePressed = false;
            }
        });
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
        car.getGearbox().gearDown(car.getEngine());
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
    }
    @FXML
    private void onClutch() {
        if (car.getGearbox().getType().equals("manual")) {
            if (car.getGearbox().getClutch().getIsPressed()) car.getGearbox().getClutch().release();
            else car.getGearbox().getClutch().press();
            clutchButton = setButtonStyle(clutchButton, car.getGearbox().getClutch().getIsPressed());
        }
        else clutchButton.setDisable(true);
    }
    @FXML
    private void onGas() {
        car.setIsGasPressed(true);
        car.getEngine().increaseRPM();
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
    }
    @FXML
    private void onBrake() {
        car.setIsGasPressed(false);
        car.getEngine().decreaseRPM();
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
    }
    @FXML
    private void onGearUp() {
        car.getGearbox().gearUp(car.getEngine());
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
    }

    private Button setButtonStyle(Button btn, boolean isActive) {
        if (isActive) {
            btn.setStyle("-fx-background-color: #4b5563; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12; -fx-cursor: hand;");
        }
        return btn;
    }
}
