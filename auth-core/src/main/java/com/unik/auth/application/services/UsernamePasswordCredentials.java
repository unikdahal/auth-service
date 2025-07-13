package com.unik.auth.application.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Username and password credentials.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordCredentials {
    private String username;
    private String password;
}
