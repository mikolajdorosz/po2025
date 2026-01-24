package org.example.thewitcher.view.interaction;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.entity.character.Armorer;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.items.Armor;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.view.util.ViewportCalculator;
import javafx.scene.text.Font;

import java.util.List;

public class ArmorerView extends GenericInteractionView {

    public ArmorerView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        super(gc, canvas, viewport);
    }

    @Override
    public void render(Game game) {
        // Save state to avoid side effects (like font size changes) leaking to other views
        gc.save();

        try {
            // Clear background
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            Armorer armorer = (Armorer) game.getCurrentInteractable();
            Player player = game.getPlayer();

            if (armorer == null) return;

            double startX = 50;
            double startY = 80;
            double lineHeight = 30;

            // Set smaller font to fit text
            gc.setFont(new Font("System", 14));

            // Title
            gc.setFill(Color.GOLD);
            gc.fillText("Armorer Interaction (ESC to close)", startX, 40);
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

            for (Item item : armorer.getStock().getItems()) {
                if (item instanceof Armor armor) {
                    gc.setFill(Color.WHITE);
                    String text = String.format("%s - Armor: %d, Buy: %d",
                            armor.getName(), armor.getBonus(), armor.getPrice());
                    gc.fillText(text + " [" + key + "]", col1X, currentY);
                    key++;
                    currentY += lineHeight;
                }
            }

            // --- Player Items ---
            double col2X = startX;
            double currentY2 = currentY + lineHeight;

            gc.setFill(Color.ORANGE);
            gc.fillText("Your Items:", col2X, currentY2);
            currentY2 += lineHeight;

            // Combined iterator logic must match Key logic in Controller
            // 1. Applied Items
            // 2. Backpack Items

            List<Item> appliedItems = java.util.Arrays.asList(
                    player.getAppliedEquipment().getSilver(),
                    player.getAppliedEquipment().getSteel(),
                    player.getAppliedEquipment().getDistance(),
                    player.getAppliedEquipment().getArmor()
            );

            for (Item item : appliedItems) {
                if (item instanceof Armor armor) {
                    renderPlayerArmor(armor, col2X, currentY2, key);
                    key += 2; // Sell + Repair = 2 keys
                    currentY2 += lineHeight;
                }
            }

            for (Item item : player.getEquipment().getItems()) {
                if (item instanceof Armor armor) {
                    renderPlayerArmor(armor, col2X, currentY2, key);
                    key += 2; // Sell + Repair = 2 keys
                    currentY2 += lineHeight;
                }
            }
        } finally {
            // Restore state (font, colors, etc) to what it was before this render call
            gc.restore();
        }
    }

    private void renderPlayerArmor(Armor armor, double x, double y, char currentKey) {
        int sellPrice = (int)(armor.getPrice() * (armor.getCondition() / 100.0) * 0.5);
        int repairPrice = (100 - armor.getCondition()) * 5;
        char repairKey = (char)(currentKey + 1);

        String text = String.format("%s - Cond: %d, Sell: %d [%c]  Repair: %d [%c]",
                armor.getName(), armor.getCondition(), sellPrice, currentKey, repairPrice, repairKey);

        gc.setFill(Color.WHITE);
        gc.fillText(text, x, y);
    }
}
