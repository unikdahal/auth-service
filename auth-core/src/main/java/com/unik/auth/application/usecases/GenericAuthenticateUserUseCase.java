package com.unik.auth.application.usecases;

import com.unik.auth.application.services.*;
import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.ports.input.AuthenticateUserPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generic use case for user authentication supporting multiple authentication methods.
 * This implementation allows for flexible authentication strategies and can be extended
 * for different types of users and roles.
 *
 * @param <U> User type extending BaseUser
 * @param <R> Role type used by the user
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class GenericAuthenticateUserUseCase<U extends BaseUser<R>, R> implements AuthenticateUserPort<U, R> {

    private final GenericAuthenticationService<U, R> authService;

    /**
     * Authenticates a user with the provided generic credentials.
     * This method delegates to the authentication service to find an appropriate
     * authentication strategy based on the credential type.
     *
     * @param <C> The type of credentials provided
     * @param credentials The credentials to authenticate with
     * @return Authentication result containing user info and tokens if successful
     */
    @Override
    public <C> AuthenticationResult<U> authenticate(C credentials) {
        log.debug("Authenticating with credentials of type: {}", credentials.getClass().getSimpleName());
        return authService.authenticate(credentials);
    }

    /**
     * Authenticates a user with username and password credentials.
     *
     * @param username The username
     * @param password The password
     * @return Authentication result containing user info and tokens if successful
     */
    @Override
    public AuthenticationResult<U> authenticateWithUsernamePassword(String username, String password) {
        log.debug("Authenticating with username: {}", username);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        return authService.authenticate(credentials);
    }

    /**
     * Authenticates a user with email and password credentials.
     *
     * @param email The email address
     * @param password The password
     * @return Authentication result containing user info and tokens if successful
     */
    @Override
    public AuthenticationResult<U> authenticateWithEmail(String email, String password) {
        log.debug("Authenticating with email: {}", email);
        EmailPasswordCredentials credentials = new EmailPasswordCredentials(email, password);
        return authService.authenticate(credentials);
    }

    @Override
    public Boolean isAuthenticated(String accessToken) {
        log.debug("Checking authentication status for access token: {}", accessToken);
        return authService.isAuthenticated(accessToken);
    }

    /** TODO: Implement other authentication methods as needed
     *
     * Authenticates a user with a one-time password or verification code.
     *
     * @param identifier User identifier (email or phone)
     * @param code One-time password or verification code
     * @return Authentication result containing user info and tokens if successful

    @Override
    public AuthenticationResult<U> authenticateWithOTP(String identifier, String code) {
        log.debug("Authenticating with OTP for identifier: {}", identifier);
        OTPCredentials credentials = new OTPCredentials(identifier, code);
        return authService.authenticate(credentials);
    }


     * Authenticates a user with a social login provider.
     *
     * @param provider Name of the provider (e.g., "google", "facebook")
     * @param token Access token or ID token from the provider
     * @return Authentication result containing user info and tokens if successful

    @Override
    public AuthenticationResult<U> authenticateWithSocialProvider(String provider, String token) {
        log.debug("Authenticating with social provider: {}", provider);
        SocialLoginCredentials credentials = new SocialLoginCredentials(provider, token);
        return authService.authenticate(credentials);
    }


     * Authenticates a user with a custom authentication strategy.
     *
     * @param strategyName Name of the strategy to use
     * @param parameters Map of parameters required by the strategy
     * @return Authentication result containing user info and tokens if successful

    @Override
    public AuthenticationResult<U> authenticateWithCustomStrategy(String strategyName, Map<String, Object> parameters) {
        log.debug("Authenticating with custom strategy: {}", strategyName);
        CustomCredentials credentials = new CustomCredentials(strategyName, parameters);
        return authService.authenticate(credentials);
    }
    */
}
