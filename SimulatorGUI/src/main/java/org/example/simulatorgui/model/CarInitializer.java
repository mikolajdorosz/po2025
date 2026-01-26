package org.example.simulatorgui.model;

import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.race.Race;
import org.example.simulatorgui.model.util.Position;

public class CarInitializer {

    public void initialize(Race race) {
        for (Car car : race.getCars()) {
            car.resetCar(true);
            car.setCurrentTarget(determineInitialTarget(race));

            if (!car.getPlayerControlled()) {
                car.turnOn();
                car.getGearbox().setCurrentGear(1);
            }
        }
    }
    private Position determineInitialTarget(Race race) {
        return race.getCheckpoints().isEmpty()
                ? race.getFinish()
                : race.getCheckpoints().getFirst();
    }
}
