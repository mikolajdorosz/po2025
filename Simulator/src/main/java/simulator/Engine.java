package simulator;

public class Engine extends Component {
    private int maxRPM;
    private int RPM;
    private int dRPM;
    private int minRPM;

    public Engine(int maxRPM, String name, double weight, double price) {
        super(name, weight, price);
        this.maxRPM = maxRPM;
        this.RPM = 0;
        this.dRPM = 250;
        this.minRPM = 1000;
    }

    public int getMaxRPM() {
        return maxRPM;
    }
    public int getMinRPM() {
        return minRPM;
    }
    public double getNormalizedRPM() {
        return (double)(RPM - minRPM) / (maxRPM - minRPM);
    }
    public int getRPM() {
        return RPM;
    }
    public void setRPM(int rpm) {
        RPM = Math.max(minRPM, Math.min(rpm, maxRPM));
    }

    public void start() {
        RPM = minRPM;
    }
    public void stop() {
        RPM = 0;
    }
    public void increaseRPM() {
        RPM += dRPM;
        RPM = Math.min(RPM, maxRPM);
    }
    public void decreaseRPM() {
        RPM -= dRPM;
        RPM = Math.max(minRPM, RPM);
    }
    public void update(double dt, boolean gasPressed) {
        if (gasPressed) RPM += dRPM * dt;
        else RPM -= dRPM * 1.5 * dt;
        RPM = Math.max(minRPM, Math.min(RPM, maxRPM));
    }
}
