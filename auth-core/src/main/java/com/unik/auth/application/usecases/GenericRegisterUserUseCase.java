package com.unik.auth.application.usecases;

import com.unik.auth.application.services.GenericAuthenticationService;
import com.unik.auth.application.services.RegistrationRequest;
import com.unik.auth.application.services.AuthenticationResult;
import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.ports.input.RegisterUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic use case for user registration.
 */
@Slf4j
@RequiredArgsConstructor
public class GenericRegisterUserUseCase<U extends BaseUser<R>, R> implements RegisterUserPort<U, R> {

    private final GenericAuthenticationService<U, R> authService;

    @Override
    public RegisterUserResult<U> registerUser(RegisterUserRequest<R> request) {
        try {
            log.info("Processing registration request for user: {}", request.getUsername());

            RegistrationRequest<R> registrationRequest = new RegistrationRequest<>(
                    request.getEmail(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getRoles(),
                    request.getAttributes()
            );

            AuthenticationResult<U> result = authService.registerUser(registrationRequest);

            if (result.isSuccess()) {
                return RegisterUserResult.success(
                        result.getUser(),
                        result.getAccessToken(),
                        result.getRefreshToken(),
                        result.getMessage()
                );
            } else {
                return RegisterUserResult.failure(result.getMessage());
            }

        } catch (Exception e) {
            log.error("Registration failed for user: {}", request.getUsername(), e);
            return RegisterUserResult.failure("Registration failed: " + e.getMessage());
        }
    }
}
