package org.example.simulatorgui.model.util;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position getPosition() { return this; }
    public double getX() { return x; }
    public double getY() { return y; }

    public void setPosition(Position position) { this.x = position.getX(); this.y = position.getY(); }

    public void updatePosition(double deltaX, double deltaY) {
        x += deltaX;
        y += deltaY;
    }
    public void moveTowards(Position destination, double speed, double deltaTime) {
        double step = speed * deltaTime;
        double deltaX = destination.getX() - this.getX();
        double deltaY = destination.getY() - this.getY();
        double s = sqrt(pow(deltaX, 2) + pow(deltaY, 2));

        if (step == 0 || s == 0) return;
        if (step > s) step = s;
        updatePosition(step * deltaX / s,  step * deltaY / s);
    }
}
