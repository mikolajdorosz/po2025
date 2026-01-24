package org.example.thewitcher.view.interaction;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.thewitcher.model.entity.character.Merchant;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.view.util.ViewportCalculator;

public class MerchantView extends GenericInteractionView {

    public MerchantView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        super(gc, canvas, viewport);
    }

    @Override
    public void render(Game game) {
        gc.save();
        try {
            // Clear background
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            Merchant merchant = (Merchant) game.getCurrentInteractable();
            Player player = game.getPlayer();

            if (merchant == null) return;

            double startX = 50;
            double startY = 80;
            double lineHeight = 30;

            gc.setFont(new Font("System", 14));

            // Title
            gc.setFill(Color.GOLD);
            gc.fillText("Merchant Interaction (ESC to close)", startX, 40);
            gc.setFill(Color.WHITE);
            gc.fillText("Player Gold: " + player.getCoins(), startX, 60);

            // Message
            gc.setFill(Color.LIGHTBLUE);
            gc.fillText(game.getMessage(), startX + 300, 60);

            // --- Stock ---
            double col1X = startX;
            gc.setFill(Color.ORANGE);
            gc.fillText("Supply (Price includes " + merchant.getServiceFee() + " fee):", col1X, startY);

            char key = 'a';
            double currentY = startY + lineHeight;

            for (Item item : merchant.getCargo().getItems()) {
                gc.setFill(Color.WHITE);
                int totalCost = item.getPrice() + merchant.getServiceFee();
                String text = String.format("%s - Cost: %d", item.getName(), totalCost);
                gc.fillText(text + " [" + key + "]", col1X, currentY);
                key++;
                currentY += lineHeight;
            }

            // --- Player Food ---
            double col2X = startX;
            double currentY2 = currentY + lineHeight;
            gc.setFill(Color.ORANGE);
            gc.fillText("Your Food:", col2X, currentY2);
            currentY2 += lineHeight;

            for (Item item : player.getEquipment().getItems()) {
                if (item instanceof org.example.thewitcher.model.items.Food) {
                    gc.setFill(Color.WHITE);
                    String text = String.format("%s - Sell: %d", item.getName(), item.getPrice());
                    gc.fillText(text + " [" + key + "]", col2X, currentY2);
                    key++;
                    currentY2 += lineHeight;
                }
            }

        } finally {
            gc.restore();
        }
    }
}