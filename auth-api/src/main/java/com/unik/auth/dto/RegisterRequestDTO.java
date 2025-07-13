package com.unik.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.Set;

@Data
@Schema(name = "RegisterRequestDTO", description = "Request payload for user registration.")
public class RegisterRequestDTO<R> {

    @Schema(description = "User email address", example = "user@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Username for the user", example = "user1")
    @NotBlank
    private String username;

    @Schema(description = "User password", example = "StrongP@ssw0rd!")
    @NotBlank
    private String password;

    @Schema(description = "Set of roles for the user", example = "[\"USER\", \"ADMIN\"]")
    private Set<R> roles;
}
