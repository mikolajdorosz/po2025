package org.example.simulatorgui.controller.maincomponent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import simulator.Car;
import simulator.Listener;
import simulator.Utils;

public class CarTileController implements Listener {
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
    private int lastPosition;
    private double lastRaceTime;
    private int lastScore;
    private int lastSpeed;
    private int lastRPM;
    private int lastGear;

    public void setCar(Car car) {
        this.car = car;
        updateCarTile(car, 1);
        car.addListener(() -> Platform.runLater(() -> updateCarTile(car, lastPosition)));
    }

    public void updateCarTile(Car car, int position) {
        if (!car.getFinished()) {
            lastPosition = position;
            lastRaceTime = car.getRaceTime();
            lastScore = car.getFinalScore();
            lastSpeed = car.getSpeed();
            lastRPM = car.getEngine().getRPM();
            lastGear = car.getGearbox().getCurrentGear();
        }
        plateNumberLabel.setText(car.getPlateNumber());
        modelLabel.setText(car.getModel());
        if (car.getCarImageView() != null)  carImageView.setImage(car.getCarImageView().getImage());
        positionLabel.setText(String.valueOf(lastPosition));
        timeLabel.setText(Utils.formatTime(car.getRaceTime()));
        scoreLabel.setText(String.valueOf(lastScore));
        rpmLabel.setText(String.valueOf(lastRPM));
        speedLabel.setText(String.valueOf(lastSpeed));
        gearLabel.setText(String.valueOf(lastGear));

        if (car.getPlayerControlled()) {
            playerControlledLabel.setVisible(true);
            playerControlledLabel.setManaged(true);
        } else {
            playerControlledLabel.setVisible(false);
            playerControlledLabel.setManaged(false);
        }
    }
    @Override
    public void update() { Platform.runLater(() -> updateCarTile(car, lastPosition)); }
}
