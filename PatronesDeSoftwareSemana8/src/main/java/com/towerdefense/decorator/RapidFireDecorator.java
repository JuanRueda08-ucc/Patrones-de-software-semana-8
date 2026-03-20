package com.towerdefense.decorator;

import com.towerdefense.model.Tower;

/**
 * Increases attack speed by 50%.
 * Pure stat decorator — attack() is delegated unchanged.
 * The speed boost is resolved at the simulation level via tickInterval.
 */
public class RapidFireDecorator extends TowerDecorator {

    private static final double SPEED_MULTIPLIER = 1.5;
    private static final int UPGRADE_COST = 75;

    public RapidFireDecorator(Tower wrappedTower) {
        super(wrappedTower);
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription() + " + Rapid Fire";
    }

    @Override
    public double getAttackSpeed() {
        return wrappedTower.getAttackSpeed() * SPEED_MULTIPLIER;
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost() + UPGRADE_COST;
    }
}
