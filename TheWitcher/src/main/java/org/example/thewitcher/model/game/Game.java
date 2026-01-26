package org.example.thewitcher.model.game;

import org.example.thewitcher.model.battle.Battle;
import org.example.thewitcher.model.battle.BattleDetector;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.items.AppliedEquipment;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.map.Location;
import org.example.thewitcher.model.map.Velen;
import org.example.thewitcher.model.map.TestMap;

import org.example.thewitcher.model.entity.Interactable;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int BATTLE_TRIGGER_RADIUS = 2;
    private static final int BATTLE_ENEMIES_RADIUS = 5;

    private Player player;
    private Location location;
    private GameState state;
    private Interactable currentInteractable;
    private Item selectedItem;
    private String message = "";
    private Battle battle;

    public Game() {
        player = new Player();
        location = new Velen(player);
        location = new TestMap(player);
        state = GameState.MAP;
    }

    public GameState getState() { return state; }
    public void setState(GameState state) {
        //Auto-clear message
        if (this.state == GameState.MAP) {
            if (state == GameState.INVENTORY || state.toString().startsWith("INTERACTION_")) {
                this.message = "";
            }
        }

        this.state = state;
    }

    public Interactable getCurrentInteractable() { return currentInteractable; }
    public void setCurrentInteractable(Interactable interactable) { this.currentInteractable = interactable; }

    public void setPlayer(Player player) { this.player = player; }

    public Item getSelectedItem() { return selectedItem; }
    public void setSelectedItem(Item item) { this.selectedItem = item; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Player getPlayer() { return player; }
    public Location getLocation() { return location; }
    public Battle getBattle() { return battle; }
    public void setBattle(Battle battle) { this.battle = battle; }

    public void movePlayer(int deltaX, int deltaY) {
        int newX = player.getX() + deltaX;
        int newY = player.getY() + deltaY;

        if (newX < 0 || newY < 0 || newX >= location.getWidth() || newY >= location.getHeight()) return;
        if (!location.getPoint(newX, newY).isObstacle()) {
            player.setX(newX);
            player.setY(newY);
        }
        checkBattle();
    }

    private void checkBattle() {
        for (IBattleUnit enemy : location.getEnemies()) {
            if (BattleDetector.searchForEnemy(player, enemy, BATTLE_TRIGGER_RADIUS)) {
                state = GameState.BATTLE;
                battle = new Battle(player.getAllies(), BattleDetector.gatherEnemies(player, location.getEnemies(), BATTLE_ENEMIES_RADIUS));
                if (!battle.isPlayerAlive()) { state = GameState.GAME_OVER; }
                return;
            }
        }
    }

    public List<InventoryEntry> getInventoryEntries() {
        List<InventoryEntry> entries = new ArrayList<>();
        char key = 'a';

        // Applied Equipment
        AppliedEquipment applied = player.getAppliedEquipment();
        if (applied.getSilver() != null) {
            entries.add(new InventoryEntry(key++, "Silver: " + applied.getSilver().getName(), applied.getSilver(), true));
        }
        if (applied.getSteel() != null) {
            entries.add(new InventoryEntry(key++, "Steel: " + applied.getSteel().getName(), applied.getSteel(), true));
        }
        if (applied.getDistance() != null) {
            entries.add(new InventoryEntry(key++, "Distance: " + applied.getDistance().getName(), applied.getDistance(), true));
        }
        if (applied.getArmor() != null) {
            entries.add(new InventoryEntry(key++, "Armor: " + applied.getArmor().getName(), applied.getArmor(), true));
        }

        // Backpack
        for (Item item : player.getEquipment().getItems()) {
            entries.add(new InventoryEntry(key++, item.getName(), item, false));
        }

        return entries;
    }

    public static class InventoryEntry {
        public final char key;
        public final String label;
        public final Item item;
        public final boolean isApplied;

        public InventoryEntry(char key, String label, Item item, boolean isApplied) {
            this.key = key;
            this.label = label;
            this.item = item;
            this.isApplied = isApplied;
        }
    }
}
