package com.unik.auth.ports.input;

import com.unik.auth.application.services.AuthenticationResult;
import com.unik.auth.domain.entities.BaseUser;

/**
 * Input port for user authentication operations.
 */
public interface AuthenticateUserPort<U extends BaseUser<?>, R> {

    <C> AuthenticationResult<U> authenticate(C credentials);

    AuthenticationResult<U> authenticateWithUsernamePassword(String username, String password);

    AuthenticationResult<U> authenticateWithEmail(String email, String password);

}
