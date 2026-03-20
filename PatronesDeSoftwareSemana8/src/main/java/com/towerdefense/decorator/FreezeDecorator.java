package com.towerdefense.decorator;

import com.towerdefense.model.AttackResult;
import com.towerdefense.model.Enemy;
import com.towerdefense.model.Tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies a freeze effect after each attack, slowing the enemy by 50% for 2 ticks.
 * Delegates attack() first, then applies freeze and enriches the AttackResult.
 */
public class FreezeDecorator extends TowerDecorator {

    private static final int FREEZE_DURATION = 2;
    private static final int UPGRADE_COST = 100;

    public FreezeDecorator(Tower wrappedTower) {
        super(wrappedTower);
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription() + " + Freeze";
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost() + UPGRADE_COST;
    }

    @Override
    public List<String> getSpecialEffects() {
        List<String> effects = new ArrayList<>(wrappedTower.getSpecialEffects());
        effects.add("Freeze");
        return effects;
    }

    @Override
    public AttackResult attack(Enemy enemy) {
        // Delegate to super (TowerDecorator) for correct damage computation
        AttackResult result = super.attack(enemy);

        // Apply freeze effect if the enemy is still alive
        if (enemy.isAlive()) {
            enemy.applyFreeze(FREEZE_DURATION);
            result.setFroze(true);
            result.appendLog("FREEZE applied for " + FREEZE_DURATION + " ticks");
        }

        return result;
    }
}
