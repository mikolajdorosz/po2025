package org.example.simulatorgui.model.race;

import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Position;
import org.example.simulatorgui.model.util.Utils;

import java.util.*;

public class Race {
    private static final double TARGET_EPSILON = 0.05;

    private final Position start;
    private final Position finish;
    private final List<Position> checkpoints;
    private final List<Car> cars;

    public Race(RaceSetup setup) {
        this.cars = List.copyOf(setup.cars());
        this.start = setup.start();
        this.finish = setup.finish();
        this.checkpoints = List.copyOf(setup.checkpoints());
        initializeCars();
    }

    public Position getStart() { return start; }
    public Position getFinish() { return finish; }
    public List<Position> getCheckpoints() { return checkpoints; }
    public List<Car> getCars() { return cars; }

    // ========================= LIFECYCLE  =========================
    private void initializeCars() {
        for (Car car : cars) {
            Position startPos = new Position(start.getX(), start.getY());
            car.setCurrentPosition(startPos);
            car.setStartingPosition(startPos);
            car.setCurrentCheckpoint(0);
            car.setFinished(false);
            car.setCurrentTarget(getTargetFor(car));
        }
    }
    public void updateProgress() {
        for (Car car : cars) {
            if (car.getFinished()) continue;
            if (hasReachedTarget(car.getCurrentPosition(), getTargetFor(car))) car.setCurrentCheckpoint(car.getCurrentCheckpoint() + 1);
            car.setCurrentTarget(getTargetFor(car));
        }
    }
    public void checkFinish() {
        for (Car car : cars) {
            if (!car.getFinished() && hasReachedTarget(car.getCurrentPosition(), finish)) {
                car.setFinished(true);
            }
        }
    }
    public boolean ended() { return cars.stream().allMatch(Car::getFinished); }
    public int calculatePoints(double time, double price) {
        if (time <= 0) time = 1;        // prevent division by zero
        if (price <= 0) price = 1;      // prevent division by zero
        double timeScore = 10000 / time;
        double priceScore = 5000 / price;
        return (int) Math.round(timeScore + priceScore);
    }

    // ========================= RACE RULES =========================
    private Position getTargetFor(Car car) {
        int index = car.getCurrentCheckpoint();
        return index < checkpoints.size() ? checkpoints.get(index) : finish;
    }
    private boolean hasReachedTarget(Position a, Position b) { return Math.abs(a.getX() - b.getX()) < TARGET_EPSILON
            && Math.abs(a.getY() - b.getY()) < TARGET_EPSILON; }

    // ========================= RANKING =========================
    public Map<Car, Integer> computeCarPositions() {
        List<Car> sorted = new ArrayList<>(cars);
        sorted.sort((car1, car2) -> Double.compare(Utils.distance(car1.getCurrentPosition(), finish), Utils.distance(car2.getCurrentPosition(), finish)));
        Map<Car, Integer> positions = new HashMap<>();
        for (int i = 0; i < sorted.size(); i++) positions.put(sorted.get(i), i + 1);
        return positions;
    }
}
