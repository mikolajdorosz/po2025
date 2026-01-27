package org.example.thewitcher.model.battle;
import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.character.Ally;
import org.example.thewitcher.model.entity.character.Bandit;
import org.example.thewitcher.model.entity.character.Character;
import org.example.thewitcher.model.items.WeaponType;

public class CharacterBattleUnit implements IBattleUnit {
    private final Character character;
    private BattleAction action;
    private boolean defending;
    private int damageBoost = 0;
    private int resistance = 0;
    private String statusMessage;

    public CharacterBattleUnit(Character character) { this.character = character; }

    @Override public Entity getEntity() { return character; }
    @Override public int getHealth() { return character.getHealth(); }
    @Override public boolean isAlive() { return character.getHealth() > 0; }
    @Override public int attack() {
        int baseDmg = 0;
        if (character instanceof Bandit) baseDmg = ((Bandit) character).attack();
        else if (character instanceof Ally) baseDmg = ((Ally) character).attack();

        return baseDmg + damageBoost;
    }
    @Override public int attack(IBattleUnit enemy, WeaponType weapon) { return 0; }
    @Override public void takeDamage(int dmg) {
        dmg -= resistance;
        if (dmg < 0) dmg = 0;
        if (defending) dmg /= 3;
        character.setHealth(character.getHealth() - dmg);
    }
    @Override public void setAction(BattleAction action) { this.action = action; }
    @Override public BattleAction getAction() { return action; }
    @Override public String getStatusMessage() { return statusMessage; }
    @Override public void resetStatusMessage() { statusMessage = null; }
    @Override public void setDefending(boolean defending) { this.defending = defending; }
    @Override public boolean isDefending() { return defending; }
    @Override public void applyEffect(String effectName) {
        if ("Thunderbolt".equalsIgnoreCase(effectName)) {
            damageBoost += 5;
            statusMessage = "Thunderbolt! (+5 DMG)";
        } else if ("Resistance Potion".equalsIgnoreCase(effectName)) {
            resistance += 3;
            statusMessage = "Resistance! (-3 DMG Taken)";
        }
    }
}
