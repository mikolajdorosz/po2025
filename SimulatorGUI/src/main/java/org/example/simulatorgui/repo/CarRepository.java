package org.example.simulatorgui.repo;

import javafx.collections.ObservableList;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.race.RaceConfig;

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
}
