package org.example.thewitcher.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.thewitcher.config.GameConfig;
import org.example.thewitcher.model.entity.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
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
        clearCanvas();
        switch (game.getState()) {
            case MAP -> {
                Player player = game.getPlayer();
                Location location = game.getLocation();
                int viewX = calculateViewX(player, location);
                int viewY = calculateViewY(player, location);
                int offsetX = calculateOffsetX(location);
                int offsetY = calculateOffsetY(location);
                renderMap(location, player, viewX, viewY, offsetX, offsetY);
            }
            case INVENTORY, ITEM_ACTION_MENU -> renderInventory(game);
            case INSPECT_ITEM -> {
                renderInventory(game);
                renderInspectionWindow(game);
            }
        }
    }

    private void renderInventory(Game game) {
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
        if (!game.getMessage().isEmpty() && game.getState() != GameState.INSPECT_ITEM) {
            gc.setFill(Color.YELLOW);
            gc.fillText(game.getMessage(), 20, canvas.getHeight() - 50);
        }

        // Draw Action Menu Overlay
        if (game.getState() == org.example.thewitcher.model.game.GameState.ITEM_ACTION_MENU && game.getSelectedItem() != null) {
            renderActionMenu(game);
        }
    }

    private void renderActionMenu(Game game) {
        double w = 400;
        double h = 200;
        double x = (canvas.getWidth() - w) / 2;
        double y = (canvas.getHeight() - h) / 2;

        gc.setFill(Color.rgb(0, 0, 0, 0.9));
        gc.fillRect(x, y, w, h);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(x, y, w, h);

        gc.setFill(Color.WHITE);
        int textX = (int)x + 20;
        int textY = (int)y + 30;

        gc.fillText("Selected: " + game.getSelectedItem().getName(), textX, textY);
        textY += 30;

        String type = game.getSelectedItem().getType();
        gc.fillText("1 - Inspect", textX, textY); textY += 20;

        if (type.equals("food")) {
            gc.fillText("2 - Use", textX, textY); textY += 20;
        } else if (type.equals("silver") || type.equals("steel") || type.equals("distance") || type.equals("armor")) {
            gc.fillText("2 - Equip/Unequip", textX, textY); textY += 20;
        }

        gc.fillText("3 - Drop", textX, textY); textY += 20;
        gc.fillText("0 - Cancel", textX, textY);
    }

    private void renderInspectionWindow(Game game) {
        double w = 500;
        double h = 300;
        double x = (canvas.getWidth() - w) / 2;
        double y = (canvas.getHeight() - h) / 2;

        gc.setFill(Color.rgb(0, 0, 0, 0.95));
        gc.fillRect(x, y, w, h);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(x, y, w, h);

        gc.setFill(Color.WHITE);
        int textX = (int)x + 20;
        int textY = (int)y + 30;

        gc.fillText("INSPECTION", textX, textY);
        textY += 30;

        String[] lines = game.getMessage().split("\n");
        for (String line : lines) {
            gc.fillText(line, textX, textY);
            textY += 20;
        }

        textY += 20;
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Press any key to close", textX, textY);
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
