package com.unik.auth.controllers;

import com.unik.auth.application.services.GenericAuthenticationService;
import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.dto.AuthResponseDTO;
import com.unik.auth.dto.LoginRequestDTO;
import com.unik.auth.dto.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user registration and login.")
@RestController
@RequestMapping("${api.auth.base-path:/api/auth}")
@RequiredArgsConstructor
public class AuthController<U extends BaseUser<R>, R> {
    private final GenericAuthenticationService<U, R> authService;

    @Value("${api.auth.token-type:Bearer}")
    private String tokenType;

    @Operation(summary = "Register a new user", description = "Registers a new user and returns access and refresh tokens.", responses = {@ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Invalid input or registration error", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class)))})
    @PostMapping("${api.auth.register-path:/register}")
    public ResponseEntity<AuthResponseDTO> register(@Validated @RequestBody RegisterRequestDTO<R> request) {
        var regReq = new com.unik.auth.application.services.RegistrationRequest<R>(request.getEmail(), request.getUsername(), request.getPassword(), request.getRoles(), null);
        var result = authService.registerUser(regReq);
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(new AuthResponseDTO(null, null, "error: " + result.getMessage()));
        }
        return ResponseEntity.ok(new AuthResponseDTO(result.getAccessToken(), result.getRefreshToken(), tokenType));
    }

    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns access and refresh tokens.", responses = {@ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class)))})
    @PostMapping("${api.auth.login-path:/login}")
    public ResponseEntity<AuthResponseDTO> login(@Validated @RequestBody LoginRequestDTO request) {
        var credentials = new com.unik.auth.application.services.UsernamePasswordCredentials(request.getUsernameOrEmail(), request.getPassword());
        var result = authService.authenticate(credentials);
        if (!result.isSuccess()) {
            return ResponseEntity.status(401).body(new AuthResponseDTO(null, null, "error: " + result.getMessage()));
        }
        return ResponseEntity.ok(new AuthResponseDTO(result.getAccessToken(), result.getRefreshToken(), tokenType));
    }
}
