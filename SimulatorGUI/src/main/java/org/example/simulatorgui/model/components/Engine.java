package model.components;

public class Engine extends Component {
    private int RPM;
    private int minRPM;
    private int maxRPM;

    public Engine(int maxRPM, String name, double weight, double price) {
        super(name, weight, price);
        this.RPM = 0;
        this.minRPM = 1000;
        this.maxRPM = maxRPM;
    }

    public int getRPM() { return RPM; }
    public int getMinRPM() { return minRPM; }
    public int getMaxRPM() { return maxRPM; }
    public double getNormalizedRPM() { return (double) (RPM - minRPM) / (maxRPM - minRPM); }

    public void setRPM(int RPM) { this.RPM = Math.max(minRPM, Math.min(RPM, maxRPM)); }

    public void start() { RPM = minRPM; }
    public void stop() { RPM = 0; }
    public void manipulateGas(double deltaTime, boolean gasPressed) {
        double rpmChange;
        if (gasPressed) rpmChange = 1500 * deltaTime;       // RPM increase factor
        else rpmChange = -500 * deltaTime;                  // RPM decrease factor
        setRPM((int)(RPM + rpmChange));
    }
    public double manipulateBrake(double deltaTime, boolean brakePressed, double carWeight) {
        if (!brakePressed) return 0;
        return (8000 / carWeight) * deltaTime;
    }
    public void zeroRPM() { this.RPM = 0; }
}
