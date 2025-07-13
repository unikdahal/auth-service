package com.unik.auth.ports.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Input port for user registration operations.
 */
public interface RegisterUserPort<U, R> {

    RegisterUserResult<U> registerUser(RegisterUserRequest<R> request);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterUserRequest<R> implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @NonNull private String email;
        @NonNull private String username;
        @NonNull private String password;
        @Builder.Default private Set<R> roles = Set.of();
        @Builder.Default private Map<String, Object> attributes = Map.of();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterUserResult<U> implements Serializable {
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
}
