package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when a password is invalid or does not meet security requirements.
 */
public final class InvalidPasswordException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidPasswordException with the specified detail message.
     * @param message the detail message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPasswordException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
