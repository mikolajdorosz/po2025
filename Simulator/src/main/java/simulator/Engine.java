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
    public int getRPM() {
        return RPM;
    }


    public void start() {
        RPM = minRPM;
        System.out.println("Engine fired off...");
    }
    public void stop() {
        RPM = 0;
        System.out.println("Engine stopped.");
    }
    public void increaseRPM() {
        RPM += dRPM;
        if (RPM > maxRPM) {
            RPM = maxRPM;
        }
    }
    public void decreaseRPM() {
        RPM -= dRPM;
        if (RPM < minRPM) {
            RPM = minRPM;
        }
    }

}
