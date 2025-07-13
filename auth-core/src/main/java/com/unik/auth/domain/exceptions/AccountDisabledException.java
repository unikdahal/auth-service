package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when a user's account is disabled and cannot be used for authentication.
 */
public final class AccountDisabledException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AccountDisabledException with the specified detail message.
     * @param message the detail message
     */
    public AccountDisabledException(String message) {
        super(message);
    }

    /**
     * Constructs a new AccountDisabledException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public AccountDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
