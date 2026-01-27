package org.example.simulatorgui.model.car;

import javafx.application.Platform;
import org.example.simulatorgui.model.util.Position;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;

import java.util.ArrayList;
import java.util.List;

public class Car extends Thread {
    private final List<ICarListener> listeners = new ArrayList<>();
    private final CarPhysics physics;
    private final CarRaceStats stats;
    private String plateNumber;
    private String model;
    private Position startingPosition;
    private Position currentPosition;
    private Position currentTarget;
    private Engine engine;
    private Gearbox gearbox;
    private boolean alive;
    private boolean running;
    private boolean finished;
    private boolean playerControlled;
    private boolean gasPressed;
    private boolean brakePressed;
    private double weight;
    private double speed;
    private int maxSpeed;
    private int currentCheckpoint;
    private int currentScore;
    private int finalScore;
    private long lastTime;

    public Car(String plateNumber, String model, double weight, int maxSpeed, Position currentPosition, Engine engine, Gearbox gearbox) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.currentPosition = currentPosition;
        this.engine = engine;
        this.gearbox = gearbox;
        this.alive = true;
        this.running = false;
        this.playerControlled = false;
        this.gasPressed = false;
        this.brakePressed = false;
        this.finished = false;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.currentCheckpoint = 0;
        this.currentScore = 0;
        this.finalScore = 0;
        this.physics = new CarPhysics(this);
        this.stats = new CarRaceStats(this);

        start(); // starts thread
    }

    public String getPlateNumber() { return plateNumber; }
    public String getModel() { return model; }
    public Position getCurrentPosition() { return currentPosition; }
    public Position getCurrentTarget() { return currentTarget; }
    public Engine getEngine() { return engine; }
    public Gearbox getGearbox() { return gearbox; }
    public boolean getRunning() { return running; }
    public boolean getPlayerControlled() { return playerControlled; }
    public boolean getFinished() { return finished; }
    public double getWeight() {
        double clutchWeight = gearbox.getClutch() == null ? 0 : gearbox.getClutch().getWeight();
        return weight + engine.getWeight() + gearbox.getWeight() + clutchWeight;
    }
    public double getPrice() { return gearbox.getPrice() + engine.getPrice(); }
    public double getRaceTime() { return stats.getRaceTime(); }
    public int getSpeed() { return (int) speed; }
    public int getCurrentCheckpoint() { return currentCheckpoint; }
    public int getFinalScore() { return finalScore; }
    public boolean getGasPressed() { return gasPressed; }
    public boolean getBrakePressed() { return brakePressed; }
    public double getSpeedValue() { return speed; }
    public int getMaxSpeed() { return maxSpeed; }

    public void setCurrentPosition(Position currentPosition) { this.currentPosition = currentPosition; }
    public void setStartingPosition(Position startingPosition) { this.startingPosition = startingPosition; }
    public void setCurrentTarget(Position currentTarget) { this.currentTarget = currentTarget; }
    public void setPlayerControlled(boolean playerControlled) { this.playerControlled = playerControlled; }
    public void setGasPressed(boolean gasPressed) { this.gasPressed = gasPressed; }
    public void setBrakePressed(boolean brakePressed) { this.brakePressed = brakePressed; }
    public void setFinished(boolean finished) {
        this.finished = finished;
        if (finished) for (ICarListener listener : listeners) { listener.onCarFinished(this); }
    }
    public void setCurrentCheckpoint(int checkpoint) { this.currentCheckpoint = checkpoint; }
    public void setSpeedValue(double speed) { this.speed = speed; }

    // ========================= MAIN THREAD LOOP =========================
    @Override
    public void run() {
        while (alive) {
            synchronized (this) {
                try {
                    wait(); // waits for engine or input events
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            }
        }
    }

    // ========================= ENGINE-DRIVEN UPDATE =========================
    public void update(double deltaTime) {
        if (!running || finished) return;

        physics.update(deltaTime);
        stats.updateRaceTime(deltaTime);
        finalScore = stats.calculatePoints(stats.getRaceTime(), getPrice());
        Platform.runLater(this::notifyUpdated);
    }

    // ========================= CONTROL =========================
    public void turnOn() {
        running = true;
        engine.start();
        wakeUp();
    }
    public void turnOff() {
        engine.stop();
        gearbox.setCurrentGear(0);
        speed = 0;
        running = false;
        Platform.runLater(this::notifyUpdated);
    }
    public void finish() {
        setFinished(true);
        running = false;
        Platform.runLater(this::notifyFinished);
    }
    private void wakeUp() { synchronized (this) { notify(); } }

    // ========================= RESET =========================
    public void resetCar(boolean forceReset) {
        if (!forceReset && finished) return;
        currentPosition.setPosition(startingPosition.getPosition());
        engine.stop();
        gearbox.setCurrentGear(0);
        if (gearbox.getClutch() != null) gearbox.getClutch().release();
        alive = true;
        running = false;
        gasPressed = false;
        brakePressed = false;
        finished = false;
        speed = 0;
        currentCheckpoint = 0;
        if (forceReset) finalScore = 0;
        stats.setRaceTime(0);
        Platform.runLater(this::notifyUpdated);
    }

    public void addListener(ICarListener listener) { listeners.add(listener); }
    public void removeListener(ICarListener listener) { listeners.remove(listener); }
    private void notifyUpdated() { listeners.forEach(l -> l.onCarUpdated(this)); }
    private void notifyFinished() { listeners.forEach(l -> l.onCarFinished(this)); }
    @Override public String toString() { return model + " [ " + plateNumber + " ]"; }
}
