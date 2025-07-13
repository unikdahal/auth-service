package com.unik.auth.application.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Email and password credentials.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailPasswordCredentials {
    private String email;
    private String password;
}
