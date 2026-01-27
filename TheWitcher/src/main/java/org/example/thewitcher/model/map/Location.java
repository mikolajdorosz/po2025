package org.example.thewitcher.model.map;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Bandit;
import org.example.thewitcher.model.battle.CharacterBattleUnit;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.battle.MonsterBattleUnit;
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

public abstract class Location {
    protected Point[][] location;
    protected Player player;
    protected int[][] fileContents;
    protected int width;
    protected int height;
    protected EntityManager entityManager;

    public Location(Player player, InputStream textFile) {
        this.player = player;
        this.entityManager = new EntityManager();
        loadMap(textFile);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Point getPoint(int x, int y) { return location[y][x]; }

    // Delegated methods
    public List<Entity> getEntities() { return entityManager.getEntities(); }
    public List<IBattleUnit> getEnemies() { return entityManager.getEnemies(); }
    public void removeEntity(Entity entity) { entityManager.removeEntity(entity, location, width, height); }
    public void removeEnemy(IBattleUnit enemy) { entityManager.removeEnemy(enemy, location, width, height); }

    private void loadMap(InputStream textFile) {
        List<String> lines = readFile(textFile);
        height = lines.size();
        width = lines.getFirst().split("\t").length;
        fileContents = setFileContents(lines);
        location = new Point[height][width];
        boolean[][] baseFilled = new boolean[height][width];

        placeMultis(baseFilled);
        placeSingles(); // Populates enemies via addEnemy
        entityManager.spawnEntities(location, width, height);
        entityManager.spawnAllies(location, width, height);
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
        if (location[y][x].getMarker() == ObjectDrawings.bandit()) entityManager.addEnemy(new CharacterBattleUnit(new Bandit(location[y][x].getX(), location[y][x].getY())));
        if (location[y][x].getMarker() == ObjectDrawings.ghul()) entityManager.addEnemy(new MonsterBattleUnit(new Ghul(location[y][x].getX(), location[y][x].getY())));
        if (location[y][x].getMarker() == ObjectDrawings.wolf()) entityManager.addEnemy(new MonsterBattleUnit(new Wolf(location[y][x].getX(), location[y][x].getY())));
    }
}
