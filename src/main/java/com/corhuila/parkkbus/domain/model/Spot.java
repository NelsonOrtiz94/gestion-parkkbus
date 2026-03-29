package com.corhuila.parkkbus.domain.model;

import java.util.UUID;

public class Spot {
    private final UUID id;
    private final String code;
    private final boolean available;

    public Spot(UUID id, String code, boolean available) {
        this.id = id;
        this.code = code;
        this.available = available;
    }

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public boolean isAvailable() { return available; }
}