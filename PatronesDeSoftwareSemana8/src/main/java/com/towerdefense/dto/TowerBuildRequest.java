package com.towerdefense.dto;

import java.util.List;

/**
 * Request body for POST /api/tower/build.
 * Contains the list of upgrade IDs to apply as decorators.
 */
public class TowerBuildRequest {

    private List<String> upgrades;

    public TowerBuildRequest() {
    }

    public List<String> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(List<String> upgrades) {
        this.upgrades = upgrades;
    }
}
