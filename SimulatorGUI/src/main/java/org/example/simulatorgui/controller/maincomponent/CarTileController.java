package org.example.simulatorgui.controller.maincomponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import simulator.Car;

public class CarTileController {
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
        speedLabel.setText(String.valueOf(car.getSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        if (car.getPlayerControlled()) {
            playerControlledLabel.setText("P1");
            playerControlledLabel.setVisible(true);
        } else playerControlledLabel.setVisible(false);
    }
    public void updateCarTile(Car car, int position) {
        plateNumberLabel.setText(car.getPlateNumber());
        modelLabel.setText(car.getModel());
        positionLabel.setText(String.valueOf(position)); // race position
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));
        timeLabel.setText(formatTime(car.getRaceTime()));

        if (car.getPlayerControlled()) {
            playerControlledLabel.setText("P1");
            playerControlledLabel.setVisible(true);
        } else {
            playerControlledLabel.setVisible(false);
        }
    }
    private String formatTime(double seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        int millis = (int) ((seconds - ((int) seconds)) * 1000); // milliseconds
        return String.format("%02d:%02d:%03d", mins, secs, millis);
    }

}
