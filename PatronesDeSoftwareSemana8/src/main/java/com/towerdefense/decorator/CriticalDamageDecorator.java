package com.towerdefense.decorator;

import com.towerdefense.model.AttackResult;
import com.towerdefense.model.Enemy;
import com.towerdefense.model.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Adds a 25% chance for critical hits that deal bonus damage.
 * Delegates attack() first, then rolls for crit.
 * On success, applies bonusDamage equal to damageDealt (effectively doubling).
 * The bonus damage is raw (bypasses armor) — intentional piercing strike design.
 */
public class CriticalDamageDecorator extends TowerDecorator {

    private static final double CRIT_CHANCE = 0.25;
    private static final int UPGRADE_COST = 110;
    private final Random random;

    public CriticalDamageDecorator(Tower wrappedTower) {
        super(wrappedTower);
        this.random = new Random();
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription() + " + Critical Damage";
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost() + UPGRADE_COST;
    }

    @Override
    public List<String> getSpecialEffects() {
        List<String> effects = new ArrayList<>(wrappedTower.getSpecialEffects());
        effects.add("Critical");
        return effects;
    }

    @Override
    public AttackResult attack(Enemy enemy) {
        // Delegate to super (TowerDecorator) for correct damage computation
        AttackResult result = super.attack(enemy);

        // Roll for critical hit
        if (random.nextDouble() < CRIT_CHANCE && enemy.isAlive()) {
            int bonusDamage = result.getDamageDealt();
            enemy.takeDamage(bonusDamage); // Raw damage — bypasses armor
            result.setBonusDamage(result.getBonusDamage() + bonusDamage);
            result.setWasCritical(true);
            result.setEnemyKilled(!enemy.isAlive());
            result.appendLog("CRITICAL HIT! +" + bonusDamage + " bonus damage"
                    + (!enemy.isAlive() ? " — KILLED!" : ""));
        }

        return result;
    }
}
