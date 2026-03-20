package com.towerdefense.dto;

import java.util.List;

/**
 * Response DTO for tower stats.
 * Used by GET /api/tower/base and POST /api/tower/build.
 */
public class TowerResponse {

    private String description;
    private int damage;
    private double range;
    private double attackSpeed;
    private int cost;
    private List<String> specialEffects;
    private List<String> appliedDecorators;

    public TowerResponse() {
    }

    public TowerResponse(String description, int damage, double range, double attackSpeed,
                          int cost, List<String> specialEffects, List<String> appliedDecorators) {
        this.description = description;
        this.damage = damage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.cost = cost;
        this.specialEffects = specialEffects;
        this.appliedDecorators = appliedDecorators;
    }

    // --- Getters and Setters ---

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<String> getSpecialEffects() {
        return specialEffects;
    }

    public void setSpecialEffects(List<String> specialEffects) {
        this.specialEffects = specialEffects;
    }

    public List<String> getAppliedDecorators() {
        return appliedDecorators;
    }

    public void setAppliedDecorators(List<String> appliedDecorators) {
        this.appliedDecorators = appliedDecorators;
    }
}
