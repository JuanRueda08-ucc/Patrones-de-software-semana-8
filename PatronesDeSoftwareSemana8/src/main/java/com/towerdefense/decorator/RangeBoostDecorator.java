package com.towerdefense.decorator;

import com.towerdefense.model.Tower;

/**
 * Increases tower range by 40%.
 * Pure stat decorator — attack() is delegated unchanged.
 * Extended range is resolved at the simulation level via isInRange().
 */
public class RangeBoostDecorator extends TowerDecorator {

    private static final double RANGE_MULTIPLIER = 1.4;
    private static final int UPGRADE_COST = 60;

    public RangeBoostDecorator(Tower wrappedTower) {
        super(wrappedTower);
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription() + " + Range Boost";
    }

    @Override
    public double getRange() {
        return wrappedTower.getRange() * RANGE_MULTIPLIER;
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost() + UPGRADE_COST;
    }
}
