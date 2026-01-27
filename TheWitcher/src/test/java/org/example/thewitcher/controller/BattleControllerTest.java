package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.battle.*;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.battle.PlayerBattleUnit;
import org.example.thewitcher.model.entity.monster.Monster;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.items.Silver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BattleControllerTest {

    static class TestMonster extends Monster {
        public TestMonster() {
            super(0, 0, "Ghoul");
            this.health = 30;
            this.damage = 5;
        }
    }
    private Game game;
    private Battle battle;
    private BattleController controller;
    private MonsterBattleUnit monsterUnit;

    @Before public void setup() {
        game = new Game();
        Player player = game.getPlayer();
        // Equip Silver
        Item silver = player.getEquipment().getItems().stream()
                .filter(i -> i instanceof Silver)
                .findFirst()
                .orElseThrow();
        player = player.equipItem(silver);
        // Rebuild allies after equipping
        player.setAllies(new ArrayList<>());
        player.getAllies().add(new PlayerBattleUnit(player));
        Monster monster = new TestMonster();
        monsterUnit = new MonsterBattleUnit(monster);
        battle = new Battle(player.getAllies(), List.of(monsterUnit));
        game.setBattle(battle);
        controller = new BattleController(game);
    }

    // ================= ACTION FLOW =================
    @Test public void digit1_attack_movesToEnemySelection() {
        controller.handleInput(KeyCode.DIGIT1, null);
        assertEquals(BattleInputState.ENEMY, battle.getInputState());
    }
    @Test public void selectEnemy_executesAttack() {
        int hpBefore = monsterUnit.getHealth();
        controller.handleInput(KeyCode.DIGIT1, null);
        controller.handleInput(null, "a");
        controller.handleInput(KeyCode.DIGIT1, null);
        assertTrue(monsterUnit.getHealth() < hpBefore);
    }

    // ================= WEAPON =================
    @Test public void weaponSelection_worksViaDigits() {
        controller.handleInput(KeyCode.DIGIT1, null);
        controller.handleInput(KeyCode.DIGIT1, null);
        assertEquals(BattleInputState.ENEMY, battle.getInputState());
    }

    // ================= ELIXIR =================
    @Test public void drinkElixir_opensElixirSelection() {
        controller.handleInput(KeyCode.DIGIT2, null);
        assertEquals(BattleInputState.ELIXIR_SELECTION, battle.getInputState());
    }
    @Test public void escapeFromElixir_returnsToAction() {
        controller.handleInput(KeyCode.DIGIT2, null);
        controller.handleInput(KeyCode.ESCAPE, null);
        assertEquals(BattleInputState.ACTION, battle.getInputState());
    }

    // ================= ESCAPE =================
    @Test public void escapeEndsBattleAndReturnsToMap() {
        controller.handleInput(KeyCode.DIGIT3, null);
        assertNull(game.getBattle());
        assertEquals(GameState.MAP, game.getState());
    }

    // ================= FINISH =================
    @Test public void killingEnemy_finishesBattle() {
        monsterUnit.takeDamage(29);
        controller.handleInput(KeyCode.DIGIT1, null);
        controller.handleInput(null, "a");
        controller.handleInput(KeyCode.DIGIT1, null);
        controller.handleInput(KeyCode.DIGIT1, null);
        assertNull(game.getBattle());
    }
}
