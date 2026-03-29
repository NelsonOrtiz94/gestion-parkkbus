package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.application.command.RegisterVehicleEntryCommand;
import com.corhuila.parkkbus.application.dto.ParkingSessionResponse;
import com.corhuila.parkkbus.domain.model.ParkingSession;
import com.corhuila.parkkbus.domain.model.Spot;
import com.corhuila.parkkbus.domain.model.Vehicle;
import com.corhuila.parkkbus.domain.port.ParkingSessionRepositoryPort;
import com.corhuila.parkkbus.domain.port.SpotRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Use Case — Command side (CQRS).
 * Registers a vehicle entry and assigns an available spot.
 */
public class RegisterVehicleEntryUseCase {

    private final ParkingSessionRepositoryPort sessionRepository;
    private final SpotRepositoryPort spotRepository;

    public RegisterVehicleEntryUseCase(ParkingSessionRepositoryPort sessionRepository,
                                       SpotRepositoryPort spotRepository) {
        this.sessionRepository = sessionRepository;
        this.spotRepository = spotRepository;
    }

    public ParkingSessionResponse execute(RegisterVehicleEntryCommand command) {
        List<Spot> availableSpots = spotRepository.findAvailableSpots();
        if (availableSpots.isEmpty()) {
            throw new IllegalStateException("No available spots at the moment");
        }

        Spot assignedSpot = availableSpots.get(0);
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), command.plate(), command.vehicleType());

        ParkingSession session = new ParkingSession(
                UUID.randomUUID(),
                vehicle,
                assignedSpot.getId(),
                LocalDateTime.now(),
                "ACTIVE"
        );

        ParkingSession saved = sessionRepository.save(session);

        return new ParkingSessionResponse(
                saved.getId(),
                saved.getVehicle().getPlate(),
                assignedSpot.getCode(),
                saved.getEntryTime(),
                saved.getStatus()
        );
    }
}
