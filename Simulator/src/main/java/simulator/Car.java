package simulator;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Car extends Thread {
    private static final double FIXED_DT = 0.016;
    private List<Listener> listeners = new ArrayList<>();
    private String plateNumber;
    private String model;
    private ImageView carImageView;
    private Position startingPosition;
    private Position currentPosition;
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
        this.raceTime = 0;
        this.maxSpeed = maxSpeed;
        this.currentCheckpoint = 0;
        this.currentScore = 0;
        start();
    }

    public String getPlateNumber() { return plateNumber; }
    public String getModel() { return model; }
    public ImageView getCarImageView() { return carImageView; }
    public Position getCurrentPosition() { return currentPosition; }
    public Position getCurrentTarget() { return currentTarget; }
    public Engine getEngine() { return engine; }
    public Gearbox getGearbox() { return gearbox; }
    public boolean getRunning() { return running; }
    public boolean getPlayerControlled() { return playerControlled; }
    public boolean getFinished() { return finished; }
    public double getWeight() {
        System.out.println(weight);
        System.out.println(engine.getWeight());
        System.out.println(gearbox.getWeight());
        System.out.println(gearbox.getClutch().getWeight());
        return weight + engine.getWeight() + gearbox.getWeight();
    }
    public double getRaceTime() { return raceTime; }
    public int getSpeed() { return (int) speed; }
    public int getCurrentCheckpoint() { return currentCheckpoint; }
    public int getFinalScore() { return finalScore; }

    public void setCarImageView(ImageView imageView) { this.carImageView = imageView; }
    public void setCurrentPosition(Position currentPosition) { this.currentPosition = currentPosition; }
    public void setStartingPosition(Position startingPosition) { this.startingPosition = startingPosition; }
    public void setCurrentTarget(Position currentTarget) { this.currentTarget = currentTarget; }
    public void setRunning(boolean running) { this.running = running; }
    public void setPlayerControlled(boolean playerControlled) { this.playerControlled = playerControlled; }
    public void setGasPressed(boolean gasPressed) { this.gasPressed = gasPressed; }
    public void setBrakePressed(boolean brakePressed) { this.brakePressed = brakePressed; }
    public void setFinished(boolean finished) {  this.finished = finished; }
    public void setRaceTime(double deltaTime) { if (!finished && running) this.raceTime = deltaTime; }
    public void setCurrentCheckpoint(int checkpoint) { this.currentCheckpoint = checkpoint; }
    public void setFinalScore(int score) { this.finalScore = score; }

    public void turnOn() {
        running = true;
        engine.start();
        Platform.runLater(this::notifyListeners);
    }
    public void turnOff() {
        engine.stop();
        if (finished) gearbox.setCurrentGear(0);
        speed = 0;
        running = false;
        Platform.runLater(this::notifyListeners);
    }
//    private void goTo(double deltaTime, Position destination) {
//        if (!running) return;
//        engine.manipulateGas(deltaTime, gasPressed);
//        if (brakePressed) applyBrake(deltaTime);
//        else if (gasPressed) applyAcceleration(deltaTime);
//        else speed *= 1.0 - 0.15 * deltaTime;             // gas released deceleration factor
//        if (gearbox.getType().equals("automatic")) applyGearChange();
//        currentPosition.moveTowards(destination, speed, deltaTime);
//        Platform.runLater(this::notifyListeners);
//    }
    private boolean aiChangingGear = false;
    private int targetGear = 0;
    private double clutchTimer = 0;
    private double minTimeBetweenShifts = 0.5; // AI waits at least 0.5s between shifts
    private double shiftCooldown = 0;

    public void aiManualGearChange(double deltaTime) {
        if (gearbox.getType().equals("manual")) {
            // Update cooldown timer
            if (shiftCooldown > 0) shiftCooldown -= deltaTime;

            // Only attempt new shift if cooldown is done
            if (!aiChangingGear && shiftCooldown <= 0) {
                double rpm = engine.getRPM();
                if (rpm > 0.8 * engine.getMaxRPM()) {
                    targetGear = Math.min(gearbox.getCurrentGear() + 1, gearbox.getGearsNumber());
                    aiChangingGear = true;
                    clutchTimer = 0;
                } else if (rpm < 0.3 * engine.getMaxRPM()) {
                    targetGear = Math.max(1, gearbox.getCurrentGear() - 1);
                    aiChangingGear = true;
                    clutchTimer = 0;
                }
            }

            // Handle gear change sequence
            if (aiChangingGear) {
                Clutch clutch = gearbox.getClutch();
                if (!clutch.getPressed()) {
                    clutch.press(); // Step 1: press clutch
                }

                clutchTimer += deltaTime;
                if (clutchTimer >= 0.3) { // wait 0.3s for realistic shift
                    gearbox.setCurrentGear(targetGear); // Step 2: change gear
                    clutch.release();                  // Step 3: release clutch
                    aiChangingGear = false;
                    shiftCooldown = minTimeBetweenShifts;
                }
            }
        }
    }

    private void syncRPMWithSpeed() {
    if (gearbox.getCurrentGear() <= 0) return;

    boolean clutchPressed =
            gearbox.getType().equals("manual")
                    && gearbox.getClutch().getPressed();

    if (clutchPressed) return;

    double rpm =
            (speed / maxSpeed)
                    * gearbox.getGearsNumber()
                    / gearbox.getCurrentGear()
                    * engine.getMaxRPM();

    engine.setRPM((int) rpm);
}

    private void goTo(double deltaTime, Position destination) {
        if (!running) return;

        // GAS / ENGINE
        engine.manipulateGas(deltaTime, gasPressed);
        if (!playerControlled && gearbox.getType().equals("manual")) {
            aiManualGearChange(deltaTime);
        }

        // BRAKE
        if (brakePressed) {
            applyBrake(deltaTime);
        } else if (gasPressed) {
            applyAcceleration(deltaTime);
        } else {
            speed *= 1.0 - 0.15 * deltaTime;
        }

        // AUTOMATIC GEARBOX
        if (gearbox.getType().equals("automatic")) {
            applyGearChange();
        }

        // MOVE
        currentPosition.moveTowards(destination, speed, deltaTime);

        // 🔗 CRITICAL: sync RPM with wheel speed
        syncRPMWithSpeed();

        Platform.runLater(this::notifyListeners);
    }

//    private void applyBrake(double deltaTime) {
//        speed *= 1.0 - 0.6 * deltaTime;
//        engine.manipulateBrake(deltaTime, brakePressed);
//        if (speed < 1) speed = 0;
//    }
//
//    private void applyAcceleration(double deltaTime) {
//        boolean clutchPressed = gearbox.getClutch() != null && gearbox.getClutch().getPressed();
//        if (gearbox.getCurrentGear() > 0 && (!clutchPressed || gearbox.getType().equals("automatic"))) {
//            double targetSpeed = computeSpeed(); // gear-limited speed
//            double acceleration = 100 * deltaTime; // acceleration factor
//            speed = Math.min(speed + acceleration, targetSpeed);
//        } else speed *= 1.0 - 0.01 * deltaTime; // deceleration factor
//    }
    private void applyBrake(double deltaTime) {
        double deceleration = engine.manipulateBrake(deltaTime, brakePressed, weight);
        speed = Math.max(0, speed - deceleration);
    }
//    private void applyAcceleration(double deltaTime) {
//        boolean clutchPressed = gearbox.getType().equals("manual") && gearbox.getClutch().getPressed();
//        if (gearbox.getCurrentGear() > 0 && (!clutchPressed || gearbox.getType().equals("automatic"))) {
//            double targetSpeed = computeSpeed();        // gear-limited speed
//            double acceleration = 5000 / weight * deltaTime;     // acceleration factor
//            speed = Math.min(speed + acceleration, targetSpeed);
//        } else speed *= 1.0 - 0.01 * deltaTime;            // deceleration factor
//    }
private void applyAcceleration(double deltaTime) {
    boolean clutchPressed =
            gearbox.getType().equals("manual") && gearbox.getClutch().getPressed();
    if (gearbox.getCurrentGear() > 0 && (!clutchPressed || gearbox.getType().equals("automatic"))) {
        double rpmFactor = Math.max(0.2, engine.getNormalizedRPM());
        double engineForce = 8000 * rpmFactor;
        double acceleration = engineForce / weight;

        double gearMaxSpeed = computeSpeed();
        speed = Math.min(speed + acceleration * deltaTime, gearMaxSpeed);
    }
}

    private double computeSpeed() {
        if (gearbox.getCurrentGear() <= 0) return 0;
        double gearRatio = (double) gearbox.getCurrentGear() / gearbox.getGearsNumber();
        return maxSpeed * gearRatio;
    }
    private void applyGearChange() {
        if (gearbox.getType().equals("automatic")) {
            if (engine.getRPM() > 0.8 * engine.getMaxRPM()) {
                gearbox.gearUp();
            } else if (engine.getRPM() < 0.3 * engine.getMaxRPM()) {
                gearbox.gearDown();
            }
        }
    }
    public void aiDriving(Position target) {
        double dist = Utils.distance(currentPosition, target);
        gasPressed = dist > 1 && Math.random() > 0.1;   // 90% gas if far enough
        brakePressed = Math.random() < 0.05;            // 5% chance to brake randomly
        applyGearChange();
    }

    public void addListener(Listener listener) { listeners.add(listener); }
    public void removeListener(Listener listener) { listeners.remove(listener); }
    private void notifyListeners() { listeners.forEach(Listener::update); }

    @Override
    public String toString() { return model + " [ " + plateNumber + " ]"; }
    @Override
    public void run() {
        lastTime = System.nanoTime();
        while (alive) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1e9;
            lastTime = now;
            if (running && currentTarget != null) {
                goTo(deltaTime, currentTarget);
                Platform.runLater(this::notifyListeners);
            }
            if (!finished) raceTime += deltaTime;
            currentScore = calculatePoints(raceTime, getPrice());
            if (currentTarget != null && Utils.distance(currentPosition, currentTarget) < 0.05) {
                finalScore = currentScore;
                // 🔹 Immediately notify HUD that car finished
                Platform.runLater(this::notifyListeners);
            }
            try { Thread.sleep((long)(FIXED_DT * 1000)); }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    public void resetCar(boolean forceReset) {
        if (!forceReset && finished) return;
        currentPosition.setPosition(startingPosition.getPosition());
        engine.zeroRPM();
        gearbox.setCurrentGear(0);
        if (gearbox.getClutch() != null) gearbox.getClutch().release();
        alive = true;
        running = false;
        gasPressed = false;
        brakePressed = false;
        finished = false;
        raceTime = 0;
        speed = 0;
        currentCheckpoint = 0;
        if (forceReset) finalScore = 0;

        Platform.runLater(this::notifyListeners);
    }
    public double getPrice() { return gearbox.getPrice() + engine.getPrice(); }
    public int calculatePoints(double time, double price) {
        if (time <= 0) time = 1;        // prevent division by zero
        if (price <= 0) price = 1;      // prevent division by zero
        double timeScore = 10000 / time;
        double priceScore = 5000 / price;
        return (int) Math.round(timeScore + priceScore);
    }
}
