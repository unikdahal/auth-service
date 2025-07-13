package com.unik.auth.application.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Result class for token refresh operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String accessToken;
    private String message;
    private String errorCode;

    public static TokenRefreshResult success(String accessToken) {
        return new TokenRefreshResult(true, accessToken, "Token refreshed successfully", null);
    }

    public static TokenRefreshResult failure(String message) {
        return new TokenRefreshResult(false, null, message, null);
    }

    public static TokenRefreshResult failure(String message, String errorCode) {
        return new TokenRefreshResult(false, null, message, errorCode);
    }
}
