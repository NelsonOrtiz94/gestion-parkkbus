package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.User;

public interface TokenServicePort {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isValid(String token);
}