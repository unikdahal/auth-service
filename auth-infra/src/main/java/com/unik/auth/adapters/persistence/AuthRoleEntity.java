package com.unik.auth.adapters.persistence;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
