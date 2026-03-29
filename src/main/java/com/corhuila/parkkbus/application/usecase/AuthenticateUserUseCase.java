package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.domain.model.User;
import com.corhuila.parkkbus.domain.port.TokenServicePort;
import com.corhuila.parkkbus.domain.port.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final TokenServicePort tokenServicePort;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUserUseCase(UserRepositoryPort userRepositoryPort,
                                   TokenServicePort tokenServicePort,
                                   PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.tokenServicePort = tokenServicePort;
        this.passwordEncoder = passwordEncoder;
    }

    public String execute(String username, String rawPassword) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return tokenServicePort.generateToken(user);
    }
}