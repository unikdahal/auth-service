package com.unik.auth.adapters.persistence;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "auth_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "auth_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<AuthRoleEntity> roles;

    private boolean enabled;
    private boolean locked;
    private boolean accountExpired;
    private boolean credentialsExpired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
