package org.example.thewitcher.model.battle;
import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Bandit;
import org.example.thewitcher.model.entity.character.Character;
import org.example.thewitcher.model.items.WeaponType;

public class CharacterBattleUnit implements IBattleUnit {
    private final Character character;
    private BattleAction action;

    public CharacterBattleUnit(Character character) { this.character = character; }

    @Override public Entity getEntity() { return character; }
    @Override public int getHealth() { return character.getHealth(); }
    @Override public boolean isAlive() { return character.getHealth() > 0; }
    @Override public int attack() { return character instanceof Bandit ? ((Bandit) character).attack() : 0; }
    @Override public int attack(IBattleUnit enemy, WeaponType weapon) { return 0; }
    @Override public void takeDamage(int dmg) { character.setHealth(character.getHealth() - dmg); }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return action; }
}
