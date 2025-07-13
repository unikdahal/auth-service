package com.unik.auth.application.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result class for password change operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeResult {
    private boolean success;
    private String message;
    private String errorCode;

    public static PasswordChangeResult success(String message) {
        return new PasswordChangeResult(true, message, null);
    }

    public static PasswordChangeResult failure(String message) {
        return new PasswordChangeResult(false, message, null);
    }

    public static PasswordChangeResult failure(String message, String errorCode) {
        return new PasswordChangeResult(false, message, errorCode);
    }
}
