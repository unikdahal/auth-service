package com.unik.auth.adapters.security;

import com.unik.auth.ports.output.PasswordServicePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordEncoderImpl implements PasswordServicePort {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String generateRandomPassword(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    @Override
    public boolean isPasswordStrong(String password) {
        return password.length() >= 8;
    }

    @Override
    public boolean isPasswordCompromised(String password) {
        return false;
    }
}
