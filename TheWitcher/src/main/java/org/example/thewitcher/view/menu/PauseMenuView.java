package org.example.thewitcher.view.menu;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PauseMenuView {
    private final GraphicsContext gc;
    private final Canvas canvas;

    public PauseMenuView(GraphicsContext gc, Canvas canvas) {
        this.gc = gc;
        this.canvas = canvas;
    }

    public void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);

        // Title
        gc.setFont(Font.font("Verdana", 40));
        gc.fillText("PAUSE MENU", canvas.getWidth() / 2, 100);

        // Options
        gc.setFont(Font.font("Verdana", 20));
        double centerY = canvas.getHeight() / 2;

        gc.fillText("[ESC] Resume", canvas.getWidth() / 2, centerY - 30);
        gc.fillText("[1] Restart", canvas.getWidth() / 2, centerY);
        gc.fillText("[2] Save & Quit", canvas.getWidth() / 2, centerY + 30);

        gc.setTextAlign(TextAlignment.LEFT);
    }
}
