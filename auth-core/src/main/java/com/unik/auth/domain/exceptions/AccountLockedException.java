package com.unik.auth.domain.exceptions;

import java.io.Serial;

/**
 * Thrown when a user's account is locked due to security policies.
 */
public final class AccountLockedException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AccountLockedException with the specified detail message.
     * @param message the detail message
     */
    public AccountLockedException(String message) {
        super(message);
    }

    /**
     * Constructs a new AccountLockedException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
