package org.example.thewitcher.view.inventory.overlay;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.util.ViewportCalculator;

public class ItemInspectionOverlay {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final ViewportCalculator viewport;

    public ItemInspectionOverlay(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        this.gc = gc;
        this.canvas = canvas;
        this.viewport = viewport;
    }

    public void render(Game game) {
        viewport.clear(gc, canvas);

        double w = 500;
        double h = 300;
        double x = (canvas.getWidth() - w) / 2;
        double y = (canvas.getHeight() - h) / 2;

        gc.setFill(Color.rgb(0, 0, 0, 0.95));
        gc.fillRect(x, y, w, h);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(x, y, w, h);

        gc.setFill(Color.WHITE);
        int textX = (int) x + 20;
        int textY = (int) y + 30;

        gc.fillText("INSPECTION", textX, textY); textY += 30;

        String[] lines = game.getMessage().split("\n");
        for (String line : lines) {
            gc.fillText(line, textX, textY); textY += 20;
        }

        textY += 20;
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Press any key to close", textX, textY);
    }
}
