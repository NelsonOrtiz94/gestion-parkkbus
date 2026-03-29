package com.corhuila.parkkbus.domain.model;

import java.util.UUID;

public class Vehicle {
    private final UUID id;
    private final String plate;
    private final String type;

    public Vehicle(UUID id, String plate, String type) {
        this.id = id;
        this.plate = plate;
        this.type = type;
    }

    public UUID getId() { return id; }
    public String getPlate() { return plate; }
    public String getType() { return type; }
}