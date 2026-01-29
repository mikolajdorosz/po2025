package org.example.simulatorgui.model.car;

import javafx.application.Platform;
import javafx.scene.image.Image;
import org.example.simulatorgui.model.util.Position;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;

import java.util.ArrayList;
import java.util.List;

public class Car extends Thread {
    private static final double TARGET_EPSILON = 0.05;
    private final List<ICarListener> listeners = new ArrayList<>();
    private final CarPhysics physics = new CarPhysics(this);
    private final String plateNumber;
    private final String model;
    private final double weight;
    private final int maxSpeed;
    private final Gearbox gearbox;
    private final Engine engine;
    private boolean running = false;
    private boolean toggler = true;
    private Position startingPosition;
    private Position currentPosition;
    private Position currentTarget;
    private Image image;
    private boolean gasPressed = false;
    private boolean brakePressed = false;
    private double speed;

    public Car(String plateNumber, String model, double weight, int maxSpeed, Engine engine, Gearbox gearbox) throws InterruptedException {
        this.plateNumber = plateNumber;
        this.model = model;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.engine = engine;
        this.gearbox = gearbox;
        this.currentPosition = startingPosition;

        start(); // starts thread
    }

    public boolean getToggler() { return toggler; }
    public String getPlateNumber() { return plateNumber; }
    public String getModel() { return model; }
    public Position getCurrentPosition() { return currentPosition ; }
    public Position getCurrentTarget() { return currentTarget; }
    public Engine getEngine() { return engine; }
    public Gearbox getGearbox() { return gearbox; }
    public boolean getRunning() { return running; }
    public double getWeight() {
        double clutchWeight = gearbox.getClutch() == null ? 0 : gearbox.getClutch().getWeight();
        return weight + engine.getWeight() + gearbox.getWeight() + clutchWeight;
    }
    public double getPrice() { return gearbox.getPrice() + engine.getPrice(); }
    public int getSpeed() { return (int) speed; }
    public boolean getGasPressed() { return gasPressed; }
    public boolean getBrakePressed() { return brakePressed; }
    public double getSpeedValue() { return speed; }
    public int getMaxSpeed() { return maxSpeed; }
    public Image getImage() { return image; }
    public Position getStartingPosition() { return startingPosition; }
    @Override public String toString() { return model + " [ " + plateNumber + " ]"; }

    public void setStartingPosition(Position startingPosition) { this.startingPosition = startingPosition; notifyUpdated(); }
    public void setCurrentTarget(Position currentTarget) { this.currentTarget = currentTarget; }
    public void setGasPressed(boolean gasPressed) { this.gasPressed = gasPressed; }
    public void setBrakePressed(boolean brakePressed) { this.brakePressed = brakePressed; }
    public void setSpeedValue(double speed) { this.speed = speed; }
    public void setImage(Image image) { this.image = image; notifyUpdated(); }
    public void setToggler(boolean toggler) { this.toggler = toggler; }

    // ========================= MAIN THREAD LOOP =========================
    @Override public void run() {
        double deltaTime = 0.1;  // 100 ms
        while (true) {
            long start = System.nanoTime();

            if (currentTarget != null) update(deltaTime);

            long elapsed = System.nanoTime() - start;
            long sleepTime = 100_000_000 - elapsed; // 100 ms in ns
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }
    }

    // ========================= UPDATE =========================
    public void update(double deltaTime) {
        if (!running) return;
        currentPosition = physics.update(deltaTime);
        Platform.runLater(this::notifyUpdated);
        if (hasReachedTarget()) {
            startingPosition = currentTarget;
            currentTarget = null;
            toggler = false;
            turnOff();
        }
    }
    private boolean hasReachedTarget() {
        if (currentPosition == null) return false;
        return Math.abs(currentPosition.getX() - currentTarget.getX()) < TARGET_EPSILON && Math.abs(currentPosition.getY() - currentTarget.getY()) < TARGET_EPSILON;
    }

    // ========================= CONTROL =========================
    public void turnOn() {
        running = true;
        engine.start();
    }
    public void turnOff() {
        engine.stop();
        gearbox.setCurrentGear(0);
        speed = 0;
        running = false;
        Platform.runLater(this::notifyUpdated);
    }

    // ========================= LISTENER =========================
    public void addListener(ICarListener listener) { listeners.add(listener); }
    public void removeListener(ICarListener listener) { listeners.remove(listener); }
    private void notifyUpdated() { listeners.forEach(l -> l.onCarUpdated(this)); }
}
