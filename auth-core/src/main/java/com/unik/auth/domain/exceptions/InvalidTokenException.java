package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when an authentication token is invalid or malformed.
 */
public final class InvalidTokenException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidTokenException with the specified detail message.
     * @param message the detail message
     */
    public InvalidTokenException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidTokenException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
