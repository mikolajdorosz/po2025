package org.example.simulatorgui.controller;

import javafx.collections.ObservableList;
import org.example.simulatorgui.config.RaceConfig;
import simulator.Car;

public class CarRepository implements ICarRepository {
    private final RaceConfig config;

    public CarRepository(RaceConfig config) {
        this.config = config;
    }

    @Override public ObservableList<Car> getRaceCars() { return config.getRaceCars(); }
    @Override public ObservableList<Car> getStoredCars() { return config.getStoredCars(); }
    @Override public void addCar(Car car) { config.getStoredCars().add(car); }
    @Override public boolean isDuplicatePlate(String plate) {
        return config.getStoredCars().stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate))
                || config.getRaceCars().stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate));
    }
    @Override public void selectCar(Car car) {
        // optionally set default selection
        // you can leave empty or implement as needed
    }
}
