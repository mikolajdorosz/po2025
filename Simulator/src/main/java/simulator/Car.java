package simulator;

import java.util.ArrayList;
import java.util.List;

public class Car extends Thread {
    private boolean isRunning;
    private String plateNumber;
    private String model;
    private double weight;
    private int vMax;
    private Position position;
    private Engine engine;
    private Gearbox gearbox;
    private boolean playerControlled;
    private double speed;
    private boolean isGasPressed;
    private boolean isBrakePressed;
    private int currentCheckpointIndex = 0;
    private double raceTime = 0;
    private boolean finished = false;

    public Car(String plateNumber, String model, double weight, int vMax, Position position, Engine engine, Gearbox gearbox) {
        this.isRunning = false;
        this.plateNumber = plateNumber;
        this.model = model;
        this.weight = weight;
        this.vMax = vMax;
        this.position = position;
        this.engine = engine;
        this.gearbox = gearbox;
        this.playerControlled = false;
        this.isGasPressed = false;
        this.isBrakePressed = false;
    }

    public boolean getIsRunning() {
        return isRunning;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public String getModel() {
        return model;
    }
    public double getWeight() { return weight + engine.getWeight() + gearbox.getWeight(); }
    public Position getPosition() { return position; }
    public void setPosition(Position position) {
        this.position = position;
    }
    public Engine getEngine() { return engine; }
    public Gearbox getGearbox() { return gearbox; }
    public boolean getPlayerControlled() { return playerControlled; }
    public void setPlayerControlled(boolean playerControlled) { this.playerControlled = playerControlled; }
    public int getSpeed() {
        return (int) speed;
    }
    public boolean getIsGasPressed() {
        return isGasPressed;
    }
    public void setIsGasPressed(boolean isThrottlePressed) {
        this.isGasPressed = isThrottlePressed;
    }
    public boolean getIsBrakePressed() {
        return isBrakePressed;
    }
    public void setIsBrakePressed(boolean isBrakePressed) {
        this.isBrakePressed = isBrakePressed;
    }
    public int getCurrentCheckpointIndex() {
        return currentCheckpointIndex;
    }
    public double getRaceTime() {
        return raceTime;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public boolean hasFinished() {
        return finished;
    }

    public void start() {
        isRunning = true;
        engine.start();
    }
    public void turnOff() {
        engine.stop();
        gearbox.setCurrentGear(0);
        isRunning = false;
    }
    public void goTo(double dt, Position destination) {
        if (!isRunning) return;
        boolean clutchPressed = gearbox.getClutch().getIsPressed();
        engine.update(dt, isGasPressed);

        boolean isAutomatic = gearbox.getType().equals("automatic");

        if (isBrakePressed) {
            applyBraking(dt);
        } else if (isGasPressed) {
            if (!clutchPressed || isAutomatic) {
                // auto accelerates if clutch is not pressed OR car is automatic
                double targetSpeed = computeSpeed();
                double accel = 100 * dt;
                if (speed < targetSpeed) {
                    speed = Math.min(speed + accel, targetSpeed);
                } else {
                    speed = Math.max(speed - accel, targetSpeed);
                }
            } else {
                // clutch pressed → manual, speed almost constant
                speed *= (1.0 - 0.01 * dt);
            }
        } else {
            applyRollingResistance(dt);
        }


        if (isBrakePressed && !isGasPressed) engine.decreaseRPM();
        position.moveTowards(destination, speed, dt);
    }


    private void applyRollingResistance(double dt) {
        speed *= (1.0 - 0.15 * dt);
    }

    private void applyBraking(double dt) {
        double speedBrakeFactor = 1.0 - (0.6 * dt);
        double rpmBrakeFactor   = 1.0 - (0.3 * dt);
        speed *= speedBrakeFactor;
        engine.setRPM((int) Math.max(engine.getMinRPM(), engine.getRPM() * rpmBrakeFactor));
        if (speed < 0.05) speed = 0;
    }

    private double computeSpeed() {
        int gear = gearbox.getCurrentGear();
        if (gear == 0) return 0;
        double rpmFactor = engine.getNormalizedRPM(); // 0..1
        double gearRatio = gearbox.getCurrentRatio();
        double speed = rpmFactor * (gear / (double)gearbox.getGearsNumber()) * vMax;
        return Math.min(speed, vMax);
    }
    public void onGearChanged(int oldGear, int newGear) {
        if (!isRunning) return;
        if (oldGear == 0 || newGear == 0) return;
        double rpmFactor = engine.getNormalizedRPM();

        if (newGear > oldGear) engine.setRPM((int)(rpmFactor * engine.getMaxRPM() * 0.6));
        else {
            engine.setRPM((int)(rpmFactor * engine.getMaxRPM() * 1.2));
            if (engine.getRPM() > engine.getMaxRPM()) engine.setRPM(engine.getMaxRPM());
        }
    }

    public void advanceCheckpoint() {
        currentCheckpointIndex++;
    }
    public void updateRaceTime(double dt) {
        if (!finished && isRunning) {
            raceTime += dt;
        }
    }

    @Override
    public String toString() {
        return model + " [" + plateNumber + "]";
    }

    private List<Listener> listeners = new ArrayList<>();
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }
}
