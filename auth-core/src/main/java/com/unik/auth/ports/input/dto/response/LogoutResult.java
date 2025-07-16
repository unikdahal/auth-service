package com.unik.auth.ports.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result class for logout operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResult {
    private boolean success;
    private String message;
    private String errorCode;

    public static LogoutResult success(String message) {
        return new LogoutResult(true, message, null);
    }

    public static LogoutResult failure(String message) {
        return new LogoutResult(false, message, null);
    }

    public static LogoutResult failure(String message, String errorCode) {
        return new LogoutResult(false, message, errorCode);
    }
}
