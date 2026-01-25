package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.battle.BattleAction;
import org.example.thewitcher.model.battle.BattleResult;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;

public class BattleController {
    private final Game game;

    public BattleController(Game game) { this.game = game; }

    public void handleInput(KeyCode code) {
        switch (game.getBattle().getBattleState()) {
            case ACTIVE -> performAction(code);
        }
    }

    private void performAction(KeyCode code) {
        switch (code) {
            case DIGIT1 -> game.getBattle().allysAction(BattleAction.ATTACK);
            case DIGIT2 -> game.getBattle().allysAction(BattleAction.DEFEND);
            case DIGIT3 -> game.getBattle().allysAction(BattleAction.DRINK_ELIXIR);
            case DIGIT4 -> game.getBattle().allysAction(BattleAction.ESCAPE);
        }
        if (game.getBattle().isFinished()) finishBattle();
    }
    private void finishBattle() {
        BattleResult result = game.getBattle().getResult();
        game.getLocation().getEnemies().removeAll(result.defeatedEnemies());
        game.getPlayer().getAllies().removeAll(result.defeatedAllies());
        game.setBattle(null);
        game.setState(GameState.MAP);
    }
}
