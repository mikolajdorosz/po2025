package org.example.thewitcher.view.interaction;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.thewitcher.model.entity.character.Blacksmith;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.items.Weapon;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.view.util.ViewportCalculator;

import java.util.List;

public class BlacksmithView extends GenericInteractionView {

    public BlacksmithView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        super(gc, canvas, viewport);
    }

        @Override
        public void render (Game game){
            // Save state
            gc.save();

            try {
                // Clear background
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                Blacksmith blacksmith = (Blacksmith) game.getCurrentInteractable();
                Player player = game.getPlayer();

                if (blacksmith == null) return;

                double startX = 50;
                double startY = 80;
                double lineHeight = 30;

                // Set smaller font
                gc.setFont(new Font("System", 14));

                // Title
                gc.setFill(Color.GOLD);
                gc.fillText("Blacksmith Interaction (ESC to close)", startX, 40);
                gc.setFill(Color.WHITE);
                gc.fillText("Player Gold: " + player.getCoins(), startX, 60);

                // Message
                gc.setFill(Color.LIGHTBLUE);
                gc.fillText(game.getMessage(), startX + 300, 60);

                // --- Stock ---
                double col1X = startX;
                gc.setFill(Color.ORANGE);
                gc.fillText("Stock:", col1X, startY);

                char key = 'a';
                double currentY = startY + lineHeight;

                for (Item item : blacksmith.getStock().getItems()) {
                    if (item instanceof Weapon weapon) {
                        gc.setFill(Color.WHITE);
                        String text = String.format("%s - Dmg: %d, Buy: %d",
                                weapon.getName(), weapon.getBonus(), weapon.getPrice());
                        gc.fillText(text + " [" + key + "]", col1X, currentY);
                        key++;
                        currentY += lineHeight;
                    }
                }

                // --- Player Items ---
                double col2X = startX; // Can move to right column if desired, keeping same col for now as per Armorer
                double currentY2 = currentY + lineHeight;

                gc.setFill(Color.ORANGE);
                gc.fillText("Your Weapons:", col2X, currentY2);
                currentY2 += lineHeight;

                // Applied Items
                List<Item> appliedItems = java.util.Arrays.asList(
                        player.getAppliedEquipment().getSilver(),
                        player.getAppliedEquipment().getSteel(),
                        player.getAppliedEquipment().getDistance(),
                        player.getAppliedEquipment().getArmor()
                );

                for (Item item : appliedItems) {
                    if (item instanceof Weapon weapon) {
                        renderPlayerWeapon(weapon, col2X, currentY2, key);
                        key += 2; // Sell + Repair
                        currentY2 += lineHeight;
                    }
                }

                // Backpack Items
                for (Item item : player.getEquipment().getItems()) {
                    if (item instanceof Weapon weapon) {
                        renderPlayerWeapon(weapon, col2X, currentY2, key);
                        key += 2;
                        currentY2 += lineHeight;
                    }
                }

            } finally {
                gc.restore();
            }
        }

        private void renderPlayerWeapon (Weapon weapon,double x, double y, char currentKey){
            int sellPrice = (int) (weapon.getPrice() * (weapon.getCondition() / 100.0) * 0.5);
            int repairPrice = (100 - weapon.getCondition()) * 5;
            char repairKey = (char) (currentKey + 1);

            String text = String.format("%s - Cond: %d, Sell: %d [%c]  Repair: %d [%c]",
                    weapon.getName(), weapon.getCondition(), sellPrice, currentKey, repairPrice, repairKey);

            gc.setFill(Color.WHITE);
            gc.fillText(text, x, y);
        }
    }