package org.example.thewitcher.view.battle.overlay;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;

public class ElixirMenuOverlay {

    public void draw(GraphicsContext gc, Game game, double width, double height, double fontSize) {
        // Find player
        IBattleUnit playerUnit = game.getBattle().getAllies().stream()
                .filter(a -> a.getEntity() instanceof Player)
                .findFirst()
                .orElse(null);

        if (playerUnit == null) return;
        Player player = (Player) playerUnit.getEntity();

        double x = width * 0.3;
        double y = height * 0.4;

        gc.setFill(Color.BLACK);
        gc.fillRect(width * 0.2, height * 0.3, width * 0.6, height * 0.4);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(width * 0.2, height * 0.3, width * 0.6, height * 0.4);

        gc.setFill(Color.WHITE);
        gc.fillText("AVAILABLE ELIXIRS:", x, y);
        y += fontSize * 1.5;

        int index = 1;
        for (org.example.thewitcher.model.items.Item item : player.getEquipment().getItems()) {
            if (item instanceof org.example.thewitcher.model.items.Elixir) {
                gc.fillText("[" + index + "] " + item.getName(), x, y);
                y += fontSize * 1.2;
                index++;
            }
        }
        if (index == 1) {
            gc.fillText("No Elixirs available!", x, y);
        }
    }
}
