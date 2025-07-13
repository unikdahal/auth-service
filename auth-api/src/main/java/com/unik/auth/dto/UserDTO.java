package com.unik.auth.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String email;
    private String username;
    private Set<String> roles;
    private boolean enabled;
}
