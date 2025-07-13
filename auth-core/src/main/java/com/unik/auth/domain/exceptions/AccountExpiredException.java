package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when a user's account has expired and cannot be used for authentication.
 */
public final class AccountExpiredException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AccountExpiredException with the specified detail message.
     * @param message the detail message
     */
    public AccountExpiredException(String message) {
        super(message);
    }

    /**
     * Constructs a new AccountExpiredException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public AccountExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
