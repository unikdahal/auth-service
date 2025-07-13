package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when an email address is invalid or does not meet requirements.
 */
public final class InvalidEmailException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidEmailException with the specified detail message.
     * @param message the detail message
     */
    public InvalidEmailException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidEmailException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
