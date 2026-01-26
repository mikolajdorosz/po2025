package org.example.simulatorgui.model.race;

import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Position;
import org.example.simulatorgui.model.util.Utils;

import java.util.*;

public class Race {
    private final Position start;
    private final Position finish;
    private final List<Position> checkpoints;
    private final List<Car> cars;

    public Race(List<Car> cars, Position start, Position finish, List<Position> checkpoints) {
        this.start = start;
        this.finish = finish;
        this.checkpoints = checkpoints;
        this.cars = cars;

        carsInitialPositions();
    }

    public Position getStart() { return start; }
    public Position getFinish() { return finish; }
    public List<Position> getCheckpoints() { return checkpoints; }
    public List<Car> getCars() { return cars; }

    private void carsInitialPositions () {
        for (Car car : cars) {
            car.setCurrentPosition(new Position(start.getX(), start.getY()));
            car.setStartingPosition(new Position(start.getX(), start.getY()));
            car.setCurrentCheckpoint(0);
        }
    }
    public void updateProgress() {
        for (Car car : cars) {
            if (car.getFinished()) continue;
            if (hasReachedTarget(car.getCurrentPosition(), getTarget(car))) car.setCurrentCheckpoint(car.getCurrentCheckpoint() + 1);
            car.setCurrentTarget(getTarget(car));
        }
    }
    private boolean hasReachedTarget(Position a, Position b) { return Math.abs(a.getX() - b.getX()) < 0.05 && Math.abs(a.getY() - b.getY()) < 0.05; }
    private Position getTarget(Car car) {
        int index = car.getCurrentCheckpoint();
        return index < checkpoints.size() ? checkpoints.get(index) : finish;
    }
    public Map<Car, Integer> computeCarPositions() {
        List<Car> carsCopy = new ArrayList<>(cars);
        carsCopy.sort((car1, car2) -> Double.compare(Utils.distance(car1.getCurrentPosition(), finish), Utils.distance(car2.getCurrentPosition(), finish)));
        Map<Car, Integer> positions = new HashMap<>();
        for (int i = 0; i < carsCopy.size(); i++) positions.put(carsCopy.get(i), i + 1);
        return positions;
    }
    public void checkFinish() {
        for (Car car : cars) {
            if (!car.getFinished() && hasReachedTarget(car.getCurrentPosition(), finish)) {
                car.setFinished(true);
            }
        }
    }
    public int calculatePoints(double time, double price) {
        if (time <= 0) time = 1;        // prevent division by zero
        if (price <= 0) price = 1;      // prevent division by zero
        double timeScore = 10000 / time;
        double priceScore = 5000 / price;
        return (int) Math.round(timeScore + priceScore);
    }

    public boolean ended() { return cars.stream().allMatch(Car::getFinished); }
}
