package simulator;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Position {
    private double x;
    private double y;

    public Position() {
        this.x = 0;
        this.y = 0;
    }
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition(double dX, double dY) {
        x += dX;
        y += dY;
    }
    public String getPosition() { return "X: " + x + ", Y: " + y; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void replace(Position destination, double v, double dT) {
        double step = v * dT;
        double dX = destination.getX() - this.getX();
        double dY = destination.getY() - this.getY();
        double s = sqrt(pow(dX, 2) + pow(dY, 2));

        if (step == 0 || s == 0) return;
        if (step > s) step = s;
        updatePosition(step * dX / s,  step * dY / s);
    }
}
