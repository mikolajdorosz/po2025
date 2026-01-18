package org.example.thewitcher.model.map;

import org.example.thewitcher.model.entity.Player;
import org.example.thewitcher.model.map.data.MapObject;
import org.example.thewitcher.model.map.data.ObjectRegistry;
import org.example.thewitcher.model.map.point.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    protected Point[][] location;
    protected Player player;
    protected int[][] fileContents;
    protected int width;
    protected int height;

    public Location(Player player, InputStream textFile) {
        this.player = player;
        loadMap(textFile);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Point getPoint(int x, int y) { return location[y][x]; }

    private void loadMap(InputStream textFile) {
        List<String> lines = readFile(textFile);
        height = lines.size();
        width = lines.getFirst().split("\t").length;
        fileContents = setFileContents(lines);
        location = new Point[height][width];
        boolean[][] filled = new boolean[height][width];

        for (int fileY = 0; fileY < height; fileY++) {
            for (int fileX = 0; fileX < width; fileX++) {
                if (filled[fileY][fileX]) continue;
                int value = fileContents[fileY][fileX];
                MapObject object = ObjectRegistry.get(Math.abs(value));
                if (object.isMultiPoint()) placeMultiPointObject(object, fileX, fileY, fileContents, filled);
                else placeSinglePointObject(object, fileX, fileY, value, filled);
            }
        }
    }
    private List<String> readFile(InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
            return lines;
        } catch (IOException e) { throw new RuntimeException("Failed to read map file.", e); }
    }
    private int[][] setFileContents(List<String> lines) {
        int[][] contents = new int[height][width];
        for (int y = 0; y < height; y++) {
            String[] tokens = lines.get(y).split("\t");
            for (int x = 0; x < width; x++) contents[y][x] = Integer.parseInt(tokens[x]);
        }
        return contents;
    }
    private void placeSinglePointObject(MapObject object, int x, int y, int value, boolean[][] filled) {
        location[y][x] = new Point(object.getSinglePoint(), x, y, value > 0);
        filled[y][x] = true;
    }
    private void placeMultiPointObject(MapObject object, int fileX, int fileY, int[][] fileContents, boolean[][] filled) {
        String[] drawing = object.getMultiPoint();
        for (int drawingY = 0; drawingY < drawing.length; drawingY++) {
            String row = drawing[drawingY];
            for (int drawingX = 0; drawingX < row.length(); drawingX++) {
                int locationX = fileX + drawingX;
                int locationY = fileY + drawingY;
                if (!(locationX >= 0 && locationY >= 0 && locationX < width && locationY < height)) continue;
                location[locationY][locationX] = new Point(row.charAt(drawingX),
                        locationX, locationY, fileContents[locationY][locationX] > 0);
                filled[locationY][locationX] = true;
            }
        }
    }
}
