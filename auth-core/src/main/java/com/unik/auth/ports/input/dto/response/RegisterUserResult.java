package com.unik.auth.ports.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserResult<U> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private boolean success;
    private U user;
    private String accessToken;
    private String refreshToken;
    private String message;
    private String errorCode;

    public static <U> RegisterUserResult<U> success(U user, String accessToken, String refreshToken, String message) {
        return RegisterUserResult.<U>builder()
                .success(true)
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message(message)
                .build();
    }

    public static <U> RegisterUserResult<U> failure(String message) {
        return RegisterUserResult.<U>builder()
                .success(false)
                .message(message)
                .build();
    }

    public static <U> RegisterUserResult<U> failure(String message, String errorCode) {
        return RegisterUserResult.<U>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}