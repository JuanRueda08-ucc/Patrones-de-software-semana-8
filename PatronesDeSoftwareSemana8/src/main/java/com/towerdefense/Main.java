package com.towerdefense;

import com.towerdefense.routes.ApiRoutes;

import static spark.Spark.*;

/**
 * Entry point for the Tower Defense application.
 * Configures Spark Java, serves static files, and registers API routes.
 */
public class Main {

    public static void main(String[] args) {
        // Configure Spark
        port(8080);

        // Serve static files from the /public directory in resources
        staticFiles.location("/public");

        // Register API routes
        ApiRoutes apiRoutes = new ApiRoutes();
        apiRoutes.registerRoutes();

        System.out.println("========================================");
        System.out.println(" Tower Defense - Decorator Pattern");
        System.out.println(" Server running on http://localhost:8080");
        System.out.println("========================================");
    }
}
