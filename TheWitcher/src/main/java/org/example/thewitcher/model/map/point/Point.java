package org.example.thewitcher.model.map.point;

public class Point {
    protected char marker;
    protected int x;
    protected int y;
    protected boolean isObstacle;

    public Point(char marker, int x, int y, boolean isObstacle) {
        this.marker = marker;
        this.x = x;
        this.y = y;
        this.isObstacle = isObstacle;
    }

    public char getMarker() { return marker; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isObstacle() { return isObstacle; }
}
