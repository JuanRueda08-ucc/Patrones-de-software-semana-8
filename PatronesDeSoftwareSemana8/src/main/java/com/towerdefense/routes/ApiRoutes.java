package com.towerdefense.routes;

import com.google.gson.Gson;
import com.towerdefense.dto.*;
import com.towerdefense.model.Tower;
import com.towerdefense.model.Wave;
import com.towerdefense.service.BattleSimulationService;
import com.towerdefense.service.TowerFactory;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

/**
 * Registers all API routes for the tower defense application.
 * Uses Spark Java for lightweight HTTP routing.
 */
public class ApiRoutes {

    private final TowerFactory towerFactory;
    private final BattleSimulationService battleService;
    private final Gson gson;

    public ApiRoutes() {
        this.towerFactory = new TowerFactory();
        this.battleService = new BattleSimulationService(towerFactory);
        this.gson = new Gson();
    }

    /**
     * Registers all API endpoints.
     */
    public void registerRoutes() {

        // CORS headers for all responses
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type");
        });

        // Handle preflight CORS requests
        options("/*", (req, res) -> {
            res.status(200);
            return "";
        });

        // Health check
        get("/api/health", (req, res) -> {
            res.type("application/json");
            return "{\"status\":\"ok\"}";
        });

        // List all available upgrades
        get("/api/upgrades", (req, res) -> {
            res.type("application/json");
            List<UpgradeInfo> upgrades = towerFactory.getAvailableUpgrades();
            return gson.toJson(upgrades);
        });

        // Get base tower stats (no decorators)
        get("/api/tower/base", (req, res) -> {
            res.type("application/json");
            Tower baseTower = towerFactory.buildTower(new ArrayList<>());
            TowerResponse response = towerFactory.toResponse(baseTower, new ArrayList<>());
            return gson.toJson(response);
        });

        // Build tower with decorators
        post("/api/tower/build", (req, res) -> {
            res.type("application/json");
            try {
                TowerBuildRequest request = gson.fromJson(req.body(), TowerBuildRequest.class);

                if (request == null || request.getUpgrades() == null) {
                    res.status(400);
                    return "{\"error\":\"Missing 'upgrades' field in request body\"}";
                }

                Tower tower = towerFactory.buildTower(request.getUpgrades());
                TowerResponse response = towerFactory.toResponse(tower, request.getUpgrades());
                return gson.toJson(response);

            } catch (IllegalArgumentException e) {
                res.status(400);
                return "{\"error\":\"" + e.getMessage() + "\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
            }
        });

        // Simulate a wave of enemies
        post("/api/wave/simulate", (req, res) -> {
            res.type("application/json");
            try {
                WaveSimulationRequest request = gson.fromJson(req.body(), WaveSimulationRequest.class);

                if (request == null || request.getUpgrades() == null) {
                    res.status(400);
                    return "{\"error\":\"Missing 'upgrades' field in request body\"}";
                }

                if (request.getWaveNumber() < 1) {
                    res.status(400);
                    return "{\"error\":\"Wave number must be at least 1\"}";
                }

                Tower tower = towerFactory.buildTower(request.getUpgrades());
                Wave wave = Wave.generate(request.getWaveNumber());
                BattleResultResponse response = battleService.simulate(tower, wave, request.getUpgrades());
                return gson.toJson(response);

            } catch (IllegalArgumentException e) {
                res.status(400);
                return "{\"error\":\"" + e.getMessage() + "\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
            }
        });
    }
}
