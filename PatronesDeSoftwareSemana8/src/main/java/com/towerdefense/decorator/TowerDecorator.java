package com.towerdefense.decorator;

import com.towerdefense.model.AttackResult;
import com.towerdefense.model.Enemy;
import com.towerdefense.model.Tower;

import java.util.List;

/**
 * Abstract Decorator in the Decorator pattern.
 * Wraps a Tower instance and delegates all methods by default.
 * Concrete decorators override ONLY the methods they enhance.
 */
public abstract class TowerDecorator implements Tower {

    protected final Tower wrappedTower;

    public TowerDecorator(Tower wrappedTower) {
        this.wrappedTower = wrappedTower;
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription();
    }

    @Override
    public int getDamage() {
        return wrappedTower.getDamage();
    }

    @Override
    public double getRange() {
        return wrappedTower.getRange();
    }

    @Override
    public double getAttackSpeed() {
        return wrappedTower.getAttackSpeed();
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost();
    }

    @Override
    public List<String> getSpecialEffects() {
        return wrappedTower.getSpecialEffects();
    }

    /**
     * Default attack implementation for decorators.
     * Computes effectiveDamage using this.getDamage() which resolves through
     * the FULL decorator chain (not just BaseTower's getDamage()).
     * This ensures stat-modifying decorators like ShieldDecorator correctly
     * affect the damage dealt in combat.
     */
    @Override
    public AttackResult attack(Enemy enemy) {
        // Compute damage at the outermost decorator level
        int effectiveDamage = Math.max(0, this.getDamage() - enemy.getArmor());
        enemy.takeDamage(effectiveDamage);

        AttackResult result = new AttackResult();
        result.setDamageDealt(effectiveDamage);
        result.setEnemyKilled(!enemy.isAlive());
        result.setLogMessage(this.getDescription() + " hits " + enemy.getName()
                + " for " + effectiveDamage + " damage"
                + (enemy.isAlive() ? " (" + enemy.getHealth() + " HP left)" : " — KILLED!"));

        return result;
    }
}
