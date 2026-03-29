package com.corhuila.parkkbus.domain.model;

import java.util.Set;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String passwordHash;
    private final Set<Role> roles;
    private final boolean active;

    public User(UUID id, String username, String passwordHash, Set<Role> roles, boolean active) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isActive() {
        return active;
    }
}