package org.example.simulatorgui.model.engine;

import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.race.Race;
import org.example.simulatorgui.model.race.RaceSetup;

public class RaceEngine {
    private long lastTime;
    private Race race;
    private boolean running;

    public RaceEngine() { this.running = false; }

    public Race getRace() { return race; }

    public void createRace(RaceSetup setup) {
        race = new Race(setup);
    }
    public void start() {
        if (race == null) throw new IllegalStateException("Race must be created before starting");
        running = true;
        for (Car car : race.getCars()) {
            car.resetCar(true);
            if (!car.getPlayerControlled()) {
                car.turnOn();
                car.getGearbox().setCurrentGear(1);
            }
        }
        lastTime = System.nanoTime();
    }
    public void update() {
        if (!running) return;
        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1e9;
        lastTime = now;
        race.updateProgress();
        for (Car car : race.getCars()) car.update(deltaTime);
        race.checkFinish();
    }
    public void stop() {
        running = false;
        race.getCars().forEach(Car::turnOff);
    }
}
