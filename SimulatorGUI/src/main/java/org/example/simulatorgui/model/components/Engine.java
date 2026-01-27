package org.example.simulatorgui.model.components;

public class Engine extends Component {
    private static final int DEFAULT_MIN_RPM = 1000;
    private static final double GAS_RPM_INCREASE = 1500;
    private static final double NO_GAS_RPM_DECREASE = 500;

    private int rpm;
    private int minRpm;
    private int maxRpm;

    public Engine(int maxRpm, String name, double weight, double price) {
        super(name, weight, price);
        this.rpm = 0;
        this.minRpm = 1000;
        this.maxRpm = maxRpm;
    }

    public int getRpm() { return rpm; }
    public int getMaxRpm() { return maxRpm; }
    public double getNormalizedRPM() { return (double) (rpm - minRpm) / (maxRpm - minRpm); }

    public void setRpm(int rpm) { this.rpm = clamp(rpm); }

    // ========================= LIFECYCLE =========================
    public void start() { rpm = minRpm; }
    public void stop() { rpm = 0; }

    // ========================= ENGINE BEHAVIOR =========================
    public void updateRPM(double deltaTime, boolean gasPressed) {
        double rpmDelta = gasPressed ? GAS_RPM_INCREASE * deltaTime : -NO_GAS_RPM_DECREASE * deltaTime;
        setRpm((int) (rpm + rpmDelta));
    }

    // ========================= HELPERS =========================
    private int clamp(int value) { return Math.max(minRpm, Math.min(value, maxRpm)); }
}
