package com.unik.auth.ports.output;

import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.domain.valueobjects.Email;
import com.unik.auth.domain.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Generic user repository port supporting any user and role type.
 */
public interface UserRepositoryPort<U extends BaseUser<R>, R> {

    /**
     * Saves a new user or updates an existing user.
     * @param user the user to save or update
     * @param <S> the type of the user
     * @return the saved or updated user
     */
    <S extends U> S save(S user);

    /**
     * Updates an existing user.
     * @param user the user to update
     * @param <S> the type of the user
     * @return the updated user
     */
    <S extends U> S update(S user);

    /**
     * Finds a user by ID.
     * @param userId the ID of the user to find
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<U> findById(UserId userId);

    /**
     * Finds a user by email.
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<U> findByEmail(Email email);

    /**
     * Finds a user by username.
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<U> findByUsername(String username);

    /**
     * Finds users by a specific role.
     * @param role the role to search for
     * @return a list of users with the given role
     */
    List<U> findByRole(R role);

    /**
     * Finds users by a list of roles.
     * @param roles the roles to search for
     * @return a list of users with any of the given roles
     */
    List<U> findByRoles(List<R> roles);

    /**
     * Finds all enabled users.
     * @return a list of enabled users
     */
    List<U> findAllEnabled();

    /**
     * Finds all disabled users.
     * @return a list of disabled users
     */
    List<U> findAllDisabled();

    /**
     * Finds all locked users.
     * @return a list of locked users
     */
    List<U> findAllLocked();

    /**
     * Finds users created after a specific date.
     * @param date the date to compare
     * @return a list of users created after the given date
     */
    List<U> findUsersCreatedAfter(LocalDateTime date);

    /**
     * Finds users by a specific attribute key and value.
     * @param key the attribute key
     * @param value the attribute value
     * @return a list of users matching the attribute
     */
    List<U> findByAttribute(String key, Object value);

    /**
     * Finds users by a specific attribute key and a list of values.
     * @param key the attribute key
     * @param values the list of attribute values
     * @param <T> the type of the attribute values
     * @return a list of users matching any of the attribute values
     */
    <T> List<U> findByAttributes(String key, List<T> values);

    /**
     * Returns the total number of users.
     * @return the user count
     */
    long count();

    /**
     * Returns the number of users with a specific role.
     * @param role the role to count
     * @return the user count for the role
     */
    long countByRole(R role);

    /**
     * Checks if a user exists by email.
     * @param email the email to check
     * @return true if a user exists with the email, false otherwise
     */
    boolean existsByEmail(Email email);

    /**
     * Checks if a user exists by username.
     * @param username the username to check
     * @return true if a user exists with the username, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by user ID.
     * @param userId the user ID to check
     * @return true if a user exists with the ID, false otherwise
     */
    boolean existsById(UserId userId);

    /**
     * Deletes a user by user ID.
     * @param userId the user ID to delete
     */
    void deleteById(UserId userId);

    /**
     * Deletes a user entity.
     * @param user the user to delete
     */
    void delete(U user);

    /**
     * Returns the number of enabled users.
     * @return the enabled user count
     */
    long countEnabled();

    /**
     * Returns the number of disabled users.
     * @return the disabled user count
     */
    long countDisabled();

    /**
     * Finds all users with pagination.
     * @param page the page number (0-based)
     * @param size the page size
     * @return a list of users for the page
     */
    List<U> findAll(int page, int size);

    /**
     * Finds users by username pattern with pagination.
     * @param pattern the username pattern (e.g., SQL LIKE)
     * @param page the page number (0-based)
     * @param size the page size
     * @return a list of users matching the pattern for the page
     */
    List<U> findByUsernameContaining(String pattern, int page, int size);
}
