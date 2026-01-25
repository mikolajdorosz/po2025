package org.example.thewitcher.model.battle;
import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Character;

public class HumanBattleUnit implements IBattleUnit {
    private final Character human;
    private BattleAction action;

    public HumanBattleUnit(Character human) { this.human = human; }

    @Override public Entity getEntity() { return human; }
    @Override public int getHealth() { return human.getHealth(); }
    @Override public boolean isAlive() { return human.getHealth() > 0; }
    @Override public void takeDamage(int dmg) { human.setHealth(human.getHealth() - dmg); }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return action; }
}
