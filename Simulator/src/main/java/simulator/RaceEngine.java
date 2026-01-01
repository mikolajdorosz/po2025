package simulator;

public class RaceEngine {
    private final Race race;
    private boolean running;

    public RaceEngine(Race race) {
        this.race = race;
        this.running = false;
    }

    public Race getRace() { return race; }
    public boolean getRunning() { return running; }

    public void start() {
        running = true;
        Car player = race.getCars().stream().filter(Car::getPlayerControlled).findFirst().orElse(null);
        if (player != null) {
            player.setTarget(race.getCheckpoints().isEmpty() ? race.getFinish() : race.getCheckpoints().getFirst());
            if (!player.isAlive()) player.start();      // Thread
        }

        for (Car car : race.getCars()) {
            if (!car.getPlayerControlled()) {
                car.setTarget(race.getCheckpoints().isEmpty() ? race.getFinish() : race.getCheckpoints().getFirst());
                car.getGearbox().setCurrentGear(1);
                car.turnOn();
                if (!car.isAlive()) car.start();    // Thread
            }
        }
    }
    public void stop() {
        running = false;
        for (Car car : race.getCars()) {
            car.shutdown();
        }
    }
    public void update() {
        if (!running) return;
        race.updateTargets();
        race.checkFinish();
        if (race.ended()) stop();
    }
}
