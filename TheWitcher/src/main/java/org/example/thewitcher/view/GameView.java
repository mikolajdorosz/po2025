package org.example.thewitcher.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import org.example.thewitcher.model.entity.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.map.Location;

public class GameView {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int pointWidth;
    private final int pointHeight;
    private final int viewWidth;
    private final int viewHeight;

    public GameView(int windowWidth, int windowHeight) {
        this.canvas = new Canvas(windowWidth, windowHeight);
        this.gc = canvas.getGraphicsContext2D();
        this.pointWidth = 12;
        this.pointHeight = 16;
        this.viewWidth = windowWidth / this.pointWidth;
        this.viewHeight = windowHeight / this.pointHeight;
    }

    public Canvas getCanvas() { return canvas;}
    public GraphicsContext getGraphicsContext() { return gc;}

    public void render(Game game) {
        Player player = game.getPlayer();
        Location location = game.getLocation();
        int viewX = calculateViewX(player, location);
        int viewY = calculateViewY(player, location);
        int offsetX = calculateOffsetX(location);
        int offsetY = calculateOffsetY(location);

        clearCanvas();
        renderMap(location, player, viewX, viewY, offsetX, offsetY);
    }

    private int calculateViewX(Player player, Location location) {
        if (location.getWidth() <= viewWidth) return 0;
        int x = player.getX() - viewWidth / 2;
        return clamp(x, 0, location.getWidth() - viewWidth);
    }
    private int calculateViewY(Player player, Location location) {
        if (location.getHeight() <= viewHeight) return 0;
        int y = player.getY() - viewHeight / 2;
        return clamp(y, 0, location.getHeight() - viewHeight);
    }
    private int calculateOffsetX(Location location) {
        if (location.getWidth() <= viewWidth)
            return (viewWidth - location.getWidth()) / 2;
        return 0;
    }
    private int calculateOffsetY(Location location) {
        if (location.getHeight() <= viewHeight)
            return (viewHeight - location.getHeight()) / 2;
        return 0;
    }
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFont(Font.font("Consolas", 14));
    }
    private void renderMap(Location location, Player player, int viewX, int viewY, int offsetX, int offsetY) {
        for (int y = 0; y < viewHeight; y++) {
            for (int x = 0; x < viewWidth; x++) {
                int mapX = viewX + x - offsetX;
                int mapY = viewY + y - offsetY;
                if (!isInBounds(location, mapX, mapY)) continue;
                char marker = location.getPoint(mapX, mapY).getMarker();
                if (mapX == player.getX() && mapY == player.getY()) marker = 'G';
                gc.fillText(String.valueOf(marker), x * pointWidth, (y + 1) * pointHeight);
            }
        }
    }
    private boolean isInBounds(Location location, int x, int y) {
        return x >= 0 && y >= 0 && x < location.getWidth() && y < location.getHeight();
    }
}
