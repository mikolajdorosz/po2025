package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.player.Player;


public class PlayerBattleUnit implements IBattleUnit {
    private final Player player;
    private BattleAction action;

    public PlayerBattleUnit(Player player) { this.player = player; }

    @Override public Entity getEntity() { return player; }
    @Override public int getHealth() { return player.getHealth(); }
    @Override public boolean isAlive() { return player.getHealth() > 0; }
    @Override public void takeDamage(int dmg) { player.setHealth(player.getHealth() - dmg); }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return action; }
}
