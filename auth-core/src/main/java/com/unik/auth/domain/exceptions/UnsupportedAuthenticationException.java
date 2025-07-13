package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when an unsupported authentication method is used.
 */
public final class UnsupportedAuthenticationException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UnsupportedAuthenticationException with the specified detail message.
     * @param message the detail message
     */
    public UnsupportedAuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnsupportedAuthenticationException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public UnsupportedAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
