package com.towerdefense.decorator;

import com.towerdefense.model.Tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Reinforces the tower, adding +3 damage per hit.
 * Pure stat decorator — the +3 is factored into BaseTower.attack()
 * via getDamage() before armor reduction.
 */
public class ShieldDecorator extends TowerDecorator {

    private static final int BONUS_DAMAGE = 3;
    private static final int UPGRADE_COST = 80;

    public ShieldDecorator(Tower wrappedTower) {
        super(wrappedTower);
    }

    @Override
    public String getDescription() {
        return wrappedTower.getDescription() + " + Shield";
    }

    @Override
    public int getDamage() {
        return wrappedTower.getDamage() + BONUS_DAMAGE;
    }

    @Override
    public int getCost() {
        return wrappedTower.getCost() + UPGRADE_COST;
    }

    @Override
    public List<String> getSpecialEffects() {
        List<String> effects = new ArrayList<>(wrappedTower.getSpecialEffects());
        effects.add("Shield");
        return effects;
    }
}
