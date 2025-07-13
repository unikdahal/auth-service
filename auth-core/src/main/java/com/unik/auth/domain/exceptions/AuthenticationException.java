package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Base exception for authentication-related errors.
 */
public class AuthenticationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AuthenticationException with the specified detail message.
     * @param message the detail message
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthenticationException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
