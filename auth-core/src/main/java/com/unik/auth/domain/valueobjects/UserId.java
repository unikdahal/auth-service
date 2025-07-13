package com.unik.auth.domain.valueobjects;

import com.unik.auth.domain.exceptions.UserNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public final class UserId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String value;

    private UserId(String value) {
        this.value = value;
    }

    /**
     * Factory method to create a validated UserId instance from a string.
     * Accepts UUID or any non-blank string.
     *
     * @param id the user id string
     * @return a new UserId instance
     * @throws UserNotFoundException if the id is null or blank
     */
    public static UserId of(String id) {
        if (id == null || id.isBlank())
            throw new UserNotFoundException("UserId cannot be null or blank");
        return new UserId(id.trim());
    }

    /**
     * Generates a new UserId using a random UUID.
     *
     * @return a new UserId instance
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    /**
     * Returns the string representation of the UserId.
     *
     * @return the user id string
     */
    @Override
    public String toString() {
        return value;
    }
}
