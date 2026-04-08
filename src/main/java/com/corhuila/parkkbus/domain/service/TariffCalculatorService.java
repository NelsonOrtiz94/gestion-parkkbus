package com.corhuila.parkkbus.domain.service;

import com.corhuila.parkkbus.domain.model.ParkingSession;
import com.corhuila.parkkbus.domain.model.Tariff;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

/**
 * Domain Service — Strategy pattern for tariff calculation.
 * Pure domain logic, no framework dependencies.
 */
public class TariffCalculatorService {

    /**
     * Calculates the total amount for a parking session based on the applicable tariff.
     * Implements the Strategy pattern: each tariff type defines its own rate, but the calculation logic is centralized here.
     */
    public BigDecimal calculate(ParkingSession session, Tariff tariff) {
        if (session.getExitTime() == null) {
            throw new IllegalStateException("Cannot calculate tariff: session has no exit time");
        }

        Duration duration = Duration.between(session.getEntryTime(), session.getExitTime());
        long minutes = duration.toMinutes();

        // Minimum billing: 1 hour
        long billedHours = Math.max(1, (long) Math.ceil(minutes / 60.0));

        return tariff.getHourlyRate()
                .multiply(BigDecimal.valueOf(billedHours))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

