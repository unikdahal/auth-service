package com.unik.auth.adapters.security;

import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.ports.output.TokenServicePort;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

/**
 * A generic implementation of the TokenServicePort that uses JWT tokens.
 * This implementation is stateless, meaning tokens are validated purely based on their cryptographic signature
 * and claims, without dependency on a persistent store.
 *
 * @param <U> The user entity type extending BaseUser
 * @param <R> The role type used by the user entity
 */
@Slf4j
@Service
public class JwtTokenService<U extends BaseUser<R>, R> implements TokenServicePort<U, R> {
    private final SecretKey secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final String tokenType;

    // JWT standard claim names
    public static final String SUBJECT = "sub";
    public static final String ISSUED_AT = "iat";
    public static final String EXPIRATION = "exp";

    // Custom claim names
    public static final String TOKEN_TYPE_CLAIM = "typ";
    public static final String ROLES_CLAIM = "roles";
    public static final String USERNAME_CLAIM = "username";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";
    public static final String USER_ID_CLAIM = "userId";

    public JwtTokenService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-validity-seconds}") long accessTokenValidity,
            @Value("${security.jwt.refresh-token-validity-seconds}") long refreshTokenValidity,
            @Value("${security.jwt.token-type:Bearer}") String tokenType
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.tokenType = tokenType;
    }

    @Override
    public String generateAccessToken(U user) {
        return generateAccessToken(user, Collections.emptyMap());
    }

    @Override
    public String generateAccessToken(U user, Duration expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);

        Instant now = Instant.now();
        Instant expirationTime = now.plus(expiration);

        return buildToken(user, claims, now, expirationTime);
    }

    @Override
    public String generateAccessToken(U user, Map<String, Object> customClaims) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(accessTokenValidity);

        Map<String, Object> claims = new HashMap<>(customClaims);
        claims.put(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);

        return buildToken(user, claims, now, expirationTime);
    }

    @Override
    public String generateRefreshToken(U user) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(refreshTokenValidity);

        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE);

        return buildToken(user, claims, now, expirationTime);
    }

    @Override
    public String generateRefreshToken(U user, Duration expiration) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE);

        return buildToken(user, claims, now, expirationTime);
    }

    @Override
    public boolean isTokenValid(String token) {
        return isTokenValidAndNotExpired(token);
    }

    /**
     * Core token building method that creates a JWT with the given parameters.
     *
     * @param user The user for whom the token is being created
     * @param claims Map of claims to include in the token
     * @param issuedAt When the token was issued
     * @param expiration When the token expires
     * @return The compact JWT string
     */
    protected String buildToken(U user, Map<String, Object> claims, Instant issuedAt, Instant expiration) {
        JwtBuilder builder = Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration));

        // Add username and roles if they aren't already in the claims
        if (!claims.containsKey(USERNAME_CLAIM)) {
            builder.claim(USERNAME_CLAIM, user.getUsername());
        }

        if (!claims.containsKey(ROLES_CLAIM)) {
            builder.claim(ROLES_CLAIM, user.getRoles());
        }

        // Add all provided claims
        claims.forEach(builder::claim);

        return builder.signWith(secretKey).compact();
    }

    @Override
    public Optional<String> refreshAccessToken(String refreshToken) {
        try {
            if (!isTokenValidAndNotExpired(refreshToken)) {
                log.debug("Cannot refresh with invalid or expired token");
                return Optional.empty();
            }

            Claims claims = extractAllClaims(refreshToken);
            String userId = claims.getSubject();

            // Verify this is actually a refresh token
            String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
            if (!REFRESH_TOKEN_TYPE.equals(tokenType)) {
                log.debug("Cannot refresh with non-refresh token type: {}", tokenType);
                return Optional.empty();
            }

            // Prepare claims for the new access token
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);

            // Transfer relevant claims from refresh token to the new access token
            transferClaimIfPresent(claims, newClaims, USERNAME_CLAIM);
            transferClaimIfPresent(claims, newClaims, ROLES_CLAIM);

            // Generate a new access token with the same subject and transferred claims
            Instant now = Instant.now();
            return Optional.of(Jwts.builder()
                    .subject(userId)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusSeconds(accessTokenValidity)))
                    .claims(newClaims)  // This overwrites the subject, so we need to set it again
                    .subject(userId)    // Set the subject again after claims
                    .signWith(secretKey)
                    .compact());
        } catch (Exception e) {
            log.error("Failed to refresh access token", e);
            return Optional.empty();
        }
    }

    /**
     * Helper method to transfer a claim from source to target if it exists
     */
    private void transferClaimIfPresent(Claims source, Map<String, Object> target, String claimName) {
        if (source.containsKey(claimName)) {
            target.put(claimName, source.get(claimName));
        }
    }

    @Override
    public String getTokenType() {
        return this.tokenType;
    }

    @Override
    public boolean isTokenSignatureValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.debug("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            // Token is expired but signature is valid
            return true;
        } catch (Exception e) {
            log.debug("JWT token validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a token is both valid (well-formed, correct signature) and not expired.
     */
    public boolean isTokenValidAndNotExpired(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<String> extractUserId(String token) {
        try {
            return Optional.of(extractClaim(token, Claims::getSubject));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Extract a specific claim from a token using the provided function
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from a token
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            // For expired tokens, we still want to extract the claims
            return e.getClaims();
        }
    }

    @Override
    public Optional<Object> extractClaim(String token, String claimName) {
        try {
            Claims claims = extractAllClaims(token);
            return Optional.ofNullable(claims.get(claimName));
        } catch (Exception e) {
            log.debug("Failed to extract claim {}: {}", claimName, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get the token type (access or refresh) from a token
     */
    public Optional<String> getTokenType(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return Optional.ofNullable(claims.get(TOKEN_TYPE_CLAIM, String.class));
        } catch (Exception e) {
            log.debug("Failed to extract token type: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Extract roles from a token
     */
    @SuppressWarnings("unchecked")
    public Optional<Set<R>> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return Optional.ofNullable((Set<R>) claims.get(ROLES_CLAIM));
        } catch (Exception e) {
            log.debug("Failed to extract roles: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<LocalDateTime> getTokenExpirationTime(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            if (expiration != null) {
                return Optional.of(expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            } else {
                log.debug("No expiration claim found in token");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.debug("Failed to extract expiration time: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<LocalDateTime> getTokenIssueTime(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date issuedAt = claims.getIssuedAt();
            if (issuedAt != null) {
                return Optional.of(issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            } else {
                log.debug("No issued at claim found in token");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.debug("Failed to extract issue time: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Extract username from a token
     */
    public Optional<String> extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return Optional.ofNullable(claims.get(USERNAME_CLAIM, String.class));
        } catch (Exception e) {
            log.debug("Failed to extract username: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
