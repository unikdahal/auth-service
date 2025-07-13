package com.unik.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(name = "LoginRequestDTO", description = "Request payload for user login.")
public class LoginRequestDTO {
    @Schema(description = "Username or email for login", example = "user1", required = true)
    @NotBlank
    private String usernameOrEmail;

    @Schema(description = "User password", example = "StrongP@ssw0rd!", required = true)
    @NotBlank
    private String password;
}
