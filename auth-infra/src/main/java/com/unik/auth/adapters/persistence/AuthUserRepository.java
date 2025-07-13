package com.unik.auth.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserEntity, String> {
    Optional<AuthUserEntity> findByEmail(String email);
    Optional<AuthUserEntity> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
