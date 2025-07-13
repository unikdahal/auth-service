package com.unik.auth.ports.output;

import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.domain.valueobjects.Email;
import com.unik.auth.domain.valueobjects.Password;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Generic user factory port for creating and updating user domain objects in a type-safe, immutable, and extensible way.
 *
 * @param <U> the user type
 * @param <R> the role type
 */
public interface UserFactoryPort<U extends BaseUser<R>, R> {
    /**
     * Creates a new user instance.
     * @param email the user's email
     * @param username the user's username
     * @param password the user's password
     * @param roles the user's roles
     * @param attributes additional user attributes
     * @return a new user instance
     */
    U createUser(Email email, String username, Password password, Set<R> roles, Map<String, Object> attributes);

    /**
     * Updates the user's password.
     * @param user the user entity
     * @param newPassword the new password
     * @return a new user instance with updated password
     */
    U updatePassword(U user, Password newPassword);

    /**
     * Enables the user account.
     * @param user the user entity
     * @return a new user instance with enabled status
     */
    U enableUser(U user);

    /**
     * Disables the user account.
     * @param user the user entity
     * @return a new user instance with disabled status
     */
    U disableUser(U user);

    /**
     * Locks the user account.
     * @param user the user entity
     * @return a new user instance with locked status
     */
    U lockUser(U user);

    /**
     * Unlocks the user account.
     * @param user the user entity
     * @return a new user instance with unlocked status
     */
    U unlockUser(U user);

    /**
     * Expires the user account.
     * @param user the user entity
     * @return a new user instance with expired account status
     */
    U expireAccount(U user);

    /**
     * Expires the user's credentials.
     * @param user the user entity
     * @return a new user instance with expired credentials status
     */
    U expireCredentials(U user);

    /**
     * Adds a role to the user.
     * @param user the user entity
     * @param role the role to add
     * @return a new user instance with the added role
     */
    U addRole(U user, R role);

    /**
     * Removes a role from the user.
     * @param user the user entity
     * @param role the role to remove
     * @return a new user instance with the role removed
     */
    U removeRole(U user, R role);

    /**
     * Adds or updates an attribute for the user.
     * @param user the user entity
     * @param key the attribute key
     * @param value the attribute value
     * @return a new user instance with the updated attribute
     */
    U addAttribute(U user, String key, Object value);

    /**
     * Updates the user's display name.
     * @param user the user entity
     * @param displayName the new display name
     * @return a new user instance with the updated display name
     */
    U updateDisplayName(U user, String displayName);

    /**
     * Optionally removes an attribute from the user.
     * @param user the user entity
     * @param key the attribute key
     * @return a new user instance with the attribute removed, or the same instance if not present
     */
    default U removeAttribute(U user, String key) {
        return user;
    }

    /**
     * Optionally updates multiple attributes for the user.
     * @param user the user entity
     * @param attributes the attributes to update
     * @return a new user instance with the updated attributes
     */
    default U updateAttributes(U user, Map<String, Object> attributes) {
        return user;
    }

    /**
     * Optionally returns the user's attribute by key.
     * @param user the user entity
     * @param key the attribute key
     * @return an Optional containing the attribute value if present
     */
    default Optional<Object> getAttribute(U user, String key) {
        return Optional.empty();
    }
}
