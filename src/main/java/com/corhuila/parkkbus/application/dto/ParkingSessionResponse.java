package com.corhuila.parkkbus.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO — Response for vehicle entry registration.
 */
public record ParkingSessionResponse(
        UUID sessionId,
        String plate,
        String spotCode,
        LocalDateTime entryTime,
        String status
) {}

