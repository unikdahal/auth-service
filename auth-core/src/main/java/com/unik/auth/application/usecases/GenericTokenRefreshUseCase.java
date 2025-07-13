package com.unik.auth.application.usecases;

import com.unik.auth.application.services.GenericAuthenticationService;
import com.unik.auth.application.services.TokenRefreshResult;
import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.ports.input.TokenRefreshPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic use case for token refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class GenericTokenRefreshUseCase<U extends BaseUser<R>, R> implements TokenRefreshPort {

    private final GenericAuthenticationService<U, R> authService;

    @Override
    public TokenRefreshResult refreshToken(String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
