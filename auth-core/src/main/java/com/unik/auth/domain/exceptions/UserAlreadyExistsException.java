package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when attempting to register a user that already exists.
 */
public final class UserAlreadyExistsException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     * @param message the detail message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
