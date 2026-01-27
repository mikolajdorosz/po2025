package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.battle.*;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.WeaponType;
import org.example.thewitcher.model.entity.character.Ally;

public class BattleController {
    private final Game game;

    public BattleController(Game game) { this.game = game; }

    public void handleInput(KeyCode code, String charInput) {
        if (game.getBattle() == null) return;
        // Clear status messages from previous turn/action
        clearStatusMessages(game.getBattle());

        switch (game.getBattle().getInputState()) {
            case ACTION -> handleAction(code);
            case ENEMY -> handleEnemy(charInput);
            case WEAPON -> handleWeapon(code);
            case ELIXIR_SELECTION -> handleElixirSelection(code);
        }
        if (game.getBattle().isFinished()) finishBattle();
    }

    private void clearStatusMessages(Battle battle) {
        for (IBattleUnit ally : battle.getAllies()) {
            ally.resetStatusMessage();
        }
    }
    private void handleAction(KeyCode code) {
        switch (code) {
            case DIGIT1 -> game.getBattle().allysAction(BattleAction.ATTACK);
            case DIGIT2 -> {
                if (game.getBattle().isPlayerTurn()) game.getBattle().allysAction(BattleAction.DRINK_ELIXIR);
                else game.getBattle().allysAction(BattleAction.DEFEND);
            }
            case DIGIT3 -> {
                if (game.getBattle().isPlayerTurn()) game.getBattle().allysAction(BattleAction.ESCAPE);
                else game.getBattle().allysAction(BattleAction.DRINK_ELIXIR);
            }
        }
    }
    private void handleEnemy(String charInput) {
        if (charInput == null || charInput.isEmpty()) return;
        int index = Character.toLowerCase(charInput.charAt(0)) - 'a';
        if (index >= 0 && index < game.getBattle().getEnemies().size()) game.getBattle().selectEnemy(index);
    }
    private void handleWeapon(KeyCode code) {
        WeaponType weapon = switch (code) {
            case DIGIT1 -> WeaponType.SILVER;
            case DIGIT2 -> WeaponType.STEEL;
            case DIGIT3 -> WeaponType.DISTANCE;
            default -> null;
        };
        if (weapon != null) game.getBattle().chooseWeapon(weapon);
    }
    private void handleElixirSelection(KeyCode code) {
        if (code == KeyCode.ESCAPE) { game.getBattle().setInputState(BattleInputState.ACTION); return; }
        if (code.isDigitKey()) {
            int index = Integer.parseInt(code.getChar()) - 1;
            game.getBattle().selectElixir(index);
        }
    }
    private void finishBattle() {
        BattleResult result = game.getBattle().getResult();

        // Remove defeated enemies from map
        for (IBattleUnit enemy : result.defeatedEnemies()) {
            game.getLocation().removeEnemy(enemy);
        }

        game.getPlayer().getAllies().removeAll(result.defeatedAllies());

        // Remove dead allies from map
        for (IBattleUnit unit : result.defeatedAllies()) {
            if (unit.getEntity() instanceof Ally ally) {
                game.getLocation().removeEntity(ally);
            }
        }

        // Recruit surviving allies
        for (IBattleUnit unit : game.getBattle().getAllies()) {
            if (unit.getEntity() instanceof Ally ally && !game.getPlayer().getAllies().contains(unit)) {
                game.getPlayer().getAllies().add(unit);
                game.getLocation().removeEntity(ally);
            }
        }

        game.setBattle(null);
        game.setState(GameState.MAP);
    }
}
