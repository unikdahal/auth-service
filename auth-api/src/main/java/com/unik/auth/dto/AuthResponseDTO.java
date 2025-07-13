package com.unik.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "AuthResponseDTO", description = "Response payload containing authentication tokens.")
public class AuthResponseDTO {
    @Schema(description = "JWT access token for API authorization", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh token used to obtain new access tokens", example = "c012b0f9-5a33-4f1b-9f32-...")
    private String refreshToken;

    @Schema(description = "Type of the token (e.g., Bearer)", example = "Bearer")
    private String tokenType;
}
