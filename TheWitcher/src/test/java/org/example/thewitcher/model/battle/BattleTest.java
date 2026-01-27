package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.entity.monster.Monster;
import org.example.thewitcher.model.items.Elixir;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.items.Silver;
import org.example.thewitcher.model.items.WeaponType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BattleTest {

    static class TestMonster extends Monster {
        public TestMonster() {
            super(0, 0, "Ghoul");
            this.health = 30;
            this.damage = 5;
            this.flying = false;
        }
    }
    private Battle battle;
    private Player player;
    private MonsterBattleUnit monsterUnit;

    @Before public void setup() {
        player = new Player();
        player.setHealth(50);
        // Equip Silver
        Item silver = player.getEquipment().getItems().stream()
                .filter(i -> i instanceof Silver)
                .findFirst()
                .orElseThrow();
        player = player.equipItem(silver);
        // Get the player's battle unit
        PlayerBattleUnit playerUnit = (PlayerBattleUnit) player.getAllies().getFirst();
        TestMonster monster = new TestMonster();
        monsterUnit = new MonsterBattleUnit(monster);
        battle = new Battle(List.of(playerUnit), List.of(monsterUnit));
    }

    // ===================== TESTS =====================
    @Test public void initialState_isActive() {
        assertFalse(battle.isFinished());
        assertEquals(BattleInputState.ACTION, battle.getInputState());
        assertTrue(battle.isPlayerTurn());
    }
    @Test public void playerAttackWithWeapon_damagesEnemy() {
        battle.allysAction(BattleAction.ATTACK);
        battle.selectEnemy(0);
        battle.chooseWeapon(WeaponType.SILVER);
        assertTrue(monsterUnit.getHealth() < 30);
    }
    @Test public void escape_endsBattleImmediately() {
        battle.allysAction(BattleAction.ESCAPE);
        assertTrue(battle.isFinished());
    }
    @Test public void drinkElixir_removesFromInventory() {
        Elixir elixir = (Elixir) player.getEquipment().getItems().stream()
                .filter(i -> i instanceof Elixir)
                .findFirst()
                .orElseThrow();
        battle.allysAction(BattleAction.DRINK_ELIXIR);
        assertEquals(BattleInputState.ELIXIR_SELECTION, battle.getInputState());

        battle.selectElixir(0);
        assertFalse(player.getEquipment().getItems().contains(elixir));
        assertEquals(BattleInputState.ACTION, battle.getInputState());
    }
    @Test public void enemyTurn_damagesPlayer() {
        int hpBefore = player.getHealth();
        battle.allysAction(BattleAction.ATTACK);
        battle.selectEnemy(0);
        battle.chooseWeapon(WeaponType.SILVER);
        assertTrue(player.getHealth() < hpBefore);
    }
    @Test public void monsterDies_battleEnds() {
        monsterUnit.takeDamage(100);
        battle.allysAction(BattleAction.ATTACK);
        battle.selectEnemy(0);
        battle.chooseWeapon(WeaponType.SILVER);
        assertTrue(battle.isFinished());
        assertTrue(battle.getEnemies().isEmpty());
    }
}
