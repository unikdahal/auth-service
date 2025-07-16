package com.unik.auth.ports.input;

import com.unik.auth.ports.input.dto.request.RegisterUserRequest;
import com.unik.auth.ports.input.dto.response.LogoutResult;
import com.unik.auth.ports.input.dto.response.RegisterUserResult;

/**
 * Input port for user registration operations.
 */
public interface RegisterLogoutUserPort<U, R> {

    RegisterUserResult<U> registerUser(RegisterUserRequest<R> request);

    LogoutResult logoutUser(String accessToken);
}
