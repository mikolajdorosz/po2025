package org.example.thewitcher.view.map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.map.Location;
import org.example.thewitcher.model.map.data.ObjectDrawings;
import org.example.thewitcher.model.map.point.Point;
import org.example.thewitcher.view.util.ViewportCalculator;

public class MapView {
    private final GraphicsContext gc;
    private final ViewportCalculator viewport;

    public MapView(GraphicsContext gc, ViewportCalculator viewport) {
        this.gc = gc;
        this.viewport = viewport;
    }

    public void render(Game game) {
        Player player = game.getPlayer();
        Location location = game.getLocation();
        viewport.calculate(player, location);

        gc.setFill(Color.LIGHTGRAY);
        for (int y = 0; y < viewport.getViewHeight(); y++) {
            for (int x = 0; x < viewport.getViewWidth(); x++) {
                if (!isInBounds(location, viewport.getLocationX(x), viewport.getLocationY(y))) continue;
                Point point = location.getPoint(viewport.getLocationX(x), viewport.getLocationY(y));
                char marker = point.getMarker();
                if (viewport.getLocationX(x) == player.getX() && viewport.getLocationY(y) == player.getY()) marker = ObjectDrawings.player();
                gc.fillText(String.valueOf(marker), x * viewport.getPointWidth(), (y + 1) * viewport.getPointHeight());
            }
        }
    }

    private boolean isInBounds(Location location, int x, int y) {
        return x >= 0 && y >= 0 && x < location.getWidth() && y < location.getHeight();
    }
}
