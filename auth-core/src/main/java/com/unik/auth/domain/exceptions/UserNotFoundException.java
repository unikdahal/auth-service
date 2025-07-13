package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when a user is not found during authentication or lookup.
 */
public final class UserNotFoundException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserNotFoundException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
