package com.towerdefense.dto;

import java.util.List;

/**
 * Request body for POST /api/wave/simulate.
 * Contains upgrades for building the tower and the wave number to simulate.
 */
public class WaveSimulationRequest {

    private List<String> upgrades;
    private int waveNumber;

    public WaveSimulationRequest() {
    }

    public List<String> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(List<String> upgrades) {
        this.upgrades = upgrades;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }
}
