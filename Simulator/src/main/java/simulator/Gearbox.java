package simulator;

public class Gearbox extends Component {
    private int gearsNumber;
    private String type;
    private Clutch clutch;
    private int currentGear;
    private int currentRatio;

    public Gearbox(int gearsNumber, String type, String name, double weight, double price, Clutch clutch) {
        super(name, weight, price);
        this.gearsNumber = gearsNumber;
        this.type = type;
        this.clutch = clutch;
        this.currentGear = 0;
        this.currentRatio = 2;
    }
    public Gearbox(int gearsNumber, String type, String name, double weight, double price) {
        super(name, weight, price);
        this.gearsNumber = gearsNumber;
        this.type = type;
        this.currentGear = 0;
        this.currentRatio = 2;
    }

    @Override
    public double getWeight() { return super.getWeight() + clutch.getWeight(); }
    public String getType() {
        return type;
    }
    public Clutch getClutch() {
        return clutch == null ? null : clutch;
    }
    public int getCurrentGear() { return currentGear; }
    public int getCurrentRatio() { return currentRatio; }
    public void setType(String type) {
        this.type = type;
    }
    public void setClutch(Clutch clutch) {
        this.clutch = clutch;
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
        //clutch.press();
        if (currentGear > 0) {
            currentGear -= 1;
            currentRatio += 0.25;
        }
        //clutch.release();
        System.out.println("Current gear: " + getCurrentGear());
    }
}
