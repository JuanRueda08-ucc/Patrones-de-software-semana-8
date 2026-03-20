package com.towerdefense.dto;

import java.util.List;

/**
 * Response DTO for battle simulation results.
 * Contains victory status, combat stats, tower stats, and the full battle log.
 */
public class BattleResultResponse {

    private boolean victory;
    private int totalDamageDealt;
    private int enemiesDefeated;
    private int totalEnemies;
    private int goldEarned;
    private int totalTicks;
    private List<String> appliedDecorators;
    private TowerResponse towerStats;
    private List<String> battleLog;

    public BattleResultResponse() {
    }

    // --- Getters and Setters ---

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(int totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public void setEnemiesDefeated(int enemiesDefeated) {
        this.enemiesDefeated = enemiesDefeated;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }

    public void setTotalEnemies(int totalEnemies) {
        this.totalEnemies = totalEnemies;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
    }

    public int getTotalTicks() {
        return totalTicks;
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    public List<String> getAppliedDecorators() {
        return appliedDecorators;
    }

    public void setAppliedDecorators(List<String> appliedDecorators) {
        this.appliedDecorators = appliedDecorators;
    }

    public TowerResponse getTowerStats() {
        return towerStats;
    }

    public void setTowerStats(TowerResponse towerStats) {
        this.towerStats = towerStats;
    }

    public List<String> getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(List<String> battleLog) {
        this.battleLog = battleLog;
    }
}
