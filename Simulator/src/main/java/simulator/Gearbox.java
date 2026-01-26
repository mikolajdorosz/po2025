package simulator;

public class Gearbox extends Component {
    private String type;
    private Clutch clutch;
    private int gearsNumber;
    private int currentGear;

    public Gearbox(int gearsNumber, String type, String name, double weight, double price, Clutch clutch) {
        super(name, weight, price);
        this.type = type;
        this.clutch = clutch;
        this.gearsNumber = gearsNumber;
        this.currentGear = 0;
    }
    public Gearbox(int gearsNumber, String type, String name, double weight, double price) {
        super(name, weight, price);
        this.type = type;
        this.gearsNumber = gearsNumber;
        this.currentGear = 0;
    }

    public String getType() { return type; }
    public Clutch getClutch() { return clutch == null ? null : clutch; }
    public int getGearsNumber() { return gearsNumber; }
    public int getCurrentGear() { return currentGear; }
    @Override
    public double getWeight() { return super.getWeight() + clutch.getWeight(); }
    @Override
    public double getPrice() {
        if (clutch == null) return super.getPrice();
        return super.getPrice() + clutch.getPrice();
    }

    public void setCurrentGear(int currentGear) { this.currentGear = currentGear; }

    public void gearUp() {
        if (currentGear >= gearsNumber) return;
        if (type.equals("manual")) {
            if (clutch.getPressed()) currentGear++;
        } else currentGear++;
    }
    public void gearDown() {
        if (currentGear <= 1) return;
        if (type.equals("manual")) {
            if (clutch.getPressed()) currentGear--;
        } else currentGear--;
    }
}
