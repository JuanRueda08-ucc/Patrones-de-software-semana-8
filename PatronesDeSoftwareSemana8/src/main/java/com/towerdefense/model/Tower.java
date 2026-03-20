package com.towerdefense.model;

import java.util.List;

/**
 * Core interface for the Decorator pattern.
 * Both BaseTower and TowerDecorator implement this interface,
 * allowing decorators to wrap any Tower transparently.
 */
public interface Tower {

    String getDescription();

    int getDamage();

    double getRange();

    double getAttackSpeed();

    int getCost();

    List<String> getSpecialEffects();

    AttackResult attack(Enemy enemy);
}
