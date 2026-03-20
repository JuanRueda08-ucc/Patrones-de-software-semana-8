package com.towerdefense.service;

import com.towerdefense.decorator.*;
import com.towerdefense.dto.TowerResponse;
import com.towerdefense.dto.UpgradeInfo;
import com.towerdefense.model.BaseTower;
import com.towerdefense.model.Tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized factory for constructing decorated towers from upgrade IDs.
 * Each upgrade ID maps to a concrete decorator that wraps the previous tower.
 */
public class TowerFactory {

    /**
     * Builds a tower by stacking decorators in order.
     * Each decorator wraps the tower from the previous iteration.
     */
    public Tower buildTower(List<String> upgradeIds) {
        Tower tower = new BaseTower();
        for (String id : upgradeIds) {
            tower = applyDecorator(id, tower);
        }
        return tower;
    }

    /**
     * Maps a string ID to its corresponding decorator constructor.
     */
    private Tower applyDecorator(String id, Tower tower) {
        return switch (id) {
            case "rapid_fire"      -> new RapidFireDecorator(tower);
            case "freeze"          -> new FreezeDecorator(tower);
            case "shield"          -> new ShieldDecorator(tower);
            case "gold_generator"  -> new GoldGeneratorDecorator(tower);
            case "range_boost"     -> new RangeBoostDecorator(tower);
            case "critical_damage" -> new CriticalDamageDecorator(tower);
            default -> throw new IllegalArgumentException("Unknown upgrade: " + id);
        };
    }

    /**
     * Converts a Tower into a TowerResponse DTO.
     */
    public TowerResponse toResponse(Tower tower, List<String> upgradeIds) {
        return new TowerResponse(
                tower.getDescription(),
                tower.getDamage(),
                tower.getRange(),
                tower.getAttackSpeed(),
                tower.getCost(),
                tower.getSpecialEffects(),
                upgradeIds != null ? upgradeIds : new ArrayList<>()
        );
    }

    /**
     * Returns metadata for all available upgrades.
     */
    public List<UpgradeInfo> getAvailableUpgrades() {
        List<UpgradeInfo> upgrades = new ArrayList<>();
        upgrades.add(new UpgradeInfo("rapid_fire", "Rapid Fire",
                "Increases attack speed by 50%", 75, "offense"));
        upgrades.add(new UpgradeInfo("freeze", "Freeze",
                "Slows enemies by 50% for 2 ticks after each attack", 100, "control"));
        upgrades.add(new UpgradeInfo("shield", "Shield",
                "Reinforces the tower, adding +3 damage per hit", 80, "defense"));
        upgrades.add(new UpgradeInfo("gold_generator", "Gold Generator",
                "Generates +5 gold per attack, +50% enemy reward on kill", 90, "utility"));
        upgrades.add(new UpgradeInfo("range_boost", "Range Boost",
                "Increases attack range by 40%", 60, "utility"));
        upgrades.add(new UpgradeInfo("critical_damage", "Critical Damage",
                "25% chance for critical hit dealing double damage", 110, "offense"));
        return upgrades;
    }
}
