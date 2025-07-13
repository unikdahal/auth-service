package com.unik.auth.domain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Base interface for all user entities in the authentication system.
 * Provides common properties and methods that all user types should implement.
 */
public interface BaseUser<R> extends Serializable {

    /**
     * Gets the unique identifier for this user.
     * @return The user's unique identifier
     */
    Serializable getId();

    /**
     * Gets the user's email address.
     * @return The user's email address
     */
    String getEmail();

    /**
     * Gets the user's username.
     * @return The user's username
     */
    String getUsername();

    /**
     * Gets the user's encoded password.
     * @return The user's encoded password
     */
    String getPassword();

    /**
     * Gets the roles assigned to this user.
     * @return Set of roles assigned to this user
     */
    Set<R> getRoles();

    /**
     * Checks if the user account is enabled.
     * @return true if the account is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Checks if the user account is locked.
     * @return true if the account is locked, false otherwise
     */
    boolean isLocked();

    /**
     * Gets the timestamp when the user was created.
     * @return The creation timestamp
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the timestamp when the user was last updated.
     * @return The last update timestamp
     */
    LocalDateTime getUpdatedAt();

    /**
     * Validates if the user account is in a valid state.
     * @return true if the user is valid, false otherwise
     */
    boolean isValid();

    /**
     * Checks if the user account is expired.
     */
    boolean isAccountExpired();

    /**
     * Checks if the user credentials are expired.
     */
    boolean isCredentialsExpired();

    /**
     * Gets additional user attributes as a map.
     */
    java.util.Map<String, Object> getAttributes();

    /**
     * Gets the user's display name.
     */
    String getDisplayName();
}
