package org.example.simulatorgui.model.race;

import org.example.simulatorgui.model.car.Car;

public class RaceEngine {
    private Race race;
    private boolean running;

    public RaceEngine() { this.running = false; }

    public Race getRace() { return race; }
    public boolean getRunning() { return running; }

    public void createRace(RaceSetup setup) {
        race = new Race(
                setup.cars(),
                setup.start(),
                setup.finish(),
                setup.checkpoints()
        );
    }
    public void start() {
        running = true;
        initCars();
    }
    private void initCars() {
        for (Car car : race.getCars()) {
            car.resetCar(true);
            car.setCurrentTarget(
                race.getCheckpoints().isEmpty()
                    ? race.getFinish()
                    : race.getCheckpoints().getFirst()
            );
            if (!car.getPlayerControlled()) {
                car.turnOn();
                car.getGearbox().setCurrentGear(1);
            }
        }
    }
    public void update() {
        if (!running) return;
        race.updateProgress();
        for (Car car : race.getCars()) if (!car.getPlayerControlled()) car.aiDriving(car.getCurrentTarget());
        race.checkFinish();
    }
    public void stop() {
        running = false;
        for (Car car : race.getCars()) car.turnOff();
    }
}
