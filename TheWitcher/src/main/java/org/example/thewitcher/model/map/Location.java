package org.example.thewitcher.model.map;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Armorer;
import org.example.thewitcher.model.entity.character.Bandit;
import org.example.thewitcher.model.entity.character.Blacksmith;
import org.example.thewitcher.model.entity.character.Innkeeper;
import org.example.thewitcher.model.entity.character.Merchant;
import org.example.thewitcher.model.entity.character.Sorceress;
import org.example.thewitcher.model.battle.CharacterBattleUnit;
import org.example.thewitcher.model.entity.character.Ally;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.battle.MonsterBattleUnit;
import org.example.thewitcher.model.entity.character.Bandit;
import org.example.thewitcher.model.entity.monster.Ghul;
import org.example.thewitcher.model.entity.monster.Wolf;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.map.data.MapObject;
import org.example.thewitcher.model.map.data.ObjectDrawings;
import org.example.thewitcher.model.map.data.ObjectRegistry;
import org.example.thewitcher.model.map.point.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Location {
    protected Point[][] location;
    protected Player player;
    protected int[][] fileContents;
    protected int width;
    protected int height;
    protected List<Entity> entities;
    protected List<IBattleUnit> enemies;

    public Location(Player player, InputStream textFile) {
        this.player = player;
        this.enemies = new ArrayList<>();
        loadMap(textFile);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Point getPoint(int x, int y) { return location[y][x]; }
    public List<Entity> getEntities() { return entities; }
    public List<IBattleUnit> getEnemies() { return enemies; }

    private void loadMap(InputStream textFile) {
        List<String> lines = readFile(textFile);
        height = lines.size();
        width = lines.getFirst().split("\t").length;
        fileContents = setFileContents(lines);
        location = new Point[height][width];
        boolean[][] baseFilled = new boolean[height][width];

        placeMultis(baseFilled);
        placeSingles();
        spawnEntities();
        spawnAllies();
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
        for (int fileY = 0; fileY < height; fileY++) {
            String[] tokens = lines.get(fileY).split("\t");
            for (int fileX = 0; fileX < width; fileX++) contents[fileY][fileX] = Integer.parseInt(tokens[fileX]);
        }
        return contents;
    }
    private void placeMultis(boolean[][] baseFilled) {
        for (int fileY = 0; fileY < height; fileY++) {
            for (int fileX = 0; fileX < width; fileX++) {
                MapObject object = ObjectRegistry.get(Math.abs(fileContents[fileY][fileX]));
                if (object != null && object.isMultiPoint() && !baseFilled[fileY][fileX])
                    placeMultiPointObject(object, fileX, fileY, baseFilled);
            }
        }
    }
    private void placeSingles() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                MapObject object = ObjectRegistry.get(Math.abs(fileContents[y][x]));
                if (object != null && object.isSinglePoint())
                    placeSinglePointObject(object, x, y, fileContents[y][x]);
            }
        }
    }
    private void placeMultiPointObject(MapObject object, int fileX, int fileY, boolean[][] baseFilled) {
        String[] drawing = object.getMultiPoint();
        for (int drawingY = 0; drawingY < drawing.length; drawingY++) {
            for (int drawingX = 0; drawingX < drawing[drawingY].length(); drawingX++) {
                int locationX = fileX + drawingX;
                int locationY = fileY + drawingY;
                if (!(locationX >= 0 && locationY >= 0 && locationX < width && locationY < height)) continue;
                if (baseFilled[locationY][locationX]) continue;
                location[locationY][locationX] = new Point(drawing[drawingY].charAt(drawingX),
                        locationX, locationY, fileContents[locationY][locationX] > 0);
                baseFilled[locationY][locationX] = true;
            }
        }
    }
    private void placeSinglePointObject(MapObject object, int x, int y, int value) {
        if (location[y][x] == null) location[y][x] = new Point(object.getSinglePoint(), x, y, value > 0);
        else {
            location[y][x].setOverlay(object.getSinglePoint(), value > 0);
        }
        if (location[y][x].getMarker() == ObjectDrawings.bandit()) enemies.add(new CharacterBattleUnit(new Bandit(location[y][x].getX(), location[y][x].getY())));
        if (location[y][x].getMarker() == ObjectDrawings.ghul()) enemies.add(new MonsterBattleUnit(new Ghul(location[y][x].getX(), location[y][x].getY())));
        if (location[y][x].getMarker() == ObjectDrawings.wolf()) enemies.add(new MonsterBattleUnit(new Wolf(location[y][x].getX(), location[y][x].getY())));
    }

    private void spawnEntities() {
        entities = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (location[y][x] == null) continue;
                char marker = location[y][x].getMarker();
                Entity entity = switch (marker) {
                    case 'I' -> new Innkeeper(x, y);
                    case 'B' -> new Blacksmith(x, y);
                    case 'A' -> new Armorer(x, y);
                    case 'M' -> new Merchant(x, y);
                    case 'S' -> new Sorceress(x, y);
                    case 'b' -> new Bandit(x, y);
                    default -> null;
                };
                if (entity != null) entities.add(entity);
            }
        }
    }

    private void spawnAllies() {
        // Iterate enemies list. For each enemy, small chance to spawn an ally nearby.

        List<IBattleUnit> currentEnemies = new ArrayList<>(enemies);
        for (IBattleUnit unit : currentEnemies) {
            if (ThreadLocalRandom.current().nextInt(100) < 40) { // 40% chance per enemy
                Entity enemy = unit.getEntity();
                spawnAllyNear(enemy.getX(), enemy.getY());
            }
        }
    }

    private void spawnAllyNear(int x, int y) {
        // Try to find a valid spot around x,y
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                    if (location[ny][nx] != null && !location[ny][nx].isObstacle() && location[ny][nx].getMarker() == ObjectDrawings.grass()) {
                        // Check if occupied by another entity
                        boolean occupied = false;
                        for(Entity e : entities) if(e.getX() == nx && e.getY() == ny) occupied = true;
                        if(!occupied) {
                            addAlly(nx, ny);
                            return; // Spawn one and done for this trigger
                        }
                    }
                }
            }
        }
    }

    private void addAlly(int x, int y) {
        Ally ally = new Ally(x, y);
        entities.add(ally);
        // Updating point marker
        if (location[y][x] != null) {
            location[y][x].setOverlay(ObjectDrawings.ally(), false);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        if (entity.getX() >= 0 && entity.getX() < width && entity.getY() >= 0 && entity.getY() < height) {
            Point p = location[entity.getY()][entity.getX()];
            if (p != null && p.getMarker() == ObjectDrawings.ally()) {
                p.setOverlay(ObjectDrawings.grass(), false); // Restore to grass
            }
        }
    }
}
