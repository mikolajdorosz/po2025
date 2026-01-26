package org.example.simulatorgui.controller.race;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Utils;

public class CarController {
    @FXML private Label plateNumberLabel;
    @FXML private Label modelLabel;
    @FXML private ImageView carImageView;
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
    public void setCar(Car car) { this.car = car; refresh(1); }
    public void setCarImage(Image image) { carImageView.setImage(image); }

    // ===================== UPDATE =====================
    public void refresh(int position) {
        if (car == null) return;
        if (!car.getFinished()) {
            lastPosition = position;
            lastRaceTime = car.getRaceTime();
            lastScore = car.getFinalScore();
            lastSpeed = car.getSpeed();
            lastRPM = car.getEngine().getRPM();
            lastGear = car.getGearbox().getCurrentGear();
        }
        Platform.runLater(this::updateView);
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
}
