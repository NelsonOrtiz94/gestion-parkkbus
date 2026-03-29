package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.ParkingSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ParkingSessionRepositoryPort {
    ParkingSession save(ParkingSession session);
    Optional<ParkingSession> findActiveByPlate(String plate);

    // Query side — reporting (CQRS read model)
    long countEntriesBetween(LocalDate from, LocalDate to);
    long countExitsBetween(LocalDate from, LocalDate to);
    BigDecimal sumRevenueBetween(LocalDate from, LocalDate to);
    long countTotalSpots();
}