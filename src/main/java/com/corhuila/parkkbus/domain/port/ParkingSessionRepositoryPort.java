package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.ParkingSession;
import java.util.Optional;
import java.util.UUID;

public interface ParkingSessionRepositoryPort {
    ParkingSession save(ParkingSession session);
    Optional<ParkingSession> findActiveByPlate(String plate);
}