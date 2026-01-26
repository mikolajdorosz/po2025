package org.example.simulatorgui.model;

import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.race.Race;

public class AIDriverController {

    public void updateAI(Race race) {
        race.updateProgress();
        for (Car car : race.getCars()) {
            if (!car.getPlayerControlled()) {
                car.aiDriving(car.getCurrentTarget());
            }
            race.checkFinish();
        }
    }
}
