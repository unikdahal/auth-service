package com.unik.auth.ports.output;

import com.unik.auth.domain.entities.BaseUser;

import java.util.Optional;

/**
 * Generic authentication strategy port for supporting multiple authentication mechanisms.
 *
 * @param <U> the user type
 * @param <R> the role type
 * @param <C> the credentials type
 */
public interface AuthenticationStrategyPort<U extends BaseUser<R>, R, C> {
    /**
     * Attempts to authenticate a user using the provided credentials.
     * @param credentials the credentials to authenticate
     * @return an Optional containing the authenticated user if successful, or empty if authentication fails
     */
    Optional<U> authenticate(C credentials);

    /**
     * Checks if this strategy supports the given credentials type.
     * @param credentialsType the credentials class
     * @return true if supported, false otherwise
     */
    boolean supports(Class<? extends C> credentialsType);

    /**
     * Returns the unique name of this authentication strategy (e.g., "password", "otp").
     * @return the strategy name
     */
    String getStrategyName();

    /**
     * Returns whether this strategy is enabled (e.g., for feature toggling).
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Validates the format of the provided credentials (e.g., structure, required fields).
     * @param credentials the credentials to validate
     * @return true if the format is valid, false otherwise
     */
    boolean validateCredentialsFormat(C credentials);

    /**
     * Optionally prepares or normalizes credentials before authentication (e.g., trim, lowercase).
     * @param credentials the credentials to prepare
     * @return the prepared credentials
     */
    C prepareCredentials(C credentials);
}
