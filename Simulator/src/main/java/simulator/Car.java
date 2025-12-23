package simulator;

public class Car {
    private Position currentPosition;
    private Engine engine;
    private Gearbox gearbox;
    private boolean isRunning;
    private String plateNumber;
    private String model;
    private double weight;
    private int vMax;
    private boolean playerControlled;

    public boolean getPlayerControlled() { return this.playerControlled; }
    public void setPlayerControlled(boolean playerControlled) { this.playerControlled = playerControlled; }

    public Car(String plateNumber, String model, double weight, int vMax, Position position, Engine engine, Gearbox gearbox) {
        this.isRunning = false;
        this.plateNumber = plateNumber;
        this.model = model;
        this.weight = weight;
        this.vMax = vMax;
        this.currentPosition = position;
        this.engine = engine;
        this.gearbox = gearbox;
        this.playerControlled = false;
    }

    public void start() {
        engine.start();
        isRunning = true;
        System.out.println("Car is running...");
    }
    public void turnOff() {
        engine.stop();
        gearbox.gearDown();
        isRunning = false;
        System.out.println("Car turned off.");
    }
    public void goTo(Position destination) {}

    public String getModel() {
        return model;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public double getWeight() { return weight + engine.getWeight() + gearbox.getWeight(); }
    public int getCurrentV() {
        return 1;
    }
    public String getCurrentPosition() { return currentPosition.getPosition(); }
    public Gearbox getGearbox() { return gearbox; }

    @Override
    public String toString() {
        return model + " [" + plateNumber + "]";
    }
}
