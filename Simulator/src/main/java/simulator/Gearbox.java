package simulator;

public class Gearbox extends Component {
    private Clutch clutch;
    private int currentGear;
    private int gearsNumber;
    private int currentRatio;
    private String type;

    public Gearbox() {
        super("", 0, 0);
    }
    public Gearbox(int gearsNumber, String name, double weight, double price, Clutch clutch) {
        super(name, weight, price);
        this.clutch = clutch;
        this.currentGear = 0;
        this.gearsNumber = gearsNumber;
        this.currentRatio = 2;
    }
    public Gearbox(int gearsNumber, String name, double weight, double price, Clutch clutch, int currentGear) {
        super(name, weight, price);
        this.clutch = clutch;
        this.currentGear = currentGear;
        this.gearsNumber = gearsNumber;
        this.currentRatio = 2;
    }

    public void gearUp() {
        clutch.press();
        if (currentGear < gearsNumber) {
            currentGear += 1;
            currentRatio -= 0.25;
        }
        clutch.release();
        System.out.println("Current gear: " + getCurrentGear());
    }
    public void gearDown() {
        clutch.press();
        if (currentGear > 0) {
            currentGear -= 1;
            currentRatio += 0.25;
        }
        clutch.release();
        System.out.println("Current gear: " + getCurrentGear());
    }

    @Override
    public double getWeight() { return super.getWeight() + clutch.getWeight(); }
    public int getCurrentGear() { return currentGear; }
    public int getCurrentRatio() { return currentRatio; }

    public void setClutch(Clutch clutch) {
        this.clutch = clutch;
    }

    public void setType(String type) {
        this.type = type;
    }
}
