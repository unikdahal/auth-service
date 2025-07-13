package com.unik.auth.application.usecases;

import com.unik.auth.application.services.EmailPasswordCredentials;
import com.unik.auth.application.services.GenericAuthenticationService;
import com.unik.auth.application.services.AuthenticationResult;
import com.unik.auth.application.services.UsernamePasswordCredentials;
import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.ports.input.AuthenticateUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic use case for user authentication.
 */
@Slf4j
@RequiredArgsConstructor
public class GenericAuthenticateUserUseCase<U extends BaseUser<R>, R> implements AuthenticateUserPort<U, R> {

    private final GenericAuthenticationService<U, R> authService;

    @Override
    public <C> AuthenticationResult<U> authenticate(C credentials) {
        return authService.authenticate(credentials);
    }

    @Override
    public AuthenticationResult<U> authenticateWithUsernamePassword(String username, String password) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        return authService.authenticate(credentials);
    }

    @Override
    public AuthenticationResult<U> authenticateWithEmail(String email, String password) {
        EmailPasswordCredentials credentials = new EmailPasswordCredentials(email, password);
        return authService.authenticate(credentials);
    }
}
