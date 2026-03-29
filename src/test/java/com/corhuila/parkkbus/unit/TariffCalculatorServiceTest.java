package com.corhuila.parkkbus.unit;

import com.corhuila.parkkbus.domain.model.ParkingSession;
import com.corhuila.parkkbus.domain.model.Tariff;
import com.corhuila.parkkbus.domain.model.Vehicle;
import com.corhuila.parkkbus.domain.service.TariffCalculatorService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TariffCalculatorServiceTest {

    private final TariffCalculatorService calculator = new TariffCalculatorService();

    @Test
    void shouldCalculateOneHourMinimumForShortStay() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "ABC-123", "CAR");
        ParkingSession session = new ParkingSession(
                UUID.randomUUID(), vehicle, UUID.randomUUID(),
                LocalDateTime.of(2026, 3, 28, 10, 0), "ACTIVE"
        );
        // 30 minutes stay → billed as 1 hour minimum
        session.closeSession(LocalDateTime.of(2026, 3, 28, 10, 30), BigDecimal.ZERO);

        Tariff tariff = new Tariff("CAR", new BigDecimal("3000.00"));

        BigDecimal result = calculator.calculate(session, tariff);

        assertEquals(new BigDecimal("3000.00"), result);
    }

    @Test
    void shouldCalculateTwoHoursForNinetyMinutesStay() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "XYZ-999", "CAR");
        ParkingSession session = new ParkingSession(
                UUID.randomUUID(), vehicle, UUID.randomUUID(),
                LocalDateTime.of(2026, 3, 28, 8, 0), "ACTIVE"
        );
        // 90 minutes → ceil to 2 hours
        session.closeSession(LocalDateTime.of(2026, 3, 28, 9, 30), BigDecimal.ZERO);

        Tariff tariff = new Tariff("CAR", new BigDecimal("3000.00"));

        BigDecimal result = calculator.calculate(session, tariff);

        assertEquals(new BigDecimal("6000.00"), result);
    }

    @Test
    void shouldThrowWhenSessionHasNoExitTime() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "ABC-123", "CAR");
        ParkingSession session = new ParkingSession(
                UUID.randomUUID(), vehicle, UUID.randomUUID(),
                LocalDateTime.now(), "ACTIVE"
        );
        Tariff tariff = new Tariff("CAR", new BigDecimal("3000.00"));

        assertThrows(IllegalStateException.class, () -> calculator.calculate(session, tariff));
    }
}

