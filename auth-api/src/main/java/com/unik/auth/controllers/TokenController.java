package com.unik.auth.controllers;

import com.unik.auth.application.services.GenericAuthenticationService;
import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.dto.TokenRefreshRequestDTO;
import com.unik.auth.dto.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.token.base-path:/api/token}")
@RequiredArgsConstructor
@Tag(name = "Token", description = "Endpoints for token refresh and management.")
public class TokenController<U extends BaseUser<R>, R> {
    private final GenericAuthenticationService<U, R> authService;

    @Value("${api.auth.token-type:Bearer}")
    private String tokenType;

    @Operation(summary = "Refresh access token", description = "Refreshes the access token using a valid refresh token.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
                content = @Content(schema = @Schema(implementation = AuthResponseDTO.class)))
        })
    @PostMapping("${api.token.refresh-path:/refresh}")
    public ResponseEntity<AuthResponseDTO> refresh(@Validated @RequestBody TokenRefreshRequestDTO request) {
        var result = authService.refreshToken(request.getRefreshToken());
        if (!result.isSuccess()) {
            return ResponseEntity.status(401).body(new AuthResponseDTO(null, null, "error: " + result.getMessage()));
        }
        return ResponseEntity.ok(new AuthResponseDTO(result.getAccessToken(), request.getRefreshToken(), tokenType));
    }
}
