package com.unik.auth.ports.input.dto.request;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest<R> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NonNull
    private String email;
    @NonNull private String username;
    @NonNull private String password;
    @Builder.Default private Set<R> roles = Set.of();
    @Builder.Default private Map<String, Object> attributes = Map.of();
}