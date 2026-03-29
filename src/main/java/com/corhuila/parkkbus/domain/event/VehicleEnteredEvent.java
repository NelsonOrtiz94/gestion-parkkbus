package com.corhuila.parkkbus.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class VehicleEnteredEvent {
    private final UUID sessionId;
    private final String plate;
    private final String spotCode;
    private final LocalDateTime entryTime;

    public VehicleEnteredEvent(UUID sessionId, String plate, String spotCode, LocalDateTime entryTime) {
        this.sessionId = sessionId;
        this.plate = plate;
        this.spotCode = spotCode;
        this.entryTime = entryTime;
    }

    public UUID getSessionId() { return sessionId; }
    public String getPlate() { return plate; }
    public String getSpotCode() { return spotCode; }
    public LocalDateTime getEntryTime() { return entryTime; }
}

