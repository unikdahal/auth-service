package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when a user's credentials have expired and must be updated.
 */
public final class CredentialsExpiredException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new CredentialsExpiredException with the specified detail message.
     * @param message the detail message
     */
    public CredentialsExpiredException(String message) {
        super(message);
    }

    /**
     * Constructs a new CredentialsExpiredException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public CredentialsExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
