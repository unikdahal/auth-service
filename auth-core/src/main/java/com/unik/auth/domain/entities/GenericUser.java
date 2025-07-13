package com.unik.auth.domain.entities;

import com.unik.auth.domain.valueobjects.Email;
import com.unik.auth.domain.valueobjects.Password;
import com.unik.auth.domain.valueobjects.UserId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * GenericUser represents a user in the system with generic role support.
 *
 * @param <R> the type of the role
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenericUser<R> implements BaseUser<R>, java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UserId id;
    private Email email;
    private String username;
    private Password password;
    @Builder.Default
    private Set<R> roles = new HashSet<>();
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean locked = false;
    @Builder.Default
    private boolean accountExpired = false;
    @Builder.Default
    private boolean credentialsExpired = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();
    private String displayName;

    /**
     * Factory method to create a new GenericUser instance.
     *
     * @param email    the user's email
     * @param username the user's username
     * @param password the user's password
     * @param roles    the user's roles
     * @return a new GenericUser instance
     */
    public static <R> GenericUser<R> create(Email email, String username, Password password, Set<R> roles) {
        return GenericUser.<R>builder().id(UserId.generate()).email(email).username(username).password(password).roles(roles != null ? new HashSet<>(roles) : new HashSet<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }

    /**
     * Updates the user's attributes in a type-safe and immutable way.
     *
     * @param key   attribute key
     * @param value attribute value
     * @return a new GenericUser instance with updated attributes
     */
    public GenericUser<R> withAttribute(String key, Object value) {
        Map<String, Object> newAttributes = new HashMap<>(this.attributes);
        newAttributes.put(key, value);
        return this.toBuilder().attributes(newAttributes).build();
    }

    /**
     * Returns a display name, falling back to username or email if not set.
     *
     * @return display name
     */
    public String getDisplayName() {
        if (displayName != null && !displayName.isBlank()) return displayName;
        if (username != null && !username.isBlank()) return username;
        return email != null ? email.toString() : "";
    }

    @Override
    public boolean isValid() {
        return email != null && username != null && !username.trim().isEmpty() && password != null && !accountExpired && !credentialsExpired;
    }

    @Override
    public String getEmail() {
        return email != null ? email.getValue() : null;
    }

    @Override
    public String getPassword() {
        return password != null ? password.getValue() : null;
    }
}
