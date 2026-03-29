package com.corhuila.parkkbus.infrastructure.adapter.out.security;

import com.corhuila.parkkbus.domain.model.User;
import com.corhuila.parkkbus.domain.port.TokenServicePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Outbound adapter — JWT token generation and validation.
 * Implements TokenServicePort (Hexagonal: domain port → infrastructure adapter).
 */
@Component
public class JwtTokenAdapter implements TokenServicePort {

    private static final long EXPIRATION_MS = 86_400_000; // 24 hours

    private final Key signingKey;

    public JwtTokenAdapter(@Value("${security.jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {
        String roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", roles)
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(signingKey.getEncoded()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


