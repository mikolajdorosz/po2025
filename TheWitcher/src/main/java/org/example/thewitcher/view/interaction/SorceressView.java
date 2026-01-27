package org.example.thewitcher.view.interaction;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.thewitcher.model.entity.character.Sorceress;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.view.util.ViewportCalculator;

public class SorceressView extends GenericInteractionView {
    public SorceressView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        super(gc, canvas, viewport);
    }

    @Override
    public void render(Game game) {
        gc.save();
        try {
            // Clear background
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            Sorceress sorceress = (Sorceress) game.getCurrentInteractable();
            Player player = game.getPlayer();

            if (sorceress == null) return;

            double startX = 50;
            double startY = 80;
            double lineHeight = 30;

            gc.setFont(new Font("System", 14));

            // Title
            gc.setFill(Color.YELLOW);
            gc.fillText("Sorceress Interaction (ESC to close)", startX, 40);
            gc.setFill(Color.WHITE);
            gc.fillText("Player Gold: " + player.getCoins(), startX, 60);

            // Message
            gc.setFill(Color.LIGHTBLUE);
            gc.fillText(game.getMessage(), startX + 300, 60);

            // --- Elixirs ---
            double col1X = startX;
            gc.setFill(Color.ORANGE);
            gc.fillText("Elixirs (Service Fee: " + sorceress.getServiceFee() + "):", col1X, startY);

            char key = 'a';
            double currentY = startY + lineHeight;

            for (java.util.Map.Entry<String, String> entry : sorceress.getRecipes().entrySet()) {
                String elixirName = entry.getKey();
                String herbName = entry.getValue();

                gc.setFill(Color.WHITE);
                String text = String.format("%s - Cost: %d + %s", elixirName, sorceress.getServiceFee(), herbName);
                gc.fillText(text + " [" + key + "]", col1X, currentY);
                key++;
                currentY += lineHeight;
            }

            // --- Player Herbs ---
            double col2X = startX;
            double currentY2 = currentY + lineHeight;
            gc.setFill(Color.ORANGE);
            gc.fillText("Your Herbs:", col2X, currentY2);
            currentY2 += lineHeight;

            for (Item item : player.getEquipment().getItems()) {
                if (item instanceof org.example.thewitcher.model.items.Herb) {
                    gc.setFill(Color.WHITE);
                    // Herbs might not have a fixed sell price evident here, just name matters for crafting
                    String text = String.format("%s", item.getName());
                    gc.fillText(text, col2X, currentY2);
                    currentY2 += lineHeight;
                }
            }

        } finally {
            gc.restore();
        }
    }
}
