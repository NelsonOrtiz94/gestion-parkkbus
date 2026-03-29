package com.corhuila.parkkbus.unit;

import com.corhuila.parkkbus.application.usecase.AuthenticateUserUseCase;
import com.corhuila.parkkbus.domain.model.Role;
import com.corhuila.parkkbus.domain.model.User;
import com.corhuila.parkkbus.domain.port.TokenServicePort;
import com.corhuila.parkkbus.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticateUserUseCaseTest {

    @Test
    void shouldReturnJwtWhenCredentialsAreValid() {
        UserRepositoryPort userRepository = mock(UserRepositoryPort.class);
        TokenServicePort tokenService = mock(TokenServicePort.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        User user = new User(UUID.randomUUID(), "admin", "hashed-password", Set.of(Role.ADMIN), true);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashed-password")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("jwt-token");

        AuthenticateUserUseCase useCase =
                new AuthenticateUserUseCase(userRepository, tokenService, passwordEncoder);

        String result = useCase.execute("admin", "123456");

        assertEquals("jwt-token", result);
    }
}

