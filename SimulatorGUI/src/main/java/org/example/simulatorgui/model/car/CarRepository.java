package org.example.simulatorgui.model.car;

import javafx.collections.ObservableList;
import org.example.simulatorgui.config.RaceConfig;

public class CarRepository {
    private final RaceConfig config;

    public CarRepository(RaceConfig config) {
        this.config = config;
    }

    public ObservableList<Car> getRaceCars() { return config.getRaceCars(); }
    public ObservableList<Car> getStoredCars() { return config.getStoredCars(); }
    public void addCar(Car car) { config.getStoredCars().add(car); }
    public boolean isDuplicatePlate(String plate) {
        return config.getStoredCars().stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate))
                || config.getRaceCars().stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate));
    }
    public void selectCar(Car car) {
        // optionally set default selection
        // you can leave empty or implement as needed
    }
}
