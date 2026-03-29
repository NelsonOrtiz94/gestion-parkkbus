package com.corhuila.parkkbus.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO — Response for vehicle exit with amount to pay.
 */
public record VehicleExitResponse(
        UUID sessionId,
        String plate,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        BigDecimal totalAmount
) {}

