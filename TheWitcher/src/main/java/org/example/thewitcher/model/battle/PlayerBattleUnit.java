package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Character;
import org.example.thewitcher.model.entity.monster.Monster;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.items.Weapon;
import org.example.thewitcher.model.items.WeaponType;


public class PlayerBattleUnit implements IBattleUnit {
    private final Player player;
    private BattleAction action;

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
    @Override public void takeDamage(int dmg) { player.setHealth(player.getHealth() - dmg); }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return action; }

    private int silverAttack(IBattleUnit enemy) {
        if (player.getAppliedEquipment().getSilver() != null && player.getAppliedEquipment().getSilver().getCondition() > 0) {
            if (enemy.getEntity() instanceof Monster && ((Monster) enemy.getEntity()).getIsFlying()) return 0;
            if (enemy.getEntity() instanceof Monster) {
                player.getAppliedEquipment().getSilver().setCondition(player.getAppliedEquipment().getSilver().getCondition() - 1);
                return player.getAppliedEquipment().getSilver().getBonus();
            }
            player.getAppliedEquipment().getSilver().setCondition(player.getAppliedEquipment().getSilver().getCondition() - 3);
            return player.getAppliedEquipment().getSilver().getBonus() / 2;
        }
        return 0;
    }
    private int steelAttack(IBattleUnit enemy) {
        if (player.getAppliedEquipment().getSteel() != null && player.getAppliedEquipment().getSteel().getCondition() > 0) {
            if (enemy.getEntity() instanceof Monster && ((Monster) enemy.getEntity()).getIsFlying()) return 0;
            if (enemy.getEntity() instanceof Character) {
                player.getAppliedEquipment().getSteel().setCondition(player.getAppliedEquipment().getSteel().getCondition() - 1);
                return player.getAppliedEquipment().getSteel().getBonus();
            }
            player.getAppliedEquipment().getSteel().setCondition(player.getAppliedEquipment().getSteel().getCondition() - 3);
            return player.getAppliedEquipment().getSteel().getBonus() / 2;
        }
        return 0;
    }
    private int distanceAttack(IBattleUnit enemy) {
        if (player.getAppliedEquipment().getDistance() != null && player.getAppliedEquipment().getDistance().getCondition() > 0) {
            if (enemy.getEntity() instanceof Monster && !((Monster) enemy.getEntity()).getIsFlying()) return 0;
            player.getAppliedEquipment().getDistance().setCondition(player.getAppliedEquipment().getDistance().getCondition() - 1);
            return player.getAppliedEquipment().getDistance().getBonus();
        }
        return 0;
    }
}
