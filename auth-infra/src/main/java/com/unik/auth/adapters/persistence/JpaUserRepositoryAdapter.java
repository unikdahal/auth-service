package com.unik.auth.adapters.persistence;

import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.domain.entities.GenericUser;
import com.unik.auth.domain.valueobjects.Email;
import com.unik.auth.domain.valueobjects.Password;
import com.unik.auth.domain.valueobjects.UserId;
import com.unik.auth.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPA implementation of the UserRepositoryPort using Spring Data JPA.
 * This adapter maps between the domain model (GenericUser) and the persistence model (AuthUserEntity).
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class JpaUserRepositoryAdapter<R> implements UserRepositoryPort<GenericUser<R>, R> {

    private final AuthUserRepository authUserRepository;
    private final AuthRoleRepository authRoleRepository;

    @Override
    public <S extends GenericUser<R>> S save(S user) {
        log.debug("Saving user: {}", user.getUsername());
        AuthUserEntity entity = toEntity(user);
        AuthUserEntity savedEntity = authUserRepository.save(entity);
        return (S) toUser(savedEntity);
    }

    @Override
    public <S extends GenericUser<R>> S update(S user) {
        log.debug("Updating user: {}", user.getUsername());
        if (!authUserRepository.existsById(user.getId().toString())) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }
        AuthUserEntity entity = toEntity(user);
        AuthUserEntity updatedEntity = authUserRepository.save(entity);
        return (S) toUser(updatedEntity);
    }

    @Override
    public Optional<GenericUser<R>> findById(UserId userId) {
        log.debug("Finding user by ID: {}", userId);
        return authUserRepository.findById(userId.toString())
                .map(this::toUser);
    }

    @Override
    public Optional<GenericUser<R>> findByEmail(Email email) {
        log.debug("Finding user by email: {}", email);
        return authUserRepository.findByEmail(email.toString())
                .map(this::toUser);
    }

    @Override
    public Optional<GenericUser<R>> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return authUserRepository.findByUsername(username)
                .map(this::toUser);
    }

    @Override
    public List<GenericUser<R>> findByRole(R role) {
        log.debug("Finding users by role: {}", role);
        // This implementation depends on how roles are stored and retrieved
        // For simplicity, we'll assume role is a String
        String roleName = role.toString();
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findByRole not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public List<GenericUser<R>> findByRoles(List<R> roles) {
        log.debug("Finding users by roles: {}", roles);
        // This implementation depends on how roles are stored and retrieved
        // For simplicity, we'll assume roles are Strings
        List<String> roleNames = roles.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findByRoles not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public List<GenericUser<R>> findAllEnabled() {
        log.debug("Finding all enabled users");
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findAllEnabled not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public List<GenericUser<R>> findAllDisabled() {
        log.debug("Finding all disabled users");
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findAllDisabled not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public List<GenericUser<R>> findAllLocked() {
        log.debug("Finding all locked users");
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findAllLocked not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public List<GenericUser<R>> findUsersCreatedAfter(LocalDateTime date) {
        log.debug("Finding users created after: {}", date);
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findUsersCreatedAfter not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public List<GenericUser<R>> findByAttribute(String key, Object value) {
        log.debug("Finding users by attribute: {} = {}", key, value);
        // This implementation depends on how attributes are stored and retrieved
        // For now, we'll return an empty list
        log.warn("findByAttribute not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public <T> List<GenericUser<R>> findByAttributes(String key, List<T> values) {
        log.debug("Finding users by attributes: {} = {}", key, values);
        // This implementation depends on how attributes are stored and retrieved
        // For now, we'll return an empty list
        log.warn("findByAttributes not fully implemented");
        return Collections.emptyList();
    }

    @Override
    public long count() {
        log.debug("Counting all users");
        return authUserRepository.count();
    }

    @Override
    public long countByRole(R role) {
        log.debug("Counting users by role: {}", role);
        // This implementation depends on how roles are stored and retrieved
        // For now, we'll return 0
        log.warn("countByRole not fully implemented");
        return 0;
    }

    @Override
    public boolean existsByEmail(Email email) {
        log.debug("Checking if user exists by email: {}", email);
        return authUserRepository.existsByEmail(email.toString());
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking if user exists by username: {}", username);
        return authUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsById(UserId userId) {
        log.debug("Checking if user exists by ID: {}", userId);
        return authUserRepository.existsById(userId.toString());
    }

    @Override
    public void deleteById(UserId userId) {
        log.debug("Deleting user by ID: {}", userId);
        authUserRepository.deleteById(userId.toString());
    }

    @Override
    public void delete(GenericUser<R> user) {
        log.debug("Deleting user: {}", user.getUsername());
        authUserRepository.deleteById(user.getId().toString());
    }

    @Override
    public long countEnabled() {
        log.debug("Counting enabled users");
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return 0
        log.warn("countEnabled not fully implemented");
        return 0;
    }

    @Override
    public long countDisabled() {
        log.debug("Counting disabled users");
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return 0
        log.warn("countDisabled not fully implemented");
        return 0;
    }

    @Override
    public List<GenericUser<R>> findAll(int page, int size) {
        log.debug("Finding all users with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return authUserRepository.findAll(pageable).getContent().stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenericUser<R>> findByUsernameContaining(String pattern, int page, int size) {
        log.debug("Finding users by username containing: {}, page={}, size={}", pattern, page, size);
        // This would need to be implemented in the AuthUserRepository
        // For now, we'll return an empty list
        log.warn("findByUsernameContaining not fully implemented");
        return Collections.emptyList();
    }

    /**
     * Maps a domain user to a persistence entity.
     */
    private AuthUserEntity toEntity(GenericUser<R> user) {
        Set<AuthRoleEntity> roleEntities = mapRolesToEntities(user.getRoles());

        return AuthUserEntity.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .username(user.getUsername())
                .passwordHash(user.getPassword())
                .roles(roleEntities)
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .accountExpired(user.isAccountExpired())
                .credentialsExpired(user.isCredentialsExpired())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Maps a persistence entity to a domain user.
     */
    private GenericUser<R> toUser(AuthUserEntity entity) {
        Set<R> roles = mapEntityRolesToDomain(entity.getRoles());

        GenericUser<R> user = GenericUser.<R>builder()
                .id(UserId.of(entity.getId()))
                .email(Email.of(entity.getEmail()))
                .username(entity.getUsername())
                .password(Password.fromEncoded(entity.getPasswordHash()))
                .roles(roles)
                .enabled(entity.isEnabled())
                .locked(entity.isLocked())
                .accountExpired(entity.isAccountExpired())
                .credentialsExpired(entity.isCredentialsExpired())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        // Add any additional attributes if needed

        return user;
    }

    /**
     * Maps domain roles to persistence entities.
     * This implementation depends on how roles are represented in the domain model.
     * For simplicity, we'll assume roles are Strings.
     */
    @SuppressWarnings("unchecked")
    private Set<AuthRoleEntity> mapRolesToEntities(Set<R> roles) {
        if (roles == null) {
            return new HashSet<>();
        }

        return roles.stream()
                .map(role -> {
                    String roleName = role.toString();
                    // Find or create the role entity
                    return authRoleRepository.findByName(roleName)
                            .orElseGet(() -> {
                                log.debug("Creating new role: {}", roleName);
                                AuthRoleEntity newRole = AuthRoleEntity.builder()
                                        .name(roleName)
                                        .description("Auto-created role")
                                        .build();
                                return authRoleRepository.save(newRole);
                            });
                })
                .collect(Collectors.toSet());
    }

    /**
     * Maps persistence role entities to domain roles.
     * This implementation depends on how roles are represented in the domain model.
     * For simplicity, we'll assume roles are Strings.
     */
    @SuppressWarnings("unchecked")
    private Set<R> mapEntityRolesToDomain(Set<AuthRoleEntity> roleEntities) {
        if (roleEntities == null) {
            return new HashSet<>();
        }

        return roleEntities.stream()
                .map(roleEntity -> (R) roleEntity.getName())
                .collect(Collectors.toSet());
    }
}
