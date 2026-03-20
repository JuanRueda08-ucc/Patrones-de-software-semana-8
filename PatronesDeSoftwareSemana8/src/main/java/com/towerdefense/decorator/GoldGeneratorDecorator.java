package com.towerdefense.decorator;

import com.towerdefense.model.AttackResult;
import com.towerdefense.model.Enemy;
import com.towerdefense.model.Tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates bonus gold on each attack.
 * Delegates attack() first, then adds gold to the result.
 * If the attack killed the enemy, adds an extra 50% of the enemy's reward.
 */
public class GoldGeneratorDecorator extends TowerDecorator {

    private static final int GOLD_PER_ATTACK = 5;
    private static final double KILL_BONUS_RATIO = 0.5;
    private static final int UPGRADE_COST = 90;

    public GoldGeneratorDecorator(Tower wrappedTower) {
        super(wrappedTower);
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription() + " + Gold Generator";
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost() + UPGRADE_COST;
    }

    @Override
    public List<String> getSpecialEffects() {
        List<String> effects = new ArrayList<>(wrappedTower.getSpecialEffects());
        effects.add("Gold Gen");
        return effects;
    }

    @Override
    public AttackResult attack(Enemy enemy) {
        // Delegate to super (TowerDecorator) for correct damage computation
        AttackResult result = super.attack(enemy);

        // Add base gold generation
        int gold = GOLD_PER_ATTACK;

        // Bonus gold if the enemy was killed
        if (result.isEnemyKilled()) {
            gold += (int) (enemy.getReward() * KILL_BONUS_RATIO);
        }

        result.setGoldGenerated(result.getGoldGenerated() + gold);
        result.appendLog("+" + gold + " gold generated");

        return result;
    }
}
