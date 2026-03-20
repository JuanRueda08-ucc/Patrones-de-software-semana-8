package com.towerdefense.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Component in the Decorator pattern.
 * Provides base stats and is the single point of armor reduction.
 */
public class BaseTower implements Tower {

    private static final int BASE_DAMAGE = 15;
    private static final double BASE_RANGE = 8.0;
    private static final double BASE_ATTACK_SPEED = 1.0;
    private static final int BASE_COST = 100;

    @Override
    public String getDescription() {
        return "Base Tower";
    }

    @Override
    public int getDamage() {
        return BASE_DAMAGE;
    }

    @Override
    public double getRange() {
        return BASE_RANGE;
    }

    @Override
    public double getAttackSpeed() {
        return BASE_ATTACK_SPEED;
    }

    @Override
    public int getCost() {
        return BASE_COST;
    }

    @Override
    public List<String> getSpecialEffects() {
        return new ArrayList<>();
    }

    /**
     * Executes one attack. This is the SINGLE POINT of armor reduction.
     * Computes effectiveDamage = max(0, getDamage() - enemy.armor),
     * applies raw damage to the enemy, and creates the initial AttackResult.
     */
    @Override
    public AttackResult attack(Enemy enemy) {
        int effectiveDamage = Math.max(0, getDamage() - enemy.getArmor());
        enemy.takeDamage(effectiveDamage);

        AttackResult result = new AttackResult();
        result.setDamageDealt(effectiveDamage);
        result.setEnemyKilled(!enemy.isAlive());
        result.setLogMessage("Base Tower hits " + enemy.getName()
                + " for " + effectiveDamage + " damage"
                + (enemy.isAlive() ? " (" + enemy.getHealth() + " HP left)" : " — KILLED!"));

        return result;
    }
}
