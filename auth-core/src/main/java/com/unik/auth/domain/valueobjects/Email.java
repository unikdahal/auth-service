package com.unik.auth.domain.valueobjects;

import com.unik.auth.domain.exceptions.InvalidEmailException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Immutable value object representing an email address.
 *
 * <p>This class encapsulates email validation logic and ensures that only valid
 * email addresses can be created. It follows Domain-Driven Design principles
 * and provides a type-safe way to handle email addresses throughout the application.</p>
 */
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * RFC 5322 compliant email regex pattern.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    /**
     * The normalized email value (trimmed and lowercase).
     */
    private final String value;

    /**
     * Factory method to create a validated Email instance.
     *
     * @param email the email string to validate and encapsulate
     * @return a new Email instance with validated and normalized email
     * @throws IllegalArgumentException if the email is null, blank, or has invalid format
     */
    public static Email of(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email cannot be null or blank");
        }

        String normalizedEmail = normalizeEmail(email);

        if (!isValidEmail(normalizedEmail)) {
            throw new InvalidEmailException("Invalid email format: " + email);
        }

        return new Email(normalizedEmail);
    }

    /**
     * Normalizes the email by trimming whitespace and converting to lowercase.
     *
     * @param email the raw email string
     * @return normalized email string
     */
    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    /**
     * Validates the email format using RFC 5322 compliant regex.
     *
     * @param email the email to validate
     * @return true if email format is valid, false otherwise
     */
    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Returns the local part of the email (before @).
     *
     * @return the local part of the email
     */
    public String getLocalPart() {
        int atIndex = value.indexOf('@');
        if (atIndex < 1) {
            throw new InvalidEmailException("Invalid email: missing local part");
        }
        return value.substring(0, atIndex);
    }

    /**
     * Returns the domain part of the email (after @).
     *
     * @return the domain part of the email
     */
    public String getDomain() {
        int atIndex = value.indexOf('@');
        if (atIndex < 0 || atIndex == value.length() - 1) {
            throw new InvalidEmailException("Invalid email: missing domain part");
        }
        return value.substring(atIndex + 1);
    }
}