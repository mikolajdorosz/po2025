package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.items.Weapon;
import org.example.thewitcher.model.items.WeaponType;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final List<IBattleUnit> allies;
    private final List<IBattleUnit> enemies;
    private final List<IBattleUnit> defeatedAllies;
    private final List<IBattleUnit> defeatedEnemies;
    private BattleState battleState;
    private boolean escaped;
    private int allyIndex;
    private int enemyIndex;
    private BattleInputState inputState;
    private BattleAction pendingAction;
    private WeaponType pendingWeapon;

    public Battle(List<IBattleUnit> allies, List<IBattleUnit> enemies) {
        this.allies = new ArrayList<>(allies);
        this.enemies = new ArrayList<>(enemies);
        this.defeatedAllies = new ArrayList<>();
        this.defeatedEnemies = new ArrayList<>();
        this.battleState = BattleState.ACTIVE;
        this.escaped = false;
        this.allyIndex = 0;
        this.enemyIndex = -1;
        this.inputState = BattleInputState.ACTION;
    }

    public List<IBattleUnit> getAllies() { return allies; }
    public List<IBattleUnit> getEnemies() { return enemies; }
    public BattleState getBattleState() { return battleState; }
    public int getAllyIndex() { return allyIndex; }
    public BattleInputState getInputState() { return inputState; }
    public void setInputState(BattleInputState state) { this.inputState = state; }

    public boolean isEnemySelected() { return enemyIndex != -1; }
    public IBattleUnit getEnemy() { return enemies.get(enemyIndex); }
    public IBattleUnit getAlly() { return allies.get(allyIndex); }
    public void setEnemy(int enemyIndex) { this.enemyIndex = enemyIndex; }
    public void resetEnemy() { this.enemyIndex = -1; inputState = BattleInputState.ACTION; }

    public void allysAction(BattleAction action) {
        pendingAction = action;
        if (action == BattleAction.ATTACK) { setInputState(BattleInputState.ENEMY); return; }
        executePendingAction();
    }
    public void executePendingAction() {
        switch (pendingAction) {
            case ATTACK -> { setInputState(BattleInputState.WEAPON); return; }
            case DEFEND -> defend();
            case DRINK_ELIXIR -> drinkElixir();
            case ESCAPE -> escape();
        }
        pendingAction = null;
        cleanupDead();
        updateState();
        if (isFinished()) return;
        if (!nextAlly()) {
            resetAllyTurn();
            enemyTurn();
        }
        setInputState(BattleInputState.ACTION);
    }
    public void chooseWeapon(WeaponType weapon) {
        this.pendingWeapon = weapon;
        executeAttackWithWeapon();
    }
    public void executeAttackWithWeapon() {
        allyAttack(pendingWeapon);
        pendingAction = null;
        pendingWeapon = null;
        cleanupDead();
        updateState();
        if (isFinished()) return;
        if (!nextAlly()) {
            resetAllyTurn();
            enemyTurn();
        }
        setInputState(BattleInputState.ACTION);
    }
    private void cleanupDead() {
        allies.removeIf(ally -> {
            if (!ally.isAlive()) { defeatedAllies.add(ally); return true; }
            return false;
        });
        enemies.removeIf(enemy -> {
            if (!enemy.isAlive()) { defeatedEnemies.add(enemy); return true; }
            return false;
        });
    }
    private void updateState() {
        if (allies.isEmpty() || enemies.isEmpty() || escaped) battleState = BattleState.FINISHED;
    }
    private boolean nextAlly() { allyIndex++; return allyIndex < allies.size(); }
    private void resetAllyTurn() { allyIndex = 0; }
    private void enemyTurn() {
        for (IBattleUnit enemy : enemies) {
            if (!enemy.isAlive()) continue;
            switch (enemy.getAction()) {
                //case ATTACK -> attack();
                //case DEFEND -> defend();
            }
        }
    }

    private void allyAttack(WeaponType weapon) {
        if (getAlly().getEntity() instanceof Player) getAlly().attack(getEnemy(), weapon);
        else getAlly().attack(getEnemy());
    }
    private void defend() { }
    private void drinkElixir() { }
    public void escape() {
        escaped = true;
        battleState = BattleState.FINISHED;
    }

    public BattleResult getResult() { return new BattleResult(defeatedAllies, defeatedEnemies); }
    public boolean isFinished() { return battleState == BattleState.FINISHED; }
}
