package model.components;

public class Clutch extends Component {
    private boolean pressed;

    public Clutch(String name, double weight, double price) {
        super(name, weight, price);
        this.pressed = false;
    }

    public Boolean getPressed() { return pressed; }

    public void press() { pressed = true; }
    public void release() { pressed = false; }
}
