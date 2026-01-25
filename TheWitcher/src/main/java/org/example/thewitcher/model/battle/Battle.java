package org.example.thewitcher.model.battle;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final List<IBattleUnit> allies;
    private final List<IBattleUnit> enemies;
    private final List<IBattleUnit> defeatedAllies;
    private final List<IBattleUnit> defeatedEnemies;
    private BattleState battleState;
    private boolean escaped;
    private int activeAllyIndex;

    public Battle(List<IBattleUnit> allies, List<IBattleUnit> enemies) {
        this.allies = new ArrayList<>(allies);
        this.enemies = new ArrayList<>(enemies);
        this.defeatedAllies = new ArrayList<>();
        this.defeatedEnemies = new ArrayList<>();
        this.battleState = BattleState.ACTIVE;
        this.escaped = false;
        this.activeAllyIndex = 0;
    }

    public List<IBattleUnit> getAllies() { return allies; }
    public List<IBattleUnit> getEnemies() { return enemies; }
    public BattleState getBattleState() { return battleState; }
    public int getActiveAllyIndex() { return activeAllyIndex; }

    public void allysAction(BattleAction action) {
        switch (action) {
             case ATTACK -> attack();
             case DEFEND -> defend();
             case DRINK_ELIXIR -> drinkElixir();
             case ESCAPE -> escape();
        }
        cleanupDead();
        updateState();
        if (isFinished()) return;
        boolean hasNext = nextAlly();
        if (!hasNext) {
            resetAllyTurn();
            enemyTurn();
        }
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
    private boolean nextAlly() { activeAllyIndex++; return activeAllyIndex < allies.size(); }
    private void resetAllyTurn() { activeAllyIndex = 0; }
    private void enemyTurn() {
        for (IBattleUnit enemy : enemies) {
            if (!enemy.isAlive()) continue;
            switch (enemy.getAction()) {
                case ATTACK -> attack();
                case DEFEND -> defend();
            }
        }
    }

    private void attack() { }
    private void defend() { }
    private void drinkElixir() { }
    public void escape() {
        escaped = true;
        battleState = BattleState.FINISHED;
    }

    public BattleResult getResult() { return new BattleResult(defeatedAllies, defeatedEnemies); }
    public boolean isFinished() { return battleState == BattleState.FINISHED; }
}
