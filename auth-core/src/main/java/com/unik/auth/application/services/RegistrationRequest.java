package com.unik.auth.application.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * Generic registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest<R> {
    private String email;
    private String username;
    private String password;
    private Set<R> roles;
    private Map<String, Object> attributes;
}
