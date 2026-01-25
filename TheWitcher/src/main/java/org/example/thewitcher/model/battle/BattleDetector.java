package org.example.thewitcher.model.battle;

import org.example.thewitcher.model.entity.player.Player;

import java.util.List;

public class BattleDetector {
    public static List<IBattleUnit> gatherEnemies(Player player, List<IBattleUnit> allEnemies, int radius) {
        return allEnemies.stream().filter(enemy -> searchForEnemy(player, enemy, radius)).toList();
    }
    public static boolean searchForEnemy(Player player, IBattleUnit enemy, int radius) {
        int dx = Math.abs(player.getX() - enemy.getEntity().getX());
        int dy = Math.abs(player.getY() - enemy.getEntity().getY());
        return dx <= radius && dy <= radius;
    }
}
