package org.example.thewitcher.view.inventory;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.view.inventory.overlay.ItemActionOverlay;
import org.example.thewitcher.view.inventory.overlay.ItemInspectionOverlay;
import org.example.thewitcher.view.util.ViewportCalculator;

public class InventoryView {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final ViewportCalculator viewport;
    private final ItemActionOverlay itemActionOverlay;
    private final ItemInspectionOverlay itemInspectionOverlay;

    public InventoryView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        this.gc = gc;
        this.canvas = canvas;
        this.viewport = viewport;
        this.itemActionOverlay = new ItemActionOverlay(gc, canvas, viewport);
        this.itemInspectionOverlay = new ItemInspectionOverlay(gc, canvas, viewport);
    }

    public void render(Game game) {
        renderView(game);
        if (game.getState() == GameState.INVENTORY_ITEM_ACTION_MENU) itemActionOverlay.render(game);
        if (game.getState() == GameState.INVENTORY_INSPECT_ITEM) itemInspectionOverlay.render(game);
    }

    private void renderView(Game game) {
        gc.setFill(Color.WHITE);
        gc.fillText("INVENTORY", 20, 30);

        int startY = 60;
        int leftColX = 20;
        int rightColX = 400;

        gc.fillText("Applied Equipment", leftColX, startY);
        gc.fillText("Backpack", rightColX, startY);

        startY += 30;
        int leftY = startY;
        int rightY = startY;

        for (Game.InventoryEntry entry : game.getInventoryEntries()) {
            String text = "[" + entry.key + "] " + entry.label;
            if (entry.isApplied) {
                gc.fillText(text, leftColX, leftY);
                leftY += 20;
            } else {
                gc.fillText(text, rightColX, rightY);
                rightY += 20;
            }
        }

        // Draw Message
        if (!game.getMessage().isEmpty() && game.getState() != GameState.INVENTORY_INSPECT_ITEM) {
            gc.setFill(Color.YELLOW);
            gc.fillText(game.getMessage(), 20, canvas.getHeight() - 50);
        }
    }
}
