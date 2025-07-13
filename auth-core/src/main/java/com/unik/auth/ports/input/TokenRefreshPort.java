package com.unik.auth.ports.input;

import com.unik.auth.application.services.TokenRefreshResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Input port for token refresh operations.
 */
public interface TokenRefreshPort {

    TokenRefreshResult refreshToken(String refreshToken);
}
