package org.example.simulatorgui.repo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.components.Clutch;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.components.GearboxType;

public class CarRepository {
    private final ObservableList<Car> storedCars;
    private final ObservableList<Car> carsOnTrack;

    public CarRepository() throws InterruptedException {
        this.storedCars = FXCollections.observableArrayList();
        this.carsOnTrack = FXCollections.observableArrayList();
        storedCars.add(new Car("RPR01", "Ford Mustang", 650, 260,
            new Engine(6500, "2.3L EcoBoost", 180, 5000),
            new Gearbox(6, GearboxType.MANUAL, "6-Speed Manual", 120, 3000,
                    new Clutch("Standard Clutch", 20, 800))));
        storedCars.add(new Car("RPR02", "Ford F-250", 1000, 180,
            new Engine(3200, "Cummins 6.7L TurboDiesel", 350, 12000),
            new Gearbox(6, GearboxType.AUTOMATIC, "TorqShift 6R140", 180, 4000,
                    new Clutch("Heavy Duty Clutch", 25, 1200))));
    }

    public ObservableList<Car> getStoredCars() { return storedCars; }
    public ObservableList<Car> getCarsOnTrack() { return carsOnTrack; }
    public void addCar(Car car) { storedCars.add(car); }
    public boolean isDuplicatePlate(String plate) { return storedCars.stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plate)); }
}
