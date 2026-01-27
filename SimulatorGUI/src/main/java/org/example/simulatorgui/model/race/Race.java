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

    public Race(RaceSetup setup) { this(setup.cars(), setup.start(), setup.finish(), setup.checkpoints()); }
    public Race( List<Car> cars, Position start, Position finish, List<Position> checkpoints) {
        this.cars = List.copyOf(cars);
        this.start = start;
        this.finish = finish;
        this.checkpoints = List.copyOf(checkpoints);
        initializeCars();
    }

    public Position getStart() { return start; }
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
            // HERE IS FINISH LOGIC
            Position target = car.getCurrentTarget();
            if (target == null) {  car.setCurrentTarget(getTargetFor(car)); continue; }
            if (hasReachedTarget(car.getCurrentPosition(), target)) {
                car.setCurrentCheckpoint(car.getCurrentCheckpoint() + 1);
                car.setCurrentTarget(getTargetFor(car));
            }
        }
    }
    public void checkFinish() {
        for (Car car : cars) {
            if (!car.getFinished() && hasReachedTarget(car.getCurrentPosition(), finish)) car.finish();
        }
    }
    public boolean ended() { return cars.stream().allMatch(Car::getFinished); }

    // ========================= RACE RULES =========================
    private Position getTargetFor(Car car) {
        int index = car.getCurrentCheckpoint();
        return index < checkpoints.size() ? checkpoints.get(index) : finish;
    }
    private boolean hasReachedTarget(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) < TARGET_EPSILON && Math.abs(a.getY() - b.getY()) < TARGET_EPSILON;
    }

    // ========================= RANKING =========================
    public Map<Car, Integer> computeCarPositions() {
        List<Car> sorted = new ArrayList<>(cars);
        sorted.sort((car1, car2) -> Double.compare(Utils.distance(car1.getCurrentPosition(), finish), Utils.distance(car2.getCurrentPosition(), finish)));
        Map<Car, Integer> positions = new HashMap<>();
        for (int i = 0; i < sorted.size(); i++) positions.put(sorted.get(i), i + 1);
        return positions;
    }
}
