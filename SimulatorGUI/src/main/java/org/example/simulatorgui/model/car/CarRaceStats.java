package org.example.simulatorgui.model.car;

public class CarRaceStats {
    private final Car car;
    private double raceTime = 0;

    public CarRaceStats(Car car) { this.car = car; }

    public double getRaceTime() { return raceTime; }
    public void setRaceTime(double deltaTime) { raceTime = deltaTime; }

    public void updateRaceTime(double deltaTime) { if (!car.getFinished()) raceTime += deltaTime; }
    public int calculatePoints(double time, double price) {
        if (time <= 0) time = 1;
        if (price <= 0) price = 1;
        return (int) Math.round(10000 / time + 5000 / price);
    }
}
