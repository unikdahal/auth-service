package com.unik.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application entry point for the Authentication Service.
 * 
 * This service is designed to be generic and configurable for use across multiple websites
 * and applications. Key configuration properties include:
 * 
 * - api.auth.base-path: Base path for authentication endpoints (default: /api/auth)
 * - api.token.base-path: Base path for token management endpoints (default: /api/token)
 * - api.auth.token-type: Token type for authentication (default: Bearer)
 * - api.docs.title: API documentation title (default: Authentication Service API)
 * - api.docs.description: API documentation description
 * - api.docs.version: API version (default: 1.0.0)
 * - api.docs.group: API documentation group name (default: auth)
 * 
 * See application.properties/yml for additional configuration options.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.unik.auth"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
