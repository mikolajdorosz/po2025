package org.example.thewitcher.model.entity;

public class Player {
    private int x;
    private int y;

    public Player() {
        this.x = 5;
        this.y = 5;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
