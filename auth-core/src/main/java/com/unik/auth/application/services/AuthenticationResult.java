package com.unik.auth.application.services;

import com.unik.auth.domain.entities.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic result class for authentication operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResult<U extends BaseUser<?>> {
    private boolean success;
    private U user;
    private String accessToken;
    private String refreshToken;
    private String message;
    private String errorCode;

    public static <U extends BaseUser<?>> AuthenticationResult<U> success(U user, String accessToken,
                                                                          String refreshToken, String message) {
        return new AuthenticationResult<>(true, user, accessToken, refreshToken, message, null);
    }

    public static <U extends BaseUser<?>> AuthenticationResult<U> failure(String message) {
        return new AuthenticationResult<>(false, null, null, null, message, null);
    }

    public static <U extends BaseUser<?>> AuthenticationResult<U> failure(String message, String errorCode) {
        return new AuthenticationResult<>(false, null, null, null, message, errorCode);
    }
}
