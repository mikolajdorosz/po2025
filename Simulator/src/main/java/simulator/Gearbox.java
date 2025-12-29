package simulator;

public class Gearbox extends Component {
    private int gearsNumber;
    private String type;
    private Clutch clutch;
    private int currentGear;
    private final double[] gearRatios = {
            0.0, 3.6, 2.19, 1.41, 1.12, 0.93, 0.81
    };

    public Gearbox(int gearsNumber, String type, String name, double weight, double price, Clutch clutch) {
        super(name, weight, price);
        this.gearsNumber = gearsNumber;
        this.type = type;
        this.clutch = clutch;
        this.currentGear = 0;
    }
    public Gearbox(int gearsNumber, String type, String name, double weight, double price) {
        super(name, weight, price);
        this.gearsNumber = gearsNumber;
        this.type = type;
        this.currentGear = 0;
    }

    @Override
    public double getWeight() { return super.getWeight() + clutch.getWeight(); }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Clutch getClutch() {
        return clutch == null ? null : clutch;
    }
    public void setClutch(Clutch clutch) {
        this.clutch = clutch;
    }
    public int getCurrentGear() { return currentGear; }
    public void setCurrentGear(int currentGear) {
        this.currentGear = currentGear;
    }
    public double getCurrentRatio() {
        return gearRatios[currentGear];
    }

    public void gearUp(Engine engine) {
        if (currentGear >= gearRatios.length - 1) return;
        if (type.equals("manual") && clutch.getIsPressed()) {
            double oldRatio = gearRatios[currentGear];
            currentGear++;
            double newRatio = gearRatios[currentGear];
            if (!clutch.getIsPressed()) {
                if (currentGear == 1) engine.setRPM((int)engine.getMinRPM());
                else engine.setRPM((int)(engine.getRPM() * newRatio / oldRatio));
            }
        } else if (type.equals("automatic")) {
            double oldRatio = gearRatios[currentGear];
            currentGear++;
            double newRatio = gearRatios[currentGear];
            engine.setRPM((int)(engine.getRPM() * newRatio / oldRatio));
        }
    }
    public void gearDown(Engine engine) {
        if (currentGear <= 1) return;
        if (type.equals("manual") && clutch.getIsPressed()) {
            double oldRatio = gearRatios[currentGear];
            currentGear--;
            double newRatio = gearRatios[currentGear];
            if (!clutch.getIsPressed()) engine.setRPM((int)(engine.getRPM() * newRatio / oldRatio));
        } else if (type.equals("automatic")) {
            double oldRatio = gearRatios[currentGear];
            currentGear--;
            double newRatio = gearRatios[currentGear];
            engine.setRPM((int)(engine.getRPM() * newRatio / oldRatio));
        }
    }
}
