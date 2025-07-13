package com.unik.auth.ports.output;

import com.unik.auth.domain.entities.BaseUser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Generic token service port.
 */
public interface TokenServicePort<U extends BaseUser<R>, R> {

    /**
     * Generates an access token for the given user.
     * @param user the user entity
     * @return the generated access token
     */
    String generateAccessToken(U user);

    /**
     * Generates an access token for the given user with a custom expiration.
     * @param user the user entity
     * @param expiration the expiration duration
     * @return the generated access token
     */
    String generateAccessToken(U user, Duration expiration);

    /**
     * Generates an access token for the given user with custom claims.
     * @param user the user entity
     * @param customClaims the custom claims to include in the token
     * @return the generated access token
     */
    String generateAccessToken(U user, Map<String, Object> customClaims);

    /**
     * Generates a refresh token for the given user.
     * @param user the user entity
     * @return the generated refresh token
     */
    String generateRefreshToken(U user);

    /**
     * Generates a refresh token for the given user with a custom expiration.
     * @param user the user entity
     * @param expiration the expiration duration
     * @return the generated refresh token
     */
    String generateRefreshToken(U user, Duration expiration);

    /**
     * Checks if the token is structurally valid (signature and format).
     * @param token the token string
     * @return true if valid, false otherwise
     */
    boolean isTokenValid(String token);

    /**
     * Checks if the token is valid and not expired.
     * @param token the token string
     * @return true if valid and not expired, false otherwise
     */
    boolean isTokenValidAndNotExpired(String token);

    /**
     * Extracts the username from the token if present.
     * @param token the token string
     * @return an Optional containing the username if present
     */
    Optional<String> extractUsername(String token);

    /**
     * Extracts the user ID from the token if present.
     * @param token the token string
     * @return an Optional containing the user ID if present
     */
    Optional<String> extractUserId(String token);

    /**
     * Extracts all claims from the token.
     * @param token the token string
     * @return an Optional containing a map of all claims
     */
    Optional<Map<String, Object>> extractAllClaims(String token);

    /**
     * Extracts a specific claim from the token.
     * @param token the token string
     * @param claimName the name of the claim
     * @return an Optional containing the claim value if present
     */
    Optional<Object> extractClaim(String token, String claimName);

    /**
     * Extracts the roles from the token if present.
     * @param token the token string
     * @return an Optional containing a set of roles
     */
    Optional<Set<R>> extractRoles(String token);

    /**
     * Checks if the token is expired.
     * @param token the token string
     * @return true if expired, false otherwise
     */
    boolean isTokenExpired(String token);

    /**
     * Gets the expiration time of the token.
     * @param token the token string
     * @return an Optional containing the expiration time
     */
    Optional<LocalDateTime> getTokenExpirationTime(String token);

    /**
     * Gets the issue time of the token.
     * @param token the token string
     * @return an Optional containing the issue time
     */
    Optional<LocalDateTime> getTokenIssueTime(String token);

    /**
     * Refreshes the access token using a refresh token.
     * @param refreshToken the refresh token
     * @return an Optional containing the new access token if successful
     */
    Optional<String> refreshAccessToken(String refreshToken);

    /**
     * Revokes a refresh token, making it unusable.
     * @param refreshToken the refresh token to revoke
     */
    void revokeRefreshToken(String refreshToken);

    /**
     * Checks if a refresh token has been revoked.
     * @param refreshToken the refresh token
     * @return true if revoked, false otherwise
     */
    boolean isRefreshTokenRevoked(String refreshToken);

    /**
     * Revokes all tokens for a user by user ID.
     * @param userId the user ID
     */
    void revokeAllTokensForUser(String userId);

    /**
     * Gets the type of the token (e.g., "Bearer").
     * @return the token type
     */
    String getTokenType();

    /**
     * Checks if the token's signature is valid.
     * @param token the token string
     * @return true if the signature is valid, false otherwise
     */
    boolean isTokenSignatureValid(String token);

    /**
     * Extracts token metadata (e.g., issuer, audience, etc.).
     * @param token the token string
     * @return an Optional containing a map of token metadata
     */
    Optional<Map<String, Object>> extractTokenMetadata(String token);
}
