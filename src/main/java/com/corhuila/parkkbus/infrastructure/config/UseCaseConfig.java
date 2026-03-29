package com.corhuila.parkkbus.infrastructure.config;

import com.corhuila.parkkbus.application.usecase.AuthenticateUserUseCase;
import com.corhuila.parkkbus.domain.port.TokenServicePort;
import com.corhuila.parkkbus.domain.port.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepositoryPort userRepositoryPort,
                                                           TokenServicePort tokenServicePort,
                                                           PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCase(userRepositoryPort, tokenServicePort, passwordEncoder);
    }
}