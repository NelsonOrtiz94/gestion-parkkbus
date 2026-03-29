package com.corhuila.parkkbus.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingSession {
    private final UUID id;
    private final Vehicle vehicle;
    private final UUID spotId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal totalAmount;
    private String status;

    public ParkingSession(UUID id, Vehicle vehicle, UUID spotId, LocalDateTime entryTime, String status) {
        this.id = id;
        this.vehicle = vehicle;
        this.spotId = spotId;
        this.entryTime = entryTime;
        this.status = status;
    }

    public void closeSession(LocalDateTime exitTime, BigDecimal totalAmount) {
        this.exitTime = exitTime;
        this.totalAmount = totalAmount;
        this.status = "CLOSED";
    }

    public UUID getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public UUID getSpotId() { return spotId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}