package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.entity.character.Blacksmith;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.*;

import java.util.List;

public class BlacksmithController {
    private final Game game;

    public BlacksmithController(Game game) {
        this.game = game;
    }

    public void handleInput(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(GameState.MAP);
            game.setCurrentInteractable(null);
            game.setMessage("Closed trade.");
            return;
        }

        Blacksmith blacksmith = (Blacksmith) game.getCurrentInteractable();
        Player player = game.getPlayer();

        if (code.isLetterKey()) {
            char key = code.toString().toLowerCase().charAt(0);
            processAction(key, blacksmith, player);
        }
    }

    private void processAction(char key, Blacksmith blacksmith, Player player) {
        char currentKey = 'a';

        // Buy items
        for (Item item : blacksmith.getStock().getItems()) {
            if (currentKey == key) {
                buyItem(player, item);
                return;
            }
            currentKey++;
        }

        // Applied Equipment (Weapons)
        AppliedEquipment applied = player.getAppliedEquipment();
        List<Item> appliedItems = java.util.Arrays.asList(
                applied.getSilver(), applied.getSteel(), applied.getDistance(), applied.getArmor()
        );

        for (Item item : appliedItems) {
            if (item instanceof Weapon weapon) {
                if (currentKey == key) { // Sell
                    sellItem(player, weapon);
                    return;
                }
                currentKey++;
                if (currentKey == key) { // Repair
                    repairItem(player, weapon);
                    return;
                }
                currentKey++;
            }
        }

        // Backpack Items (Weapons)
        for (Item item : player.getEquipment().getItems()) {
            if (item instanceof Weapon weapon) {
                if (currentKey == key) { // Sell
                    sellItem(player, weapon);
                    return;
                }
                currentKey++;
                if (currentKey == key) { // Repair
                    repairItem(player, weapon);
                    return;
                }
                currentKey++;
            }
        }
    }

    private void buyItem(Player player, Item item) {
        if (player.getCoins() >= item.getPrice()) {
            player.setCoins(player.getCoins() - item.getPrice());
            player.getEquipment().addItem(item);
            game.setMessage("Bought " + item.getName());
        } else {
            game.setMessage("Not enough coins!");
        }
    }

    private void sellItem(Player player, Weapon weapon) {
        int price = calculateSellPrice(weapon);
        player.setCoins(player.getCoins() + price);

        if (player.getEquipment().getItems().contains(weapon)) {
            player.getEquipment().removeItem(weapon);
        } else {
            player.getAppliedEquipment().removeFromList(weapon.getName().toLowerCase());
            // Check specific slots to nullify
            if (player.getAppliedEquipment().getSilver() == weapon) player.getAppliedEquipment().setSilver(null);
            else if (player.getAppliedEquipment().getSteel() == weapon) player.getAppliedEquipment().setSteel(null);
            else if (player.getAppliedEquipment().getDistance() == weapon) player.getAppliedEquipment().setDistance(null);
        }
        game.setMessage("Sold " + weapon.getName() + " for " + price);
    }

    private void repairItem(Player player, Weapon weapon) {
        int cost = calculateRepairCost(weapon);
        if (cost == 0) {
            game.setMessage("Already perfect condition.");
            return;
        }
        if (player.getCoins() >= cost) {
            player.setCoins(player.getCoins() - cost);
            weapon.setCondition(100);
            game.setMessage("Repaired " + weapon.getName());
        } else {
            game.setMessage("Not enough coins to repair!");
        }
    }

    private int calculateSellPrice(Weapon weapon) {
        return (int) (weapon.getPrice() * (weapon.getCondition() / 100.0) * 0.5);
    }

    private int calculateRepairCost(Weapon weapon) {
        return (100 - weapon.getCondition()) * 5;
    }
}
