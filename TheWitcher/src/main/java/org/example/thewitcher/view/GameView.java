package org.example.thewitcher.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.thewitcher.config.GameConfig;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.battle.BattleView;
import org.example.thewitcher.view.inventory.InventoryView;
import org.example.thewitcher.view.map.MapView;
import org.example.thewitcher.view.menu.PauseMenuView;
import org.example.thewitcher.view.util.ViewportCalculator;
import org.example.thewitcher.view.interaction.*;

public class GameView {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final ViewportCalculator viewport;
    private final MapView mapView;
    private final InventoryView inventoryView;
    private final ArmorerView armorerView;
    private final InnkeeperView innkeeperView;
    private final BlacksmithView blacksmithView;
    private final MerchantView merchantView;
    private final SorceressView sorceressView;
    private final BattleView battleView;
    private final PauseMenuView pauseMenuView;

    public GameView(GameConfig config) {
        this.canvas = new Canvas(config.getWindowWidth(), config.getWindowHeight());
        this.gc = canvas.getGraphicsContext2D();
        this.viewport = new ViewportCalculator(
                config.getWindowWidth(),
                config.getWindowHeight(),
                config.getPointWidth(),
                config.getPointHeight()
        );
        this.mapView = new MapView(gc, viewport);
        this.inventoryView = new InventoryView(gc, canvas, viewport);
        this.armorerView = new ArmorerView(gc, canvas, viewport);
        this.innkeeperView = new InnkeeperView(gc, canvas, viewport);
        this.blacksmithView = new BlacksmithView(gc, canvas, viewport);
        this.merchantView = new MerchantView(gc, canvas, viewport);
        this.sorceressView = new SorceressView(gc, canvas, viewport);
        this.battleView = new BattleView(gc, canvas, viewport);
        this.pauseMenuView = new PauseMenuView(gc, canvas);

        this.gc.setFont(Font.font(config.getFontName(), config.getFontSize()));
    }

    public Canvas getCanvas() { return canvas; }

    public void render(Game game) {
        viewport.clear(gc, canvas);
        switch (game.getState()) {
            case MAP ->  mapView.render(game);
            case INVENTORY,
                 INVENTORY_ITEM_ACTION_MENU,
                 INVENTORY_INSPECT_ITEM -> inventoryView.render(game);
            case INTERACTION_ARMORER -> armorerView.render(game);
            case INTERACTION_INNKEEPER -> innkeeperView.render(game);
            case INTERACTION_BLACKSMITH -> blacksmithView.render(game);
            case INTERACTION_MERCHANT -> merchantView.render(game);
            case INTERACTION_SORCERESS -> sorceressView.render(game);
            case BATTLE -> battleView.render(game);
            case PAUSE_MENU -> pauseMenuView.render();
            case GAME_OVER -> renderGameOver();
        }
    }

    private void renderGameOver() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.RED);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 48));
        drawCenteredText("You died :(", height / 2 - 30, width);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.NORMAL, 20));
        drawCenteredText("Press ESC to exit", height / 2 + 30, width);
    }
    private void drawCenteredText(String text, double y, double width) {
        Text t = new Text(text);
        t.setFont(gc.getFont());
        double textWidth = t.getLayoutBounds().getWidth();
        gc.fillText(text, (width - textWidth) / 2, y);
    }
}
