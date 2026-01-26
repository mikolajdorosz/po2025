package org.example.simulatorgui.controller.race;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.car.ICarListener;
import org.example.simulatorgui.model.util.Utils;

public class CarController implements ICarListener {
    @FXML private Label plateNumberLabel;
    @FXML private Label modelLabel;
    @FXML private Label positionLabel;
    @FXML private Label timeLabel;
    @FXML private Label scoreLabel;
    @FXML private Label speedLabel;
    @FXML private Label rpmLabel;
    @FXML private Label gearLabel;
    @FXML private Label playerControlledLabel;

    private Car car;
    private int lastPosition = 1;
    private double lastRaceTime;
    private int lastScore;
    private int lastSpeed;
    private int lastRPM;
    private int lastGear;

    // ===================== SETUP =====================
    public void setCar(Car car) {
        if (this.car != null) this.car.removeListener(this);
        this.car = car;
        car.addListener(this);
    }

    // ===================== UPDATE =====================
    public void refresh(int position) {
        if (car == null) return;
        if (!car.getFinished()) {
            lastPosition = position;
            lastRaceTime = car.getRaceTime();
            lastScore = car.getFinalScore();
            lastSpeed = car.getSpeed();
            lastRPM = car.getEngine().getRpm();
            lastGear = car.getGearbox().getCurrentGear();
        }
        updateView();
    }
    private void updateView() {
        plateNumberLabel.setText(car.getPlateNumber());
        modelLabel.setText(car.getModel());
        positionLabel.setText(String.valueOf(lastPosition));
        timeLabel.setText(Utils.formatTime(lastRaceTime));
        scoreLabel.setText(String.valueOf(lastScore));
        speedLabel.setText(String.valueOf(lastSpeed));
        rpmLabel.setText(String.valueOf(lastRPM));
        gearLabel.setText(String.valueOf(lastGear));

        boolean isPlayer = car.getPlayerControlled();
        playerControlledLabel.setVisible(isPlayer);
        playerControlledLabel.setManaged(isPlayer);
    }

    @Override public void onCarUpdated(Car car) { refresh(lastPosition); }
    @Override public void onCarFinished(Car car) { refresh(lastPosition); }
}
