package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.items.Weapon;
import org.example.thewitcher.model.items.WeaponType;

public interface IBattleUnit {
    Entity getEntity();
    int getHealth();
    boolean isAlive();
    int attack(IBattleUnit enemy, WeaponType weapon);
    int attack();
    void takeDamage(int dmg);
    void setAction(BattleAction action);
    BattleAction getAction();
}
