package org.example.thewitcher.view.battle;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.util.ViewportCalculator;

public class BattleView {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final ViewportCalculator viewport;

    public BattleView(GraphicsContext gc, Canvas canvas, ViewportCalculator viewport) {
        this.gc = gc;
        this.canvas = canvas;
        this.viewport = viewport;
    }

    public void render(Game game) {
        if (game.getBattle() == null) return;
        double width = canvas.getWidth();;
        double height = canvas.getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        double fontSize = Math.max(12, height / 35);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, fontSize));
        gc.setFill(Color.LIGHTGRAY);

        double alliesX = width * 0.05;
        double enemiesX = width * 0.55;
        double headerY = fontSize * 1.5;
        double contentY = headerY + fontSize * 2;

        drawCenteredText("BATTLE", headerY, width);
        gc.fillText("ALLIES", alliesX, headerY);
        gc.fillText("ENEMIES", width * 0.8, headerY);
        drawAllies(game, alliesX, contentY, fontSize);
        drawEnemies(game, enemiesX, contentY, fontSize);
        drawCenteredText("[1] ATTACK  [2] DEFEND  [3] ELIXIR  [4] ESCAPE", height - fontSize, width);
        gc.setFont(Font.font("Consolas", FontWeight.NORMAL, 20));
    }

    private void drawAllies(Game game, double x, double y, double lineHeight) {
        int index = 0;
        int active = game.getBattle().getActiveAllyIndex();
        for (IBattleUnit ally : game.getBattle().getAllies()) {
            String marker = (index == active) ? ">>" : "  ";
            gc.fillText(
                    String.format(
                            "%s %-10s HP:%-3d AR:%-2d ACT:%s",
                            marker,
                            ally.getEntity().getName(),
                            ally.getHealth(),
                            ally.getEntity() instanceof Player ? ((Player) ally.getEntity()).getArmor() : 0,
                            ally.getAction()
                    ), x, y
            );
            y += lineHeight * 1.4;
            index++;
        }
    }
    private void drawEnemies(Game game, double x, double y, double lineHeight) {
        for (IBattleUnit enemy : game.getBattle().getEnemies()) {
            gc.fillText(
                    String.format(
                            "%-10s HP:%-3d ACT:%s",
                            enemy.getEntity().getName(),
                            enemy.getHealth(),
                            enemy.getAction()
                    ), x, y
            );
            y += lineHeight * 1.4;
        }
    }
    private void drawCenteredText(String text, double y, double width) {
        double textWidth = text.length() * 8;
        gc.fillText(text, (width - textWidth) / 2, y);
    }
}
