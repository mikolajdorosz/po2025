package simulator;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Car extends Thread {
    private static final double FIXED_DT = 0.016;
    private List<Listener> listeners = new ArrayList<>();
    private String plateNumber;
    private String model;
    private Position position;
    private Position currentTarget;
    private Engine engine;
    private Gearbox gearbox;
    private boolean alive;
    private boolean running;
    private boolean playerControlled;
    private boolean gasPressed;
    private boolean brakePressed;
    private boolean finished;
    private double weight;
    private double raceTime;
    private double speed;
    private int maxSpeed;
    private int currentCheckpoint;

    public Car(String plateNumber, String model, double weight, int maxSpeed, Position position, Engine engine, Gearbox gearbox) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.position = position;
        this.engine = engine;
        this.gearbox = gearbox;
        this.alive = true;
        this.running = false;
        this.playerControlled = false;
        this.gasPressed = false;
        this.brakePressed = false;
        this.finished = false;
        this.weight = weight;
        this.raceTime = 0;
        this.maxSpeed = maxSpeed;
        this.currentCheckpoint = 0;
    }

    public String getPlateNumber() { return plateNumber; }
    public String getModel() { return model; }
    public Position getPosition() { return position; }
    public Engine getEngine() { return engine; }
    public Gearbox getGearbox() { return gearbox; }
    public boolean getRunning() { return running; }
    public boolean getPlayerControlled() { return playerControlled; }
    public boolean getFinished() { return finished; }
    public double getWeight() { return weight + engine.getWeight() + gearbox.getWeight(); }
    public double getRaceTime() { return raceTime; }
    public int getSpeed() { return (int) speed; }
    public int getCurrentCheckpoint() { return currentCheckpoint; }

    public void setPosition(Position position) { this.position = position; }
    public void setTarget(Position target) { this.currentTarget = target; }
    public void setPlayerControlled(boolean playerControlled) { this.playerControlled = playerControlled; }
    public void setGasPressed(boolean gasPressed) { this.gasPressed = gasPressed; }
    public void setBrakePressed(boolean brakePressed) { this.brakePressed = brakePressed; }
    public void setFinished(boolean finished) {  this.finished = finished; }
    public void setRaceTime(double deltaTime) { if (!finished && running) this.raceTime = deltaTime; }
    public void setCurrentCheckpoint(int checkpoint) { this.currentCheckpoint = checkpoint; }

    public void turnOn() {
        running = true;
        engine.start();
        Platform.runLater(this::notifyListeners);
    }
    public void turnOff() {
        engine.stop();
        gearbox.setCurrentGear(0);
        running = false;
        Platform.runLater(this::notifyListeners);
    }
    private void goTo(double deltaTime, Position destination) {
        if (!running) return;
        engine.manipulateGas(deltaTime, gasPressed);
        if (brakePressed) applyBraking(deltaTime);
        else if (gasPressed) applyAcceleration(deltaTime);
        else speed *= 1.0 - 0.15 * deltaTime;             // gas released deceleration factor
        position.moveTowards(destination, speed, deltaTime);
        Platform.runLater(this::notifyListeners);
    }
    private void applyBraking(double deltaTime) {
        speed *= 1.0 - 0.6 * deltaTime;
        engine.manipulateBrake(deltaTime, brakePressed);
        if (speed < 1) speed = 0;
    }
    private void applyAcceleration(double deltaTime) {
        boolean clutchPressed = gearbox.getClutch() != null && gearbox.getClutch().getPressed();
        if (gearbox.getCurrentGear() > 0 && (!clutchPressed || gearbox.getType().equals("automatic"))) {
            double targetSpeed = computeSpeed();        // gear-limited speed
            double acceleration = 100 * deltaTime;     // acceleration factor
            speed = Math.min(speed + acceleration, targetSpeed);
        } else speed *= 1.0 - 0.01 * deltaTime;            // deceleration factor
    }

    private double computeSpeed() {
        double targetSpeed = engine.getNormalizedRPM() * gearbox.getCurrentGear() / (double)gearbox.getGearsNumber() * maxSpeed;
        return Math.min(targetSpeed, maxSpeed);
    }

    public void addListener(Listener listener) { listeners.add(listener); }
    public void removeListener(Listener listener) { listeners.remove(listener); }
    private void notifyListeners() { listeners.forEach(Listener::update); }
    @Override
    public String toString() { return model + " [ " + plateNumber + " ]"; }
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        while (alive) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1e9;
            lastTime = now;
            if (running && currentTarget != null) {
                goTo(deltaTime, currentTarget);
                raceTime += deltaTime;
                Platform.runLater(this::notifyListeners);
            }
            try {
                Thread.sleep((long)(FIXED_DT * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public void shutdown() {
        alive = false;
        interrupt();
    }
}
