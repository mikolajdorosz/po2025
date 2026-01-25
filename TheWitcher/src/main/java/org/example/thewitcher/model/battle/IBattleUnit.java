package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.Entity;

public interface IBattleUnit {
    Entity getEntity();
    int getHealth();
    boolean isAlive();
    void takeDamage(int dmg);
    void setAction(BattleAction action);
    BattleAction getAction();
}
