package org.example.simulatorgui.controller;

import javafx.collections.ObservableList;
import simulator.Car;

public interface ICarRepository {
    ObservableList<Car> getRaceCars();
    ObservableList<Car> getStoredCars();
    boolean isDuplicatePlate(String plateNumber);
    void addCar(Car car);
    void selectCar(Car car);
}
