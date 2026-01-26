package org.example.simulatorgui.model.race;

import org.example.simulatorgui.model.AIDriverController;
import org.example.simulatorgui.model.CarInitializer;
import org.example.simulatorgui.model.car.Car;

public class RaceEngine {
    private Race race;
    private boolean running;
    private final CarInitializer carInitializer = new CarInitializer();
    private final AIDriverController aiController = new AIDriverController();

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
        if (race == null) throw new IllegalStateException("Race must be created before starting");
        running = true;
        carInitializer.initialize(race);
    }
    public void update() {
        if (!running) return;
        race.updateProgress();
        aiController.updateAI(race);
        race.checkFinish();
    }
    public void stop() {
        running = false;
        race.getCars().forEach(Car::turnOff);
    }
}
