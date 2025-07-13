package com.unik.auth.ports.output;

import java.util.Optional;

/**
 * Generic service for password encoding, verification, and strength checking operations.
 * Supports pluggable encoding algorithms and extensible password policies.
 */
public interface PasswordServicePort {
    /**
     * Encodes a raw password using the configured encoding algorithm.
     * @param rawPassword the raw password
     * @return the encoded password
     */
    String encode(String rawPassword);

    /**
     * Verifies that a raw password matches the encoded password.
     * @param rawPassword the raw password
     * @param encodedPassword the encoded password
     * @return true if the passwords match, false otherwise
     */
    boolean matches(String rawPassword, String encodedPassword);

    /**
     * Generates a random password of the specified length using a secure algorithm.
     * @param length the desired password length
     * @return a random password string
     */
    String generateRandomPassword(int length);

    /**
     * Checks if the password meets strength requirements (length, complexity, etc.).
     * @param password the password to check
     * @return true if the password is strong, false otherwise
     */
    boolean isPasswordStrong(String password);

    /**
     * Checks if the password is found in known data breaches or compromised lists.
     * @param password the password to check
     * @return true if the password is compromised, false otherwise
     */
    boolean isPasswordCompromised(String password);

    /**
     * Optionally returns the reason a password is considered weak or invalid.
     * @param password the password to check
     * @return an Optional containing the reason if the password is weak, or empty if strong
     */
    default Optional<String> getWeaknessReason(String password) {
        return Optional.empty();
    }
}
