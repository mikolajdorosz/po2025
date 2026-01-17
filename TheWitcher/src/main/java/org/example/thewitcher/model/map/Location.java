package org.example.thewitcher.model.map;

import org.example.thewitcher.model.entity.Player;
import org.example.thewitcher.model.utils.Point;

public abstract class Location {
    protected int width;
    protected int height;
    protected Point[][] map;
    protected Player player;

    public Location(Player player, int width, int height) {
        this.player = player;
        this.width = width;
        this.height = height;
        this.map = new Point[height][width];

        create();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Point getPoint(int x, int y) { return map[y][x]; }

    protected void create() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) map[y][x] = new Point(' ', x, y, false);
        }
    }
}
