package com.unik.auth.domain.valueobjects;

import com.unik.auth.domain.exceptions.InvalidPasswordException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.io.Serial;
import java.io.Serializable;

/**
 * Immutable value object representing a password.
 * <p>
 * Enforces password policy and provides factory methods for raw and encoded passwords.
 * </p>
 */
@Getter
@EqualsAndHashCode
public final class Password implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    /**
     * Factory method to create a validated Password instance from a raw password.
     *
     * @param raw the raw password string
     * @return a new Password instance
     * @throws InvalidPasswordException if the password does not meet policy
     */
    public static Password of(String raw) {
        if (raw == null || raw.isBlank())
            throw new InvalidPasswordException("Password cannot be null or blank");
        if (raw.length() < MIN_LENGTH || raw.length() > MAX_LENGTH)
            throw new InvalidPasswordException("Password must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters");
        if (raw.chars().noneMatch(Character::isUpperCase))
            throw new InvalidPasswordException("Password must contain an uppercase letter");
        if (raw.chars().noneMatch(Character::isLowerCase))
            throw new InvalidPasswordException("Password must contain a lowercase letter");
        if (raw.chars().noneMatch(Character::isDigit))
            throw new InvalidPasswordException("Password must contain a digit");
        if (raw.chars().allMatch(Character::isLetterOrDigit))
            throw new InvalidPasswordException("Password must contain a special character");
        return new Password(raw);
    }

    /**
     * Factory method to create a Password instance from an encoded password.
     *
     * @param encoded the encoded password string
     * @return a new Password instance
     * @throws InvalidPasswordException if the encoded password is null or blank
     */
    public static Password fromEncoded(String encoded) {
        if (encoded == null || encoded.isBlank())
            throw new InvalidPasswordException("Encoded password cannot be null or blank");
        return new Password(encoded);
    }

    /**
     * Returns true if the password is already encoded (supports multiple encoding schemes).
     *
     * @return true if encoded, false otherwise
     */
    public boolean isEncoded() {
        // Add more encoding scheme checks as needed
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$") // bcrypt
            || value.startsWith("$argon2") // argon2
            || value.startsWith("$scrypt$") // scrypt
            || value.matches("^[A-Fa-f0-9]{64,}$"); // generic hex hash (e.g., SHA-256)
    }

    /**
     * Returns a protected string representation of the password.
     *
     * @return [PROTECTED]
     */
    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
