package com.corhuila.parkkbus.infrastructure.adapter.in.web;

import com.corhuila.parkkbus.application.usecase.AuthenticateUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authenticateUserUseCase.execute(request.username(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public record AuthRequest(String username, String password) {}
    public record AuthResponse(String token) {}
}