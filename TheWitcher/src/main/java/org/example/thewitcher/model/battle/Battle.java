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
    private boolean isAllyAttacking;

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
        this.isAllyAttacking = false;
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
    public boolean getIsAllyAttacking() { return isAllyAttacking; }
    public void setIsAllyAttacking(boolean value) { this.isAllyAttacking = value; }

    public void allysAction(BattleAction action) {
        pendingAction = action;
        if (action == BattleAction.ATTACK) { setInputState(BattleInputState.ENEMY); return; }
        executePendingAction();
    }
    public void executePendingAction() {
        switch (pendingAction) {
            case ATTACK -> {
                if (!isAllyAttacking) setInputState(BattleInputState.WEAPON);
                else setInputState(BattleInputState.ACTION);
                return;
            }
            case DEFEND -> defend();
            case DRINK_ELIXIR -> { drinkElixir(); return;}
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
        playerAttack(pendingWeapon);
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
    public void executeAttack() {
        allysAttack();
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
    private void resetAllyTurn() {
        allyIndex = 0;
        isAllyAttacking = false;
        resetDefense();
    }
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

    private void playerAttack(WeaponType weapon) {
        if (getEnemy() == null || !(getAlly().getEntity() instanceof Player)) return;
        getEnemy().takeDamage(getAlly().attack(getEnemy(), weapon));
        isAllyAttacking = true;
    }
    private void allysAttack() { getEnemy().takeDamage(getAlly().attack()); }
    private void defend() { getAlly().setDefending(true); }

    private void drinkElixir() { setInputState(BattleInputState.ELIXIR_SELECTION); }

    public void selectElixir(int index) {
        // Find player to access inventory
        IBattleUnit playerUnit = allies.stream()
                .filter(a -> a.getEntity() instanceof Player)
                .findFirst()
                .orElse(null);

        if (playerUnit == null) return;
        Player player = (Player) playerUnit.getEntity();

        // Get Elixirs
        List<org.example.thewitcher.model.items.Item> elixirs = new ArrayList<>();
        for (org.example.thewitcher.model.items.Item item : player.getEquipment().getItems()) {
            if (item instanceof org.example.thewitcher.model.items.Elixir) {
                elixirs.add(item);
            }
        }

        if (index >= 0 && index < elixirs.size()) {
            org.example.thewitcher.model.items.Item elixir = elixirs.get(index);
            // Apply effect to CURRENT ally (could be Player or Ally)
            getAlly().applyEffect(elixir.getName());

            // Remove from inventory
            player.getEquipment().removeItem(elixir);

            setInputState(BattleInputState.ACTION);
        }
    }

    public void escape() {
        escaped = true;
        battleState = BattleState.FINISHED;
    }

    private void resetDefense() {
        allies.forEach(ally -> ally.setDefending(false));
    }

    public BattleResult getResult() { return new BattleResult(defeatedAllies, defeatedEnemies); }
    public boolean isFinished() { return battleState == BattleState.FINISHED; }
}
