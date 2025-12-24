package org.example.simulatorgui.controller.competition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import simulator.Car;

public class CarParamsController {
    @FXML private Label plateNumberLabel;
    @FXML private Label modelLabel;
    @FXML private Label positionLabel;
    @FXML private Label timeLabel;
    @FXML private Label speedLabel;
    @FXML private Label rpmLabel;
    @FXML private Label gearLabel;
    @FXML private Label playerControlledLabel;

    public void setCar(Car car) {
        plateNumberLabel.setText(car.getPlateNumber());
        modelLabel.setText(car.getModel());
        positionLabel.setText("1");
        timeLabel.setText("--:--:--");
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getCurrentSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        if (car.getPlayerControlled()) {
            playerControlledLabel.setText("P1");
            playerControlledLabel.setVisible(true);
        } else playerControlledLabel.setVisible(false);
    }
}
