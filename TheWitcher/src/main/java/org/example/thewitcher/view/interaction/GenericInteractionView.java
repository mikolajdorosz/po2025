package org.example.thewitcher.view.interaction;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.util.ViewportCalculator;

public class GenericInteractionView implements InteractionView {
    protected final GraphicsContext gc;
    protected final Canvas canvas;
    protected final ViewportCalculator viewport;

    public GenericInteractionView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        this.gc = gc;
        this.canvas = canvas;
        this.viewport = viewport;
    }

    @Override
    public void render(Game game) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.fillText("Interaction (Press ESC to close)", 50, 50);
    }
}
