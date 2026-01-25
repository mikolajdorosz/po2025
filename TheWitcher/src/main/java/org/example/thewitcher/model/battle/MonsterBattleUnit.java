package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.monster.Monster;
import org.example.thewitcher.model.items.WeaponType;

public class MonsterBattleUnit implements IBattleUnit {
    private final Monster monster;
    private BattleAction action;

    public MonsterBattleUnit(Monster monster) { this.monster = monster; }

    @Override public Entity getEntity() { return monster; }
    @Override public int getHealth() { return monster.getHealth(); }
    @Override public boolean isAlive() { return monster.getHealth() > 0; }
    @Override public int attack() { return monster.attack(); }
    @Override public int attack(IBattleUnit enemy, WeaponType weapon) { return 0; }
    @Override public void takeDamage(int dmg) { monster.setHealth(monster.getHealth() - dmg); }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return BattleAction.ATTACK; }
}
