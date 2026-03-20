package com.towerdefense.service;

import com.towerdefense.dto.BattleResultResponse;
import com.towerdefense.dto.TowerResponse;
import com.towerdefense.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates combat between a tower and a wave of enemies.
 * Uses a tick-based system where the tower fires at intervals
 * determined by its attack speed.
 */
public class BattleSimulationService {

    private static final int MAX_TICKS = 100;
    private final TowerFactory towerFactory;

    public BattleSimulationService(TowerFactory towerFactory) {
        this.towerFactory = towerFactory;
    }

    /**
     * Simulates a complete wave battle.
     *
     * Time model:
     * - Each tick = 1 discrete time unit
     * - tickInterval = 1.0 / attackSpeed (ticks between attacks)
     * - nextAttackTime accumulator tracks when the tower can fire next
     * - Enemies advance each tick, reducing distanceToTower by their speed
     */
    public BattleResultResponse simulate(Tower tower, Wave wave, List<String> upgradeIds) {
        double tickInterval = 1.0 / tower.getAttackSpeed();
        double nextAttackTime = 0.0;

        List<String> battleLog = new ArrayList<>();
        int totalDamageDealt = 0;
        int enemiesDefeated = 0;
        int goldEarned = 0;
        boolean victory = false;
        int lastTick = 0;

        List<Enemy> enemies = wave.getEnemies();

        battleLog.add("=== Wave " + wave.getWaveNumber() + " begins! ===");
        battleLog.add("Enemies: " + enemies.size()
                + " | Tower: " + tower.getDescription()
                + " | Attack Speed: " + String.format("%.2f", tower.getAttackSpeed())
                + " | Range: " + String.format("%.1f", tower.getRange()));

        for (int tick = 1; tick <= MAX_TICKS; tick++) {
            lastTick = tick;

            // Phase 1: Enemy movement — tick freeze and advance
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    enemy.tickFreeze();
                    enemy.advance();
                }
            }

            // Phase 2: Lose check — has any alive enemy reached the tower?
            Enemy breacher = findBreacher(enemies);
            if (breacher != null) {
                battleLog.add("[Tick " + tick + "] " + breacher.getName()
                        + " reached the tower! DEFEAT!");
                victory = false;
                break;
            }

            // Phase 3: Tower attack phase (may fire multiple times if speed > 1)
            while (nextAttackTime <= tick) {
                Enemy target = findTarget(enemies, tower.getRange());
                if (target == null) {
                    // No target in range — advance clock to prevent burst-on-enter
                    nextAttackTime = tick + tickInterval;
                    break;
                }

                AttackResult result = tower.attack(target);

                // Accumulate stats
                totalDamageDealt += result.getTotalDamage();
                goldEarned += result.getGoldGenerated();

                // Build log entry
                StringBuilder logEntry = new StringBuilder();
                logEntry.append("[Tick ").append(tick).append("] ").append(result.getLogMessage());
                battleLog.add(logEntry.toString());

                if (result.isEnemyKilled()) {
                    enemiesDefeated++;
                    goldEarned += target.getReward();
                    battleLog.add("[Tick " + tick + "] " + target.getName()
                            + " defeated! +" + target.getReward() + " gold reward");
                }

                nextAttackTime += tickInterval;
            }

            // Phase 4: Win check
            if (allEnemiesDead(enemies)) {
                victory = true;
                battleLog.add("=== All enemies defeated! VICTORY! ===");
                break;
            }
        }

        // If we ran out of ticks without winning
        if (lastTick >= MAX_TICKS && !victory) {
            battleLog.add("=== Time limit reached! " + enemiesDefeated + "/"
                    + wave.getTotalEnemies() + " enemies defeated ===");
        }

        // Build result
        TowerResponse towerStats = towerFactory.toResponse(tower, upgradeIds);

        BattleResultResponse response = new BattleResultResponse();
        response.setVictory(victory);
        response.setTotalDamageDealt(totalDamageDealt);
        response.setEnemiesDefeated(enemiesDefeated);
        response.setTotalEnemies(wave.getTotalEnemies());
        response.setGoldEarned(goldEarned);
        response.setTotalTicks(lastTick);
        response.setAppliedDecorators(upgradeIds);
        response.setTowerStats(towerStats);
        response.setBattleLog(battleLog);

        return response;
    }

    /**
     * Finds the first alive enemy that has reached the tower (lose condition).
     */
    private Enemy findBreacher(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.hasReachedTower()) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Finds the first alive enemy within the tower's range (closest first).
     */
    private Enemy findTarget(List<Enemy> enemies, double towerRange) {
        Enemy closest = null;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.isInRange(towerRange)) {
                if (closest == null || enemy.getDistanceToTower() < closest.getDistanceToTower()) {
                    closest = enemy;
                }
            }
        }
        return closest;
    }

    /**
     * Checks if all enemies in the wave are dead.
     */
    private boolean allEnemiesDead(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                return false;
            }
        }
        return true;
    }
}
