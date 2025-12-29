package simulator;

public class Car {
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

        // Aktualizacja silnika
        engine.update(dt, isGasPressed);

        if (isBrakePressed) {
            applyBraking(dt);
        } else if (
                isGasPressed) {
            speed = computeSpeed();
        } else {
            applyRollingResistance(dt);
        }
        if (isBrakePressed && !isGasPressed) {
            engine.decreaseRPM();
        }

        position.moveTowards(destination, speed, dt);
    }
    private void applyRollingResistance(double dt) {
        speed *= (1.0 - 0.15 * dt); // opory toczenia
    }

    private void applyBraking(double dt) {
        double speedBrakeFactor = 1.0 - (0.6 * dt);   // ~60% / s
        double rpmBrakeFactor   = 1.0 - (0.3 * dt);   // ~30% / s

        speed *= speedBrakeFactor;

        engine.setRPM(
                (int) Math.max(engine.getMinRPM(),
                        engine.getRPM() * rpmBrakeFactor)
        );

        if (speed < 0.05) speed = 0;
    }

    private double computeSpeed() {
        int gear = gearbox.getCurrentGear();
        if (gear == 0) return 0;

        double rpmFactor = engine.getNormalizedRPM(); // 0..1
        double gearRatio = gearbox.getCurrentRatio();

        // im wyższy bieg → większa możliwa prędkość
        double gearSpeedFactor = 1.0 / gearRatio;

        double speed = rpmFactor * gearSpeedFactor * vMax;

        return Math.min(speed, vMax);
    }

    @Override
    public String toString() {
        return model + " [" + plateNumber + "]";
    }
}
