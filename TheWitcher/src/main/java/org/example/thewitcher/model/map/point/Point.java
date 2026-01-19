package org.example.thewitcher.model.map.point;

public class Point {
    private final char baseMarker;
    private Character overlayMarker;
    private int x;
    private int y;
    private boolean baseIsObstacle;
    private boolean overlayIsObstacle;

    public Point(char baseMarker, int x, int y, boolean baseIsObstacle) {
        this.baseMarker = baseMarker;
        this.x = x;
        this.y = y;
        this.baseIsObstacle = baseIsObstacle;
    }

    public char getMarker() { return overlayMarker != null ? overlayMarker : baseMarker; }
    public char getBaseMarker() { return baseMarker; }
    public Character getOverlayMarker() { return overlayMarker; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isObstacle() { return baseIsObstacle || overlayIsObstacle; }

    public void setOverlay(char overlayMarker, boolean overlayIsObstacle) {
        this.overlayMarker = overlayMarker;
        this.overlayIsObstacle = overlayIsObstacle;
    }
}
