package org.example.thewitcher.model.map.data;

public class MapObject {
    private final String[] multiPoint;
    private final Character singlePoint;

    private MapObject(String[] multiPoint, Character singlePoint) {
        this.multiPoint = multiPoint;
        this.singlePoint = singlePoint;
    }

    public String[] getMultiPoint() { return multiPoint; }
    public char getSinglePoint() { return singlePoint; }

    public static MapObject ofMultiPoint(String[] multiPoint) { return new MapObject(multiPoint, null); }
    public static MapObject ofSinglePoint(char singlePoint) { return new MapObject(null, singlePoint); }
    public boolean isMultiPoint() { return multiPoint != null; }
    public boolean isSinglePoint() { return singlePoint != null; }
}
