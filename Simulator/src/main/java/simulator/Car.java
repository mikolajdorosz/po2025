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
    }

    public Boolean getIsRunning() {
        return isRunning;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public String getModel() {
        return model;
    }
    public double getWeight() { return weight + engine.getWeight() + gearbox.getWeight(); }
    public int getCurrentSpeed() {
        return 1;
    }
    public Position getPosition() { return position; }
    public Engine getEngine() { return engine; }
    public Gearbox getGearbox() { return gearbox; }
    public Boolean getPlayerControlled() { return playerControlled; }
    public void setPlayerControlled(boolean playerControlled) { this.playerControlled = playerControlled; }


    public void start() {
        isRunning = true;
        engine.start();
        System.out.println("Car is running...");
    }
    public void turnOff() {
        engine.stop();
        gearbox.gearDown();
        isRunning = false;
        System.out.println("Car turned off.");
    }
    public void goTo(double deltaTime, Position destination) {
        if (!isRunning) return;
        speed = computeSpeed();
        position.moveTowards(destination, speed, deltaTime);
    }

    private double computeSpeed() {
        double rpmFactor = engine.getRPM() / (double) engine.getMaxRPM();
        double gearFactor = gearbox.getCurrentGear();
        return Math.min(vMax, rpmFactor * gearFactor * 50);
    }
    @Override
    public String toString() {
        return model + " [" + plateNumber + "]";
    }
}
