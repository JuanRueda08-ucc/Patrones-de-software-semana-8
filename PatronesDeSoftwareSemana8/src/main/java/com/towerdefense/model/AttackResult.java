package com.towerdefense.model;

/**
 * Mutable result object created by BaseTower.attack() and enriched
 * as it propagates back up the decorator chain.
 *
 * Ownership contract:
 * - BaseTower sets: damageDealt, enemyKilled, logMessage
 * - CriticalDamageDecorator sets: bonusDamage, wasCritical, updates enemyKilled
 * - FreezeDecorator sets: froze
 * - GoldGeneratorDecorator sets: goldGenerated
 * - All layers may append to logMessage
 */
public class AttackResult {

    private int damageDealt;
    private int bonusDamage;
    private boolean wasCritical;
    private boolean froze;
    private int goldGenerated;
    private boolean enemyKilled;
    private String logMessage;

    public AttackResult() {
        this.damageDealt = 0;
        this.bonusDamage = 0;
        this.wasCritical = false;
        this.froze = false;
        this.goldGenerated = 0;
        this.enemyKilled = false;
        this.logMessage = "";
    }

    // --- Getters and Setters ---

    public int getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public int getBonusDamage() {
        return bonusDamage;
    }

    public void setBonusDamage(int bonusDamage) {
        this.bonusDamage = bonusDamage;
    }

    public boolean isWasCritical() {
        return wasCritical;
    }

    public void setWasCritical(boolean wasCritical) {
        this.wasCritical = wasCritical;
    }

    public boolean isFroze() {
        return froze;
    }

    public void setFroze(boolean froze) {
        this.froze = froze;
    }

    public int getGoldGenerated() {
        return goldGenerated;
    }

    public void setGoldGenerated(int goldGenerated) {
        this.goldGenerated = goldGenerated;
    }

    public boolean isEnemyKilled() {
        return enemyKilled;
    }

    public void setEnemyKilled(boolean enemyKilled) {
        this.enemyKilled = enemyKilled;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    /**
     * Appends additional info to the existing log message.
     */
    public void appendLog(String extra) {
        if (this.logMessage.isEmpty()) {
            this.logMessage = extra;
        } else {
            this.logMessage += " | " + extra;
        }
    }

    /**
     * Returns total damage dealt including bonus damage from crits.
     */
    public int getTotalDamage() {
        return damageDealt + bonusDamage;
    }
}
