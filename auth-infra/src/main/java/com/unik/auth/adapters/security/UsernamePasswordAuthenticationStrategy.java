package com.unik.auth.adapters.security;

import com.unik.auth.application.services.UsernamePasswordCredentials;
import com.unik.auth.domain.entities.GenericUser;
import com.unik.auth.ports.output.AuthenticationStrategyPort;
import com.unik.auth.ports.output.PasswordServicePort;
import com.unik.auth.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the AuthenticationStrategyPort for username/password authentication.
 * This strategy authenticates users based on their username or email and password.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationStrategy<R> implements AuthenticationStrategyPort<GenericUser<R>, R, UsernamePasswordCredentials> {

    private final UserRepositoryPort<GenericUser<R>, R> userRepository;
    private final PasswordServicePort passwordService;

    @Override
    public Optional<GenericUser<R>> authenticate(UsernamePasswordCredentials credentials) {
        log.debug("Authenticating user with username: {}", credentials.getUsername());

        // Prepare credentials
        credentials = prepareCredentials(credentials);

        // Validate credentials format
        if (!validateCredentialsFormat(credentials)) {
            log.warn("Invalid credentials format for username: {}", credentials.getUsername());
            return Optional.empty();
        }

        // Try to find the user by username or email
        Optional<GenericUser<R>> userOpt = userRepository.findByUsername(credentials.getUsername());

        if (userOpt.isEmpty()) {
            // If not found by username, try email
            try {
                com.unik.auth.domain.valueobjects.Email email = 
                    com.unik.auth.domain.valueobjects.Email.of(credentials.getUsername());
                userOpt = userRepository.findByEmail(email);
            } catch (Exception e) {
                log.debug("Invalid email format: {}", credentials.getUsername());
            }
        }

        if (userOpt.isEmpty()) {
            log.debug("User not found with username: {}", credentials.getUsername());
            return Optional.empty();
        }

        GenericUser<R> user = userOpt.get();

        // Verify password
        if (passwordService.matches(credentials.getPassword(), user.getPassword())) {
            log.debug("Password does not match for user: {}", user.getUsername());
            return Optional.empty();
        }

        log.info("User authenticated successfully: {}", user.getUsername());
        return Optional.of(user);
    }

    @Override
    public boolean supports(Class<? extends UsernamePasswordCredentials> credentialsType) {
        return UsernamePasswordCredentials.class.isAssignableFrom(credentialsType);
    }

    @Override
    public String getStrategyName() {
        return "username-password";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean validateCredentialsFormat(UsernamePasswordCredentials credentials) {
        if (credentials == null) {
            return false;
        }

        if (credentials.getUsername() == null || credentials.getUsername().trim().isEmpty()) {
            return false;
        }

        if (credentials.getPassword() == null || credentials.getPassword().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public UsernamePasswordCredentials prepareCredentials(UsernamePasswordCredentials credentials) {
        if (credentials == null) {
            return null;
        }

        // Trim username and password
        String username = credentials.getUsername() != null ? 
                credentials.getUsername().trim() : null;
        String password = credentials.getPassword() != null ? 
                credentials.getPassword() : null;

        return new UsernamePasswordCredentials(username, password);
    }
}
