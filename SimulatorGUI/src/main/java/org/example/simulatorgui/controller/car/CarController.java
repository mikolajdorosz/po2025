package org.example.simulatorgui.controller.car;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.car.ICarListener;

public class CarController implements ICarListener {
    @FXML private HBox tileHBox;
    @FXML private Label plateNumberLabel;
    @FXML private Label modelLabel;
    @FXML private ImageView carImageView;

    private Car car;

    // ===================== SETUP =====================
    public void setCar(Car car) {
        if (this.car != null) this.car.removeListener(this);
        this.car = car;
        car.addListener(this);
    }

    // ===================== UPDATE =====================
    public void refresh() {
        if (car == null) return;
        tileHBox.setVisible(true);
        tileHBox.setManaged(true);
        plateNumberLabel.setText(car.getPlateNumber());
        modelLabel.setText(car.getModel());
        carImageView.setImage(car.getImage());
    }
    @Override public void onCarUpdated(Car car) { refresh(); }
}
