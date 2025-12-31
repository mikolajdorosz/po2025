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

    public void setCurrentGear(int currentGear) { this.currentGear = currentGear; }

    public void gearUp(Engine engine) {
        if (currentGear >= gearsNumber) return;
        if (type.equals("manual")) {
            if (clutch.getPressed()) {
                currentGear++;
                if (currentGear == 1) engine.setRPM((int) engine.getMinRPM());
                else
                    engine.setRPM((int) (engine.getNormalizedRPM() * engine.getMaxRPM() * 0.7));    // gear up RPM decrease
            }
        } else {
            currentGear++;
            engine.setRPM((int) (engine.getNormalizedRPM() * engine.getMaxRPM() * 0.7));            // gear up RPM decrease
        }
    }
    public void gearDown(Engine engine) {
        if (currentGear <= 0) return;
        if (type.equals("manual")) {
            if (clutch.getPressed()) {
                currentGear--;
                engine.setRPM((int) (engine.getNormalizedRPM() * engine.getMaxRPM() * 1.25));   // gear down RPM increase
            }
        } else {
            currentGear--;
            engine.setRPM((int)(engine.getNormalizedRPM() * engine.getMaxRPM() * 1.25));        // gear down RPM increase
        }
    }
}
