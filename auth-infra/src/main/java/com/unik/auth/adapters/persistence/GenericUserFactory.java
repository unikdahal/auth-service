package com.unik.auth.adapters.persistence;

import com.unik.auth.domain.entities.GenericUser;
import com.unik.auth.domain.valueobjects.Email;
import com.unik.auth.domain.valueobjects.Password;
import com.unik.auth.domain.valueobjects.UserId;
import com.unik.auth.ports.output.UserFactoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the UserFactoryPort for GenericUser entities.
 * This factory creates and updates GenericUser domain objects in a type-safe, immutable way.
 */
@Slf4j
@Component
public class GenericUserFactory<R> implements UserFactoryPort<GenericUser<R>, R> {

    @Override
    public GenericUser<R> createUser(Email email, String username, Password password, Set<R> roles, Map<String, Object> attributes) {
        log.debug("Creating new user with username: {}, email: {}", username, email);
        
        GenericUser<R> user = GenericUser.<R>builder()
                .id(UserId.generate())
                .email(email)
                .username(username)
                .password(password)
                .roles(roles != null ? new HashSet<>(roles) : new HashSet<>())
                .enabled(true)
                .locked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .attributes(attributes != null ? new HashMap<>(attributes) : new HashMap<>())
                .build();
        
        return user;
    }

    @Override
    public GenericUser<R> updatePassword(GenericUser<R> user, Password newPassword) {
        log.debug("Updating password for user: {}", user.getUsername());
        return user.toBuilder()
                .password(newPassword)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> enableUser(GenericUser<R> user) {
        log.debug("Enabling user: {}", user.getUsername());
        return user.toBuilder()
                .enabled(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> disableUser(GenericUser<R> user) {
        log.debug("Disabling user: {}", user.getUsername());
        return user.toBuilder()
                .enabled(false)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> lockUser(GenericUser<R> user) {
        log.debug("Locking user: {}", user.getUsername());
        return user.toBuilder()
                .locked(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> unlockUser(GenericUser<R> user) {
        log.debug("Unlocking user: {}", user.getUsername());
        return user.toBuilder()
                .locked(false)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> expireAccount(GenericUser<R> user) {
        log.debug("Expiring account for user: {}", user.getUsername());
        return user.toBuilder()
                .accountExpired(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> expireCredentials(GenericUser<R> user) {
        log.debug("Expiring credentials for user: {}", user.getUsername());
        return user.toBuilder()
                .credentialsExpired(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> addRole(GenericUser<R> user, R role) {
        log.debug("Adding role {} to user: {}", role, user.getUsername());
        Set<R> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.add(role);
        return user.toBuilder()
                .roles(updatedRoles)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> removeRole(GenericUser<R> user, R role) {
        log.debug("Removing role {} from user: {}", role, user.getUsername());
        Set<R> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.remove(role);
        return user.toBuilder()
                .roles(updatedRoles)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> addAttribute(GenericUser<R> user, String key, Object value) {
        log.debug("Adding attribute {} to user: {}", key, user.getUsername());
        Map<String, Object> updatedAttributes = new HashMap<>(user.getAttributes());
        updatedAttributes.put(key, value);
        return user.toBuilder()
                .attributes(updatedAttributes)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> updateDisplayName(GenericUser<R> user, String displayName) {
        log.debug("Updating display name for user: {}", user.getUsername());
        return user.toBuilder()
                .displayName(displayName)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> removeAttribute(GenericUser<R> user, String key) {
        log.debug("Removing attribute {} from user: {}", key, user.getUsername());
        Map<String, Object> updatedAttributes = new HashMap<>(user.getAttributes());
        updatedAttributes.remove(key);
        return user.toBuilder()
                .attributes(updatedAttributes)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public GenericUser<R> updateAttributes(GenericUser<R> user, Map<String, Object> attributes) {
        log.debug("Updating attributes for user: {}", user.getUsername());
        Map<String, Object> updatedAttributes = new HashMap<>(user.getAttributes());
        updatedAttributes.putAll(attributes);
        return user.toBuilder()
                .attributes(updatedAttributes)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Object> getAttribute(GenericUser<R> user, String key) {
        return Optional.ofNullable(user.getAttributes().get(key));
    }
}