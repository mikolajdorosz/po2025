package org.example.simulatorgui.controller.maincomponent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import simulator.Car;
import simulator.Listener;
import simulator.Utils;

public class CarTileController implements Listener {
    @FXML private Label plateNumberLabel;
    @FXML private Label modelLabel;
    @FXML private Label positionLabel;
    @FXML private Label timeLabel;
    @FXML private Label speedLabel;
    @FXML private Label rpmLabel;
    @FXML private Label gearLabel;
    @FXML private Label playerControlledLabel;
    private Car car;
    private int currentPosition;

    public void setCar(Car car) {
        this.car = car;
        updateCarTile(car, 1);
        car.addListener(() -> Platform.runLater(() -> updateCarTile(car, currentPosition)));
    }
    public void setCurrentPosition(int position) { this.currentPosition = position; }

    public void updateCarTile(Car car, int position) {
        plateNumberLabel.setText(car.getPlateNumber());
        modelLabel.setText(car.getModel());
        positionLabel.setText(String.valueOf(position)); // race position
        timeLabel.setText(Utils.formatTime(car.getRaceTime()));
        rpmLabel.setText(String.valueOf(car.getEngine().getRPM()));
        speedLabel.setText(String.valueOf(car.getSpeed()));
        gearLabel.setText(String.valueOf(car.getGearbox().getCurrentGear()));

        if (car.getPlayerControlled()) {
            playerControlledLabel.setText("P");
            playerControlledLabel.setVisible(true);
        } else playerControlledLabel.setVisible(false);
    }
    @Override
    public void update() { Platform.runLater(() -> updateCarTile(car, currentPosition)); }
}
