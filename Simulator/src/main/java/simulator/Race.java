package simulator;

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

        for (Car car : cars) {
            car.setPosition(new Position(start.getX(), start.getY()));
            car.setCurrentCheckpoint(0);
        }
    }

    public Position getStart() { return start; }
    public Position getFinish() { return finish; }
    public List<Position> getCheckpoints() { return checkpoints; }
    public List<Car> getCars() { return cars; }

    public void updateTargets() {
        for (Car car : cars) {
            if (car.getFinished()) continue;
            int index = car.getCurrentCheckpoint();
            if (index < checkpoints.size() && hasReached(car.getPosition(), checkpoints.get(index))) car.setCurrentCheckpoint(index + 1);
            Position target = getTargetForCar(car);
            car.setTarget(target);

            if (!car.getPlayerControlled()) car.setGasPressed(distance(car.getPosition(), target) > 1);
        }
    }
    private boolean hasReached(Position a, Position b) { return Math.abs(a.getX() - b.getX()) < 0.05 && Math.abs(a.getY() - b.getY()) < 0.05; }
    private Position getTargetForCar(Car car) {
        int index = car.getCurrentCheckpoint();
        if (index < checkpoints.size()) return checkpoints.get(index);
        return finish;
    }
    public Map<Car, Integer> computeCarPositions() {
        List<Car> carsCopy = new ArrayList<>(cars);
        carsCopy.sort((car1, car2) -> Double.compare(distance(car1.getPosition(), finish), distance(car2.getPosition(), finish)));
        Map<Car, Integer> positions = new HashMap<>();
        for (int i = 0; i < carsCopy.size(); i++) positions.put(carsCopy.get(i), i + 1);
        return positions;
    }

    private double distance(Position a, Position b) { return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2)); }
    public void checkFinish() {
        for (Car car : cars) {
            if (!car.getFinished() && hasReached(car.getPosition(), finish)) {
                car.setFinished(true);
                car.turnOff();
            }
        }
    }
    public boolean ended() { return cars.stream().allMatch(Car::getFinished); }
}
