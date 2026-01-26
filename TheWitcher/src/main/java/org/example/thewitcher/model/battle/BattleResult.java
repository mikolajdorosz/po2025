package org.example.thewitcher.model.battle;

import java.util.List;

public record BattleResult(
        List<IBattleUnit> defeatedAllies,
        List<IBattleUnit> defeatedEnemies
) {}
