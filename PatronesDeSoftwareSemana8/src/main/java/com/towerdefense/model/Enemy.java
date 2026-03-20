package com.towerdefense.model;

/**
 * Represents an enemy unit that advances toward the tower.
 * Enemies have health, armor, speed, and can be frozen.
 *
 * Position model: enemies spawn at distanceToTower = 10.0 and
 * advance each tick. The tower can only attack enemies in range.
 */
public class Enemy {

    private String name;
    private int health;
    private int maxHealth;
    private double speed;
    private double baseSpeed;
    private int armor;
    private int reward;
    private double distanceToTower;
    private boolean frozen;
    private int frozenTurnsRemaining;

    public Enemy(String name, int health, double speed, int armor, int reward) {
        this(name, health, speed, armor, reward, 20.0);
    }

    public Enemy(String name, int health, double speed, int armor, int reward, double distanceToTower) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.baseSpeed = speed;
        this.armor = armor;
        this.reward = reward;
        this.distanceToTower = distanceToTower;
        this.frozen = false;
        this.frozenTurnsRemaining = 0;
    }

    /**
     * Raw HP reduction — no armor logic here.
     * Armor is applied once in BaseTower.attack().
     */
    public void takeDamage(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    /**
     * Applies freeze: reduces speed to 50% and sets the freeze duration.
     */
    public void applyFreeze(int turns) {
        this.frozen = true;
        this.frozenTurnsRemaining = turns;
        this.speed = this.baseSpeed * 0.5;
    }

    /**
     * Decrements freeze timer. Restores speed when freeze expires.
     */
    public void tickFreeze() {
        if (frozen) {
            frozenTurnsRemaining--;
            if (frozenTurnsRemaining <= 0) {
                frozen = false;
                frozenTurnsRemaining = 0;
                speed = baseSpeed;
            }
        }
    }

    /**
     * Advances the enemy toward the tower by its current speed.
     */
    public void advance() {
        this.distanceToTower = Math.max(0, this.distanceToTower - this.speed);
    }

    /**
     * Checks if this enemy is within the tower's attack range.
     */
    public boolean isInRange(double towerRange) {
        return this.distanceToTower <= towerRange;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    /**
     * Returns true if the enemy has reached the tower (lose condition).
     */
    public boolean hasReachedTower() {
        return this.distanceToTower <= 0;
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getSpeed() {
        return speed;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public int getArmor() {
        return armor;
    }

    public int getReward() {
        return reward;
    }

    public double getDistanceToTower() {
        return distanceToTower;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public int getFrozenTurnsRemaining() {
        return frozenTurnsRemaining;
    }
}
