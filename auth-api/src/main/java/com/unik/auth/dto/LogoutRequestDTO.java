package com.unik.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(name = "LogoutRequestDTO", description = "Request payload for user logout.")
public class LogoutRequestDTO {

    @NotBlank
    @Schema(description = "Refresh token to invalidate", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
}
