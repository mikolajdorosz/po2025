package org.example.simulatorgui.model.components;

public class Gearbox extends Component {
    private final GearboxType type;
    private final Clutch clutch;
    private final int gearsNumber;
    private int currentGear;

    public Gearbox(int gearsNumber, GearboxType type, String name, double weight, double price, Clutch clutch) {
        super(name, weight, price);
        this.type = type;
        this.clutch = clutch;
        this.gearsNumber = gearsNumber;
        this.currentGear = 0;
    }
    public Gearbox(int gearsNumber, GearboxType type, String name, double weight, double price) {
        this(gearsNumber, type, name, weight, price, null);
    }

    public GearboxType getType() { return type; }
    public Clutch getClutch() { return clutch; }
    public int getGearsNumber() { return gearsNumber; }
    public int getCurrentGear() { return currentGear; }
    @Override public double getWeight() { return super.getWeight() + (clutch == null ? 0 : clutch.getWeight()); }
    @Override public double getPrice() { return super.getPrice() + (clutch == null ? 0 : clutch.getPrice()); }

    public void setCurrentGear(int currentGear) { this.currentGear = currentGear; }

    public void gearUp() { if (canChangeGear()) currentGear = clamp(currentGear + 1); }
    public void gearDown() { if (canChangeGear()) currentGear = clamp(currentGear - 1); }

    private boolean canChangeGear() {
        if (type == GearboxType.AUTOMATIC) return true;
        return clutch != null && clutch.getPressed();
    }
    private int clamp(int gear) {
        return Math.max(0, Math.min(gear, gearsNumber));
    }
}
