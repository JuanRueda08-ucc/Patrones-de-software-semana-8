package com.towerdefense.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wave of enemies with a wave number and scaling difficulty.
 * Uses a static factory method to generate predefined compositions.
 */
public class Wave {

    private int waveNumber;
    private List<Enemy> enemies;

    public Wave(int waveNumber, List<Enemy> enemies) {
        this.waveNumber = waveNumber;
        this.enemies = enemies;
    }

    /**
     * Generates a wave with predefined enemy compositions.
     * Waves 1-3 have fixed layouts; wave 4+ scales with a 1.3x HP multiplier.
     */
    public static Wave generate(int waveNumber) {
        List<Enemy> enemies = new ArrayList<>();

        switch (waveNumber) {
            case 1:
                for (int i = 0; i < 5; i++) {
                    enemies.add(new Enemy("Goblin", 30, 1.2, 0, 10));
                }
                break;

            case 2:
                for (int i = 0; i < 3; i++) {
                    enemies.add(new Enemy("Goblin", 30, 1.2, 0, 10));
                }
                for (int i = 0; i < 3; i++) {
                    enemies.add(new Enemy("Orc Warrior", 60, 1.5, 3, 20));
                }
                break;

            case 3:
                for (int i = 0; i < 2; i++) {
                    enemies.add(new Enemy("Orc Warrior", 60, 1.5, 3, 20));
                }
                for (int i = 0; i < 2; i++) {
                    enemies.add(new Enemy("Dark Mage", 40, 1.8, 1, 25));
                }
                enemies.add(new Enemy("Troll", 120, 1.0, 5, 50));
                break;

            default:
                // Wave 4+: scale based on wave 3 composition with 1.3^(wave-3) HP multiplier
                double scaleFactor = Math.pow(1.3, waveNumber - 3);
                int extraEnemies = waveNumber - 3;

                for (int i = 0; i < 2 + extraEnemies; i++) {
                    enemies.add(new Enemy("Orc Warrior",
                            (int) (60 * scaleFactor), 1.5, 3 + (waveNumber - 3), 20 + waveNumber));
                }
                for (int i = 0; i < 2 + extraEnemies; i++) {
                    enemies.add(new Enemy("Dark Mage",
                            (int) (40 * scaleFactor), 1.8, 1, 25 + waveNumber));
                }
                for (int i = 0; i < 1 + (waveNumber - 3) / 2; i++) {
                    enemies.add(new Enemy("Troll",
                            (int) (120 * scaleFactor), 1.0, 5 + (waveNumber - 3), 50 + waveNumber * 2));
                }
                break;
        }

        return new Wave(waveNumber, enemies);
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public int getTotalEnemies() {
        return enemies.size();
    }
}
