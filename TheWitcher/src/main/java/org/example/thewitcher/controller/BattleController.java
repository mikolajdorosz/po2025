package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.battle.BattleAction;
import org.example.thewitcher.model.battle.BattleInputState;
import org.example.thewitcher.model.battle.BattleResult;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.WeaponType;
import org.example.thewitcher.model.battle.IBattleUnit;
import org.example.thewitcher.model.entity.character.Ally;

public class BattleController {
    private final Game game;

    public BattleController(Game game) { this.game = game; }

    public void handleInput(KeyCode code, String charInput) {
        switch (game.getBattle().getInputState()) {
            case ACTION -> handleAction(code);
            case ENEMY -> handleEnemy(charInput);
            case WEAPON -> handleWeapon(code);
        }
    }
    private void handleAction(KeyCode code) {
        switch (code) {
            case DIGIT1 -> game.getBattle().allysAction(BattleAction.ATTACK);
            case DIGIT2 -> game.getBattle().allysAction(BattleAction.DEFEND);
            case DIGIT3 -> game.getBattle().allysAction(BattleAction.DRINK_ELIXIR);
            case DIGIT4 -> { game.getBattle().allysAction(BattleAction.ESCAPE); finishBattle(); }
        }
        if (game.getBattle().isFinished()) finishBattle();
    }
    private void handleEnemy(String charInput) {
        if (charInput == null || charInput.isEmpty()) return;
        int index = Character.toLowerCase(charInput.charAt(0)) - 'a';
        if (index >= 0 && index < game.getBattle().getEnemies().size()) {
            game.getBattle().setEnemy(index);
            game.getBattle().executePendingAction();
            if (game.getBattle().isFinished()) finishBattle();
        }
    }
    private void handleWeapon(KeyCode code) {
        WeaponType weapon = switch (code) {
            case DIGIT1 -> WeaponType.SILVER;
            case DIGIT2 -> WeaponType.STEEL;
            case DIGIT3 -> WeaponType.DISTANCE;
            default -> null;
        };
        if (game.getBattle().isFinished()) finishBattle();
        if (weapon == null) return;
        game.getBattle().chooseWeapon(weapon);
    }
    private void finishBattle() {
        BattleResult result = game.getBattle().getResult();
        game.getLocation().getEnemies().removeAll(result.defeatedEnemies());
        game.getPlayer().getAllies().removeAll(result.defeatedAllies());

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
