package org.example.thewitcher.view.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.entity.Player;
import org.example.thewitcher.model.map.Location;

public class ViewportCalculator {
    private final int pointWidth, pointHeight;
    private final int viewWidth, viewHeight;
    private int viewX, viewY, offsetX, offsetY;

    public ViewportCalculator(int windowW, int windowH, int pw, int ph) {
        this.pointWidth = pw;
        this.pointHeight = ph;
        this.viewWidth = windowW / pw;
        this.viewHeight = windowH / ph;
    }

    public int getViewWidth() { return viewWidth; }
    public int getViewHeight() { return viewHeight; }
    public int getPointWidth() { return pointWidth; }
    public int getPointHeight() { return pointHeight; }
    public int getLocationX(int screenX) { return viewX + screenX - offsetX; }
    public int getLocationY(int screenY) { return viewY + screenY - offsetY; }

    public void calculate(Player player, Location location) {
        viewX = clamp(player.getX() - viewWidth / 2, 0, Math.max(0, location.getWidth() - viewWidth));
        viewY = clamp(player.getY() - viewHeight / 2, 0, Math.max(0, location.getHeight() - viewHeight));
        offsetX = location.getWidth() < viewWidth ? (viewWidth - location.getWidth()) / 2 : 0;
        offsetY = location.getHeight() < viewHeight ? (viewHeight - location.getHeight()) / 2 : 0;
    }
    public void clear(GraphicsContext gc, Canvas canvas) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    private int clamp(int value, int min, int max) { return Math.max(min, Math.min(value, max)); }
}
