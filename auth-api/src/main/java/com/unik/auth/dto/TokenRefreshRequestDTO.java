package com.unik.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(name = "TokenRefreshRequestDTO", description = "Request payload for refreshing an access token.")
public class TokenRefreshRequestDTO {
    @NotBlank
    @Schema(description = "The refresh token from a previous authentication", example = "c012b0f9-5a33-4f1b-9f32-...", required = true)
    private String refreshToken;
}
