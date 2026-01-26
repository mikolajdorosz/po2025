package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Character;
import org.example.thewitcher.model.entity.monster.Monster;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.items.Weapon;
import org.example.thewitcher.model.items.WeaponType;

import java.util.Random;


public class PlayerBattleUnit implements IBattleUnit {
    private final Player player;
    private BattleAction action;
    private String statusMessage;

    public PlayerBattleUnit(Player player) { this.player = player; }

    @Override public Entity getEntity() { return player; }
    @Override public int getHealth() { return player.getHealth(); }
    @Override public boolean isAlive() { return player.getHealth() > 0; }
    @Override public int attack(IBattleUnit enemy, WeaponType weapon) {
        return switch (weapon) {
            case SILVER -> silverAttack(enemy);
            case STEEL -> steelAttack(enemy);
            case DISTANCE -> distanceAttack(enemy);
            default -> 0;
        };
    }
    @Override public int attack() { return 0; }
    @Override public void takeDamage(int dmg) {
        if (player.getAppliedEquipment().getArmor() != null) {
            org.example.thewitcher.model.items.Armor armor = player.getAppliedEquipment().getArmor();
            if (armor.getCondition() > 0) {
                int newCondition = Math.max(0, armor.getCondition() - 1);
                armor.setCondition(newCondition);
                if (newCondition == 0) statusMessage = "Armor broken!";

                Random random = new Random();
                dmg -= (int)(random.nextDouble() * (double)armor.getBonus());
                if (dmg < 0) dmg = 0;
            }
        }
        player.setHealth(player.getHealth() - dmg);
    }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return action; }
    @Override public String getStatusMessage() { return statusMessage; }
    @Override public void resetStatusMessage() { statusMessage = null; }

    private int silverAttack(IBattleUnit enemy) {
        if (player.getAppliedEquipment().getSilver() != null && player.getAppliedEquipment().getSilver().getCondition() > 0) {
            if (enemy.getEntity() instanceof Monster && ((Monster) enemy.getEntity()).getIsFlying()) return 0;
            org.example.thewitcher.model.items.Silver weapon = player.getAppliedEquipment().getSilver();
            int conditionCost = (enemy.getEntity() instanceof Monster) ? 1 : 3;

            int newCondition = Math.max(0, weapon.getCondition() - conditionCost);
            weapon.setCondition(newCondition);
            if (newCondition == 0) statusMessage = "Silver Sword broken!";

            if (conditionCost == 3) return weapon.getBonus() / 2;
            return weapon.getBonus();
        }
        return 0;
    }
    private int steelAttack(IBattleUnit enemy) {
        if (player.getAppliedEquipment().getSteel() != null && player.getAppliedEquipment().getSteel().getCondition() > 0) {
            if (enemy.getEntity() instanceof Monster && ((Monster) enemy.getEntity()).getIsFlying()) return 0;
            org.example.thewitcher.model.items.Steel weapon = player.getAppliedEquipment().getSteel();
            int conditionCost = (enemy.getEntity() instanceof Character) ? 1 : 3;

            int newCondition = Math.max(0, weapon.getCondition() - conditionCost);
            weapon.setCondition(newCondition);
            if (newCondition == 0) statusMessage = "Steel Sword broken!";

            if (conditionCost == 3) return weapon.getBonus() / 2;
            return weapon.getBonus();
        }
        return 0;
    }
    private int distanceAttack(IBattleUnit enemy) {
        if (player.getAppliedEquipment().getDistance() != null && player.getAppliedEquipment().getDistance().getCondition() > 0) {
            if (enemy.getEntity() instanceof Monster && !((Monster) enemy.getEntity()).getIsFlying()) return 0;
            org.example.thewitcher.model.items.Distance weapon = player.getAppliedEquipment().getDistance();

            int newCondition = Math.max(0, weapon.getCondition() - 1);
            weapon.setCondition(newCondition);
            if (newCondition == 0) statusMessage = "Crossbow broken!";

            return weapon.getBonus();
        }
        return 0;
    }
}
