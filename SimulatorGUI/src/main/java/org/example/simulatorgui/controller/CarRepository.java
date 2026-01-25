package org.example.simulatorgui.controller;

import javafx.collections.ObservableList;
import simulator.Car;

public class CarRepository implements ICarRepository {
    private final ObservableList<Car> storedCars;
    private final ObservableList<Car> raceCars;

    public CarRepository(ObservableList<Car> storedCars, ObservableList<Car> raceCars) {
        this.storedCars = storedCars;
        this.raceCars = raceCars;
    }

    @Override public ObservableList<Car> getRaceCars() { return raceCars; }
    @Override public ObservableList<Car> getStoredCars() { return storedCars; }
    @Override public void addCar(Car car) { storedCars.add(car); }
    @Override public boolean isDuplicatePlate(String plate) {
        return storedCars.stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate))
                || raceCars.stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate));
    }
    @Override public void selectCar(Car car) {
        // optionally set default selection
        // you can leave empty or implement as needed
    }
}
