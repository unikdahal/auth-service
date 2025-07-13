package com.unik.auth.adapters.security;

import com.unik.auth.adapters.cache.RedisTokenStore;
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
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
public class JwtTokenService<U extends BaseUser<R>, R> implements TokenServicePort<U, R> {
    private final SecretKey secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final String tokenType;
    private final RedisTokenStore tokenStore;

    private static final String REVOKED_TOKEN_PREFIX = "revoked:";
    private static final String USER_TOKENS_PREFIX = "user:";

    public JwtTokenService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-validity-seconds}") long accessTokenValidity,
            @Value("${security.jwt.refresh-token-validity-seconds}") long refreshTokenValidity,
            @Value("${security.jwt.token-type:Bearer}") String tokenType,
            RedisTokenStore tokenStore
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.tokenType = tokenType;
        this.tokenStore = tokenStore;
    }

    @Override
    public String generateAccessToken(U user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTokenValidity)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateAccessToken(U user, Duration expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateAccessToken(U user, Map<String, Object> customClaims) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTokenValidity)));

        // Add custom claims
        if (customClaims != null) {
            customClaims.forEach(builder::claim);
        }

        return builder.signWith(secretKey).compact();
    }

    @Override
    public String generateRefreshToken(U user) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshTokenValidity)))
                .signWith(secretKey)
                .compact();

        // Store the refresh token in Redis with user ID
//        tokenStore.storeToken(
//                USER_TOKENS_PREFIX + user.getId().toString() + ":" + token,
//                token,
//                Duration.ofSeconds(refreshTokenValidity)
//        );

        return token;
    }

    @Override
    public String generateRefreshToken(U user, Duration expiration) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();

        // Store the refresh token in Redis with user ID
        tokenStore.storeToken(
                USER_TOKENS_PREFIX + user.getId().toString() + ":" + token,
                token,
                expiration
        );

        return token;
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isTokenValidAndNotExpired(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !isTokenExpired(token) && !isRefreshTokenRevoked(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<String> extractUsername(String token) {
        try {
            return Optional.ofNullable(extractClaim(token, Claims::getSubject))
                    .map(subject -> extractClaim(token, claims -> claims.get("username", String.class)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> extractUserId(String token) {
        try {
            return Optional.ofNullable(extractClaim(token, Claims::getSubject));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(new HashMap<>(claims));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Object> extractClaim(String token, String claimName) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.ofNullable(claims.get(claimName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Set<R>> extractRoles(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.ofNullable((Set<R>) claims.get("roles"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public Optional<LocalDateTime> getTokenExpirationTime(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return Optional.of(LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<LocalDateTime> getTokenIssueTime(String token) {
        try {
            Date issuedAt = extractClaim(token, Claims::getIssuedAt);
            return Optional.of(LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> refreshAccessToken(String refreshToken) {
        if (!isTokenValidAndNotExpired(refreshToken)) {
            return Optional.empty();
        }

        Optional<String> userIdOpt = extractUserId(refreshToken);
        if (userIdOpt.isEmpty()) {
            return Optional.empty();
        }

        // Check if the refresh token is in the store
        String tokenKey = USER_TOKENS_PREFIX + userIdOpt.get() + ":" + refreshToken;
        if (!tokenStore.exists(tokenKey)) {
            return Optional.empty();
        }

        // Create a minimal user object with just the ID to generate a new access token
        // In a real implementation, you would likely fetch the user from a repository
        try {
            // Extract claims to create a new access token with the same information
            Optional<Map<String, Object>> claimsOpt = extractAllClaims(refreshToken);
            if (claimsOpt.isEmpty()) {
                return Optional.empty();
            }

            // Create a new access token
            Instant now = Instant.now();
            JwtBuilder builder = Jwts.builder()
                    .setSubject(userIdOpt.get())
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusSeconds(accessTokenValidity)));

            // Add all claims except standard ones
            Map<String, Object> claims = claimsOpt.get();
            claims.forEach((key, value) -> {
                if (!key.equals("sub") && !key.equals("iat") && !key.equals("exp")) {
                    builder.claim(key, value);
                }
            });

            return Optional.of(builder.signWith(secretKey).compact());
        } catch (Exception e) {
            log.error("Error refreshing access token", e);
            return Optional.empty();
        }
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        if (!isTokenValid(refreshToken)) {
            return;
        }

        Optional<String> userIdOpt = extractUserId(refreshToken);
        if (userIdOpt.isEmpty()) {
            return;
        }

        // Delete the token from the user's tokens
        String tokenKey = USER_TOKENS_PREFIX + userIdOpt.get() + ":" + refreshToken;
        tokenStore.deleteToken(tokenKey);

        // Add to revoked tokens
        Optional<LocalDateTime> expirationOpt = getTokenExpirationTime(refreshToken);
        if (expirationOpt.isPresent()) {
            LocalDateTime expiration = expirationOpt.get();
            Duration ttl = Duration.between(LocalDateTime.now(), expiration);
            if (!ttl.isNegative()) {
                tokenStore.storeToken(REVOKED_TOKEN_PREFIX + refreshToken, "revoked", ttl);
            }
        }
    }

    @Override
    public boolean isRefreshTokenRevoked(String refreshToken) {
        // Check if token is in the revoked tokens list
        return tokenStore.exists(REVOKED_TOKEN_PREFIX + refreshToken);
    }

    @Override
    public void revokeAllTokensForUser(String userId) {
        // In a real implementation, you would need to scan Redis for all tokens
        // belonging to the user and revoke them
        // This is a simplified implementation
        log.info("Revoking all tokens for user: {}", userId);
        // The actual implementation would depend on Redis capabilities
        // and how tokens are stored
    }

    @Override
    public String getTokenType() {
        return tokenType;
    }

    @Override
    public boolean isTokenSignatureValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Map<String, Object>> extractTokenMetadata(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("algorithm", jws.getHeader().getAlgorithm());
            metadata.put("type", jws.getHeader().getType());
            metadata.put("issuer", jws.getBody().getIssuer());
            metadata.put("audience", jws.getBody().getAudience());
            metadata.put("id", jws.getBody().getId());

            return Optional.of(metadata);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Helper method to extract claims
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
