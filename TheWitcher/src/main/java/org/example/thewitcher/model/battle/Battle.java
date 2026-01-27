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
    private BattleInputState inputState;
    private boolean escaped;
    private boolean isAllyAttacking;
    private int allyIndex;
    private int enemyIndex;
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
        this.isAllyAttacking = false;
    }

    public List<IBattleUnit> getAllies() { return allies; }
    public List<IBattleUnit> getEnemies() { return enemies; }
    public int getAllyIndex() { return allyIndex; }
    public BattleInputState getInputState() { return inputState; }
    public void setInputState(BattleInputState state) { this.inputState = state; }

    public IBattleUnit getEnemy() { return enemyIndex >= 0 ? enemies.get(enemyIndex) : null; }
    public IBattleUnit getAlly() { return allies.get(allyIndex); }
    public void setEnemy(int enemyIndex) { this.enemyIndex = enemyIndex; }
    public boolean isPlayerAlive() { return allies.stream().anyMatch(a -> a.getEntity() instanceof Player); }
    public boolean isPlayerTurn() { return getAlly().getEntity() instanceof Player; }

    public boolean isFinished() { return battleState == BattleState.FINISHED; }

    // ========================= PLAYER INPUT =========================
    public void allysAction(BattleAction action) {
        pendingAction = action;
        switch (action) {
            case ATTACK -> inputState = BattleInputState.ENEMY;
            case DEFEND -> { defend(); endTurn(); }
            case DRINK_ELIXIR -> { if (!isPlayerTurn()) return; inputState = BattleInputState.ELIXIR_SELECTION; }
            case ESCAPE ->{ if (!isPlayerTurn()) return; escape(); }
        }
    }
    public void selectEnemy(int index) {
        enemyIndex = index;
        if (getAlly().getEntity() instanceof Player && !isAllyAttacking) {
            inputState = BattleInputState.WEAPON; return;
        }
        performAttack();
    }
    public void chooseWeapon(WeaponType weapon) {
        this.pendingWeapon = weapon;
        performAttack();
    }
    public void selectElixir(int index) {
        if (!isPlayerTurn()) return;
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

            inputState = BattleInputState.ACTION;
        }
    }

    //======================= BATTLE LOGIC =======================
    public void performAttack() {
        if (getAlly() == null) return;
        if (getAlly().getEntity() instanceof Player && pendingWeapon != null) {
            getEnemy().takeDamage(getAlly().attack(getEnemy(), pendingWeapon));
            isAllyAttacking = true;
        } else getEnemy().takeDamage(getAlly().attack());
        endTurn();
    }
    private void defend() { getAlly().setDefending(true); }
    private void drinkElixir() { setInputState(BattleInputState.ELIXIR_SELECTION); }
    public void escape() {
        escaped = true;
        updateState();
    }

    // ======================= TURN =======================
    private void endTurn() {
        pendingAction = null;
        pendingWeapon = null;
        enemyIndex = -1;
        cleanupDead();
        updateState();
        if (isFinished()) return;
        if (!nextAlly()) {
            resetAllyTurn();
            enemyTurn();
        }
        inputState = BattleInputState.ACTION;
    }
    private boolean nextAlly() { allyIndex++; return allyIndex < allies.size(); }
    private void resetAllyTurn() {
        allyIndex = 0;
        isAllyAttacking = false;
        allies.forEach(a -> a.setDefending(false));
    }
    private void enemyTurn() {
        for (IBattleUnit enemy : enemies) {
            if (!enemy.isAlive() || allies.isEmpty()) break;
            IBattleUnit target = allies.get(ThreadLocalRandom.current().nextInt(allies.size()));
            target.takeDamage(enemy.attack());
            cleanupDead();
            updateState();
            if (isFinished()) return;
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
        if (allyIndex >= allies.size()) allyIndex = 0;
    }
    private void updateState() {
        if (allies.isEmpty() || enemies.isEmpty() || escaped) battleState = BattleState.FINISHED;
    }
    public BattleResult getResult() { return new BattleResult(defeatedAllies, defeatedEnemies); }
}
