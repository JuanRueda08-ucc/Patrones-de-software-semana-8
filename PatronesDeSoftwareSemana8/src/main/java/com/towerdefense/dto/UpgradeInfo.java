package com.towerdefense.dto;

/**
 * DTO for GET /api/upgrades response.
 * Contains metadata about each available decorator upgrade.
 */
public class UpgradeInfo {

    private String id;
    private String name;
    private String description;
    private int cost;
    private String category;

    public UpgradeInfo(String id, String name, String description, int cost, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.category = category;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }
}
