package com.unik.auth.application.services;

import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.domain.exceptions.*;
import com.unik.auth.domain.valueobjects.Email;
import com.unik.auth.domain.valueobjects.Password;
import com.unik.auth.domain.valueobjects.UserId;
import com.unik.auth.ports.output.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Generic authentication service supporting any user and role type.
 */
@Slf4j
@Component
public class GenericAuthenticationService<U extends BaseUser<R>, R> {

    private final UserRepositoryPort<U, R> userRepository;
    private final List<AuthenticationStrategyPort<U, R, ?>> authenticationStrategies;
    private final TokenServicePort<U, R> tokenService;
    private final PasswordServicePort passwordService;
    private final NotificationServicePort<U, R> notificationService;
    private final UserFactoryPort<U, R> userFactory;

    @Autowired
    public GenericAuthenticationService(
            UserRepositoryPort<U, R> userRepository,
            List<AuthenticationStrategyPort<U, R, ?>> authenticationStrategies,
            TokenServicePort<U, R> tokenService,
            PasswordServicePort passwordService,
            NotificationServicePort<U, R> notificationService,
            UserFactoryPort<U, R> userFactory) {
        this.userRepository = userRepository;
        this.authenticationStrategies = authenticationStrategies;
        this.tokenService = tokenService;
        this.passwordService = passwordService;
        this.notificationService = notificationService;
        this.userFactory = userFactory;
    }

    /**
     * Registers a new user with the specified details.
     */
    @Transactional
    public AuthenticationResult<U> registerUser(RegistrationRequest<R> request) {
        try {
            log.info("Registering user with email: {}", request.getEmail());

            validateRegistrationRequest(request);

            Email email = Email.of(request.getEmail());

            // Check if a user already exists
            if (userRepository.existsByEmail(email)) {
                throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
            }

            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
            }

            // Create password value object and encode it
            Password password = Password.of(request.getPassword());
            String encodedPassword = passwordService.encode(password.getValue());
            Password encodedPasswordVO = Password.fromEncoded(encodedPassword);

            // Create user using factory
            U user = userFactory.createUser(
                    email,
                    request.getUsername(),
                    encodedPasswordVO,
                    request.getRoles(),
                    request.getAttributes()
            );

            // Save user
            U savedUser = userRepository.save(user);

            // Generate tokens
            String accessToken = tokenService.generateAccessToken(savedUser);
            String refreshToken = tokenService.generateRefreshToken(savedUser);

            // Send welcome notification
            notificationService.sendWelcomeNotification(savedUser);

            log.info("User registered successfully with ID: {}", savedUser.getId());

            return AuthenticationResult.success(
                    savedUser,
                    accessToken,
                    refreshToken,
                    "User registered successfully"
            );

        } catch (Exception e) {
            log.error("Registration failed for email: {}", request.getEmail(), e);
            return AuthenticationResult.failure(e.getMessage());
        }
    }

    /**
     * Authenticates a user with the given credentials using available strategies.
     */
    public <C> AuthenticationResult<U> authenticate(C credentials) {
        try {
            log.info("Authenticating user with credentials type: {}", credentials.getClass().getSimpleName());

            // Find a suitable authentication strategy
            AuthenticationStrategyPort<U, R, C> strategy = findAuthenticationStrategy(credentials);

            if (strategy == null) {
                throw new UnsupportedAuthenticationException("No authentication strategy found for credentials type: " + credentials.getClass().getSimpleName());
            }

            // Authenticate using strategy
            Optional<U> userOpt = strategy.authenticate(credentials);

            if (userOpt.isEmpty()) {
                throw new InvalidCredentialsException("Invalid credentials provided");
            }

            U user = userOpt.get();

            // Validate user status
            validateUserStatus(user);

            // Generate tokens
            String accessToken = tokenService.generateAccessToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);

            // Update last login
            updateLastLogin(user);

            log.info("User authenticated successfully: {}", user.getUsername());

            return AuthenticationResult.success(
                    user,
                    accessToken,
                    refreshToken,
                    "Authentication successful"
            );

        } catch (Exception e) {
            log.error("Authentication failed", e);
            return AuthenticationResult.failure(e.getMessage());
        }
    }

    /**
     * Refreshes an access token using a refresh token.
     */
    public TokenRefreshResult refreshToken(String refreshToken) {
        try {
            log.info("Refreshing access token");

            if (tokenService.isRefreshTokenRevoked(refreshToken)) {
                throw new InvalidTokenException("Refresh token has been revoked");
            }

            if (!tokenService.isTokenValidAndNotExpired(refreshToken)) {
                throw new InvalidTokenException("Refresh token is invalid or expired");
            }

            Optional<String> newAccessToken = tokenService.refreshAccessToken(refreshToken);

            if (newAccessToken.isEmpty()) {
                throw new InvalidTokenException("Failed to refresh access token");
            }

            log.info("Access token refreshed successfully");
            return TokenRefreshResult.success(newAccessToken.get());

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return TokenRefreshResult.failure(e.getMessage());
        }
    }

    /**
     * Logs out a user by revoking their tokens.
     */
    public LogoutResult logout(String refreshToken) {
        try {
            log.info("Logging out user");

            Optional<String> userIdOpt = tokenService.extractUserId(refreshToken);

            if (userIdOpt.isPresent()) {
                tokenService.revokeAllTokensForUser(userIdOpt.get());
                log.info("User logged out successfully");
                return LogoutResult.success("User logged out successfully");
            }

            return LogoutResult.failure("Invalid refresh token");

        } catch (Exception e) {
            log.error("Logout failed", e);
            return LogoutResult.failure(e.getMessage());
        }
    }

    /**
     * Changes a user's password.
     */
    public PasswordChangeResult changePassword(String userId, String currentPassword, String newPassword) {
        try {
            log.info("Changing password for user: {}", userId);

            U user = findUserById(userId);

            // Verify current password
            if (!passwordService.matches(currentPassword, user.getPassword())) {
                throw new InvalidCredentialsException("Current password is incorrect");
            }

            // Validate new password
            Password newPasswordVO = Password.of(newPassword);
            String encodedNewPassword = passwordService.encode(newPasswordVO.getValue());
            Password encodedNewPasswordVO = Password.fromEncoded(encodedNewPassword);

            // Update user password
            U updatedUser = userFactory.updatePassword(user, encodedNewPasswordVO);
            userRepository.update(updatedUser);

            // Revoke all existing tokens
            tokenService.revokeAllTokensForUser(userId);

            // Send password change notification
            notificationService.sendPasswordChangeNotification(updatedUser);

            log.info("Password changed successfully for user: {}", userId);
            return PasswordChangeResult.success("Password changed successfully");

        } catch (Exception e) {
            log.error("Password change failed for user: {}", userId, e);
            return PasswordChangeResult.failure(e.getMessage());
        }
    }

    /**
     * Validates token and returns user information.
     */
    public Optional<U> validateToken(String token) {
        try {
            if (!tokenService.isTokenValidAndNotExpired(token)) {
                return Optional.empty();
            }

            Optional<String> userIdOpt = tokenService.extractUserId(token);
            if (userIdOpt.isEmpty()) {
                return Optional.empty();
            }

            return getUserById(userIdOpt.get());

        } catch (Exception e) {
            log.error("Token validation failed", e);
            return Optional.empty();
        }
    }

    /**
     * Gets user information by ID.
     */
    public Optional<U> getUserById(String userId) {
        try {
            return Optional.of(findUserById(userId));
        } catch (Exception e) {
            log.error("Failed to get user by ID: {}", userId, e);
            return Optional.empty();
        }
    }

    /**
     * Gets user information by email.
     */
    public Optional<U> getUserByEmail(String email) {
        try {
            Email emailVO = Email.of(email);
            return userRepository.findByEmail(emailVO);
        } catch (Exception e) {
            log.error("Failed to get user by email: {}", email, e);
            return Optional.empty();
        }
    }

    /**
     * Gets user information by username.
     */
    public Optional<U> getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            log.error("Failed to get user by username: {}", username, e);
            return Optional.empty();
        }
    }

    // Private helper methods

    private void validateRegistrationRequest(RegistrationRequest<R> request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }
    @SuppressWarnings("unchecked")
    private <C> AuthenticationStrategyPort<U, R, C> findAuthenticationStrategy(C credentials) {
        for (AuthenticationStrategyPort<U, R, ?> strategy : authenticationStrategies) {
            // Cast the strategy to a raw type temporarily to avoid generic type issues
            if (((AuthenticationStrategyPort) strategy).supports(credentials.getClass()) && strategy.isEnabled()) {
                return (AuthenticationStrategyPort<U, R, C>) strategy;
            }
        }
        return null;
    }

    private void validateUserStatus(U user) {
        if (!user.isEnabled()) {
            throw new AccountDisabledException("Account is disabled");
        }

        if (user.isLocked()) {
            throw new AccountLockedException("Account is locked");
        }

        if (user.isAccountExpired()) {
            throw new AccountExpiredException("Account has expired");
        }

        if (user.isCredentialsExpired()) {
            throw new CredentialsExpiredException("Credentials have expired");
        }
    }

    private U findUserById(String userId) {
        UserId userIdVO = UserId.of(userId);
        return userRepository.findById(userIdVO)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    private void updateLastLogin(U user) {
        try {
            U updatedUser = userFactory.addAttribute(user, "lastLogin", LocalDateTime.now());
            userRepository.update(updatedUser);
        } catch (Exception e) {
            log.error("Failed to update last login for user: {}", user.getUsername(), e);
        }
    }
}
