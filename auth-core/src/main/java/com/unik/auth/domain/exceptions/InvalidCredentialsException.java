package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when provided credentials are invalid during authentication.
 */
public final class InvalidCredentialsException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidCredentialsException with the specified detail message.
     * @param message the detail message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidCredentialsException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
