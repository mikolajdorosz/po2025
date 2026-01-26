package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.items.WeaponType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    public IBattleUnit getEnemy() { return enemyIndex >= 0 ? enemies.get(enemyIndex) : null; }
    public IBattleUnit getAlly() { return allies.get(allyIndex); }
    public void setEnemy(int enemyIndex) { this.enemyIndex = enemyIndex; }
    public void resetEnemy() { this.enemyIndex = -1; }
    public boolean isPlayerAlive() { return allies.stream().anyMatch(a -> a.getEntity() instanceof Player); }

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
        resetEnemy();
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
        if (allyIndex >= allies.size()) allyIndex = 0;
    }
    private void updateState() {
        if (allies.isEmpty() || enemies.isEmpty() || escaped) battleState = BattleState.FINISHED;
    }
    private boolean nextAlly() { allyIndex++; return allyIndex < allies.size(); }
    private void resetAllyTurn() { allyIndex = 0; }
    private void enemyTurn() {
        if (isFinished()) return;
        for (IBattleUnit enemy : enemies) {
            if (!enemy.isAlive()) continue;
            if (allies.isEmpty()) { battleState = BattleState.FINISHED; return; }
            allies.get(ThreadLocalRandom.current().nextInt(allies.size())).takeDamage(enemy.attack());
            cleanupDead();
            updateState();
            if (isFinished()) return;
        }
    }

    private void allyAttack(WeaponType weapon) {
        if (getEnemy() == null) return;
        System.out.println(getAlly().getEntity().getName());
        if (getAlly().getEntity() instanceof Player) getEnemy().takeDamage(getAlly().attack(getEnemy(), weapon));
        else getEnemy().takeDamage(getAlly().attack());

    }
    private void defend() { } // take damage instead of attacked ally
    private void drinkElixir() { } // boost ally
    public void escape() {
        escaped = true;
        battleState = BattleState.FINISHED;
    }

    public BattleResult getResult() { return new BattleResult(defeatedAllies, defeatedEnemies); }
    public boolean isFinished() { return battleState == BattleState.FINISHED; }
}
