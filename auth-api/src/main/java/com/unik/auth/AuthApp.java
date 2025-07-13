package com.unik.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main class for the authentication service application.
 * This is the entry point for the Spring Boot application.
 *
 * Note: Explicit package scanning annotations are not necessary as Spring Boot
 * will automatically scan the com.unik.auth package and its subpackages by default.
 */
@SpringBootApplication
@EnableTransactionManagement
public class AuthApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }
}
