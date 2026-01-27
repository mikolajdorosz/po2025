package org.example.thewitcher.model.map;

import org.example.thewitcher.model.battle.CharacterBattleUnit;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.battle.MonsterBattleUnit;
import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Ally;
import org.example.thewitcher.model.entity.character.Armorer;
import org.example.thewitcher.model.entity.character.Bandit;
import org.example.thewitcher.model.entity.character.Blacksmith;
import org.example.thewitcher.model.entity.character.Innkeeper;
import org.example.thewitcher.model.entity.character.Merchant;
import org.example.thewitcher.model.entity.character.Sorceress;
import org.example.thewitcher.model.entity.monster.Ghul;
import org.example.thewitcher.model.entity.monster.Wolf;
import org.example.thewitcher.model.map.data.ObjectDrawings;
import org.example.thewitcher.model.map.point.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EntityManager {
    private List<Entity> entities;
    private List<IBattleUnit> enemies;

    public EntityManager() {
        this.entities = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    public List<Entity> getEntities() { return entities; }
    public List<IBattleUnit> getEnemies() { return enemies; }

    public void spawnEntities(Point[][] location, int width, int height) {
        entities.clear();
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

    public void addEnemy(IBattleUnit enemy) {
        enemies.add(enemy);
    }

    public void spawnAllies(Point[][] location, int width, int height) {
        List<IBattleUnit> currentEnemies = new ArrayList<>(enemies);
        for (IBattleUnit unit : currentEnemies) {
            if (ThreadLocalRandom.current().nextInt(100) < 40) { // 40% chance per enemy
                Entity enemy = unit.getEntity();
                spawnAllyNear(enemy.getX(), enemy.getY(), location, width, height);
            }
        }
    }

    private void spawnAllyNear(int x, int y, Point[][] location, int width, int height) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                    if (location[ny][nx] != null && !location[ny][nx].isObstacle() && location[ny][nx].getMarker() == ObjectDrawings.grass()) {
                        boolean occupied = false;
                        for(Entity e : entities) if(e.getX() == nx && e.getY() == ny) occupied = true;
                        if(!occupied) {
                            addAlly(nx, ny, location);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void addAlly(int x, int y, Point[][] location) {
        Ally ally = new Ally(x, y);
        entities.add(ally);
        if (location[y][x] != null) {
            location[y][x].setOverlay(ObjectDrawings.ally(), false);
        }
    }

    public void removeEntity(Entity entity, Point[][] location, int width, int height) {
        entities.remove(entity);
        if (entity.getX() >= 0 && entity.getX() < width && entity.getY() >= 0 && entity.getY() < height) {
            Point p = location[entity.getY()][entity.getX()];
            if (p != null && p.getMarker() == ObjectDrawings.ally()) {
                p.setOverlay(ObjectDrawings.grass(), false); // Restore to grass
            }
        }
    }

    public void removeEnemy(IBattleUnit enemy, Point[][] location, int width, int height) {
        enemies.remove(enemy);
        Entity entity = enemy.getEntity();
        if (entity.getX() >= 0 && entity.getX() < width && entity.getY() >= 0 && entity.getY() < height) {
            Point p = location[entity.getY()][entity.getX()];
            if (p != null) {
                // Check if the current marker matches the enemy type before replacing
                char marker = p.getMarker();
                if (marker == ObjectDrawings.bandit() || marker == ObjectDrawings.ghul() || marker == ObjectDrawings.wolf()) {
                    p.setOverlay(ObjectDrawings.loot(), false);
                }
            }
        }
    }
}
