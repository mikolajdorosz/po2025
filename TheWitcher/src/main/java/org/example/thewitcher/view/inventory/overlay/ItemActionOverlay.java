package org.example.thewitcher.view.inventory.overlay;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.util.ViewportCalculator;

public class ItemActionOverlay {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final ViewportCalculator viewport;

    public ItemActionOverlay(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        this.gc = gc;
        this.canvas = canvas;
        this.viewport = viewport;
    }

    public void render(Game game) {
        if (game.getSelectedItem() == null) return;
        viewport.clear(gc, canvas);

        double w = 400;
        double h = 200;
        double x = (canvas.getWidth() - w) / 2;
        double y = (canvas.getHeight() - h) / 2;

        gc.setFill(Color.rgb(0, 0, 0, 0.9));
        gc.fillRect(x, y, w, h);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(x, y, w, h);

        gc.setFill(Color.WHITE);
        int textX = (int) x + 20;
        int textY = (int) y + 30;

        gc.fillText("Selected: " + game.getSelectedItem().getName(), textX, textY); textY += 30;

        String type = game.getSelectedItem().getType();
        gc.fillText("1 - Inspect", textX, textY);  textY += 20;

        if (type.equals("food")) {
            gc.fillText("2 - Use", textX, textY); textY += 20;
        } else if (type.equals("silver") || type.equals("steel") || type.equals("distance") || type.equals("armor")) {
            gc.fillText("2 - Equip/Unequip", textX, textY); textY += 20;
        }

        gc.fillText("3 - Drop", textX, textY); textY += 20;
        gc.fillText("0 - Cancel", textX, textY);
    }
}
