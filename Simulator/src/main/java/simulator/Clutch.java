package simulator;

public class Clutch extends Component {
    private boolean isPressed;

    public Clutch(String name, double weight, double price) {
        super(name, weight, price);
        this.isPressed = false;
    }

    public void press() {
        isPressed = true;
    }
    public void release() {
        isPressed = false;
    }



}
