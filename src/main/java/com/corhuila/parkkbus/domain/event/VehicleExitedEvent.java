package com.corhuila.parkkbus.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class VehicleExitedEvent {
    private final UUID sessionId;
    private final String plate;
    private final LocalDateTime exitTime;
    private final BigDecimal totalAmount;

    public VehicleExitedEvent(UUID sessionId, String plate, LocalDateTime exitTime, BigDecimal totalAmount) {
        this.sessionId = sessionId;
        this.plate = plate;
        this.exitTime = exitTime;
        this.totalAmount = totalAmount;
    }

    public UUID getSessionId() { return sessionId; }
    public String getPlate() { return plate; }
    public LocalDateTime getExitTime() { return exitTime; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}

