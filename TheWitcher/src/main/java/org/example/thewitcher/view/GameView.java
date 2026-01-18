package org.example.thewitcher.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.thewitcher.config.GameConfig;
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

    public GameView(GameConfig config) {
        this.canvas = new Canvas(config.getWindowWidth(), config.getWindowHeight());
        this.gc = canvas.getGraphicsContext2D();
        this.pointWidth = config.getPointWidth();
        this.pointHeight = config.getPointHeight();
        this.viewWidth = config.getWindowWidth() / this.pointWidth;
        this.viewHeight = config.getWindowHeight() / this.pointHeight;
        gc.setFont(Font.font(config.getFontName(), config.getFontSize()));
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    private void renderMap(Location location, Player player, int viewX, int viewY, int offsetX, int offsetY) {
        gc.setFill(Color.LIGHTGRAY);
        for (int y = 0; y < viewHeight; y++) {
            for (int x = 0; x < viewWidth; x++) {
                int locationX = viewX + x - offsetX;
                int locationY = viewY + y - offsetY;
                if (!isInBounds(location, locationX, locationY)) continue;
                char marker = location.getPoint(locationX, locationY).getMarker();
                if (locationX == player.getX() && locationY == player.getY()) marker = 'G';
                gc.fillText(String.valueOf(marker), x * pointWidth, (y + 1) * pointHeight);
            }
        }
    }
    private boolean isInBounds(Location location, int x, int y) {
        return x >= 0 && y >= 0 && x < location.getWidth() && y < location.getHeight();
    }
}
