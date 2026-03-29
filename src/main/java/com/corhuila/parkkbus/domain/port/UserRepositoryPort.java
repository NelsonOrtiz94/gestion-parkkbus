package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
}