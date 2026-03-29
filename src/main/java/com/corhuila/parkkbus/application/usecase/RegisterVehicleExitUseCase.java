package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.application.command.RegisterVehicleExitCommand;
import com.corhuila.parkkbus.application.dto.VehicleExitResponse;
import com.corhuila.parkkbus.domain.model.ParkingSession;
import com.corhuila.parkkbus.domain.model.Tariff;
import com.corhuila.parkkbus.domain.port.ParkingSessionRepositoryPort;
import com.corhuila.parkkbus.domain.port.TariffRepositoryPort;
import com.corhuila.parkkbus.domain.service.TariffCalculatorService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Use Case — Command side (CQRS).
 * Registers vehicle exit, calculates the amount due using the TariffCalculatorService.
 */
public class RegisterVehicleExitUseCase {

    private final ParkingSessionRepositoryPort sessionRepository;
    private final TariffRepositoryPort tariffRepository;
    private final TariffCalculatorService tariffCalculator;

    public RegisterVehicleExitUseCase(ParkingSessionRepositoryPort sessionRepository,
                                      TariffRepositoryPort tariffRepository,
                                      TariffCalculatorService tariffCalculator) {
        this.sessionRepository = sessionRepository;
        this.tariffRepository = tariffRepository;
        this.tariffCalculator = tariffCalculator;
    }

    public VehicleExitResponse execute(RegisterVehicleExitCommand command) {
        ParkingSession session = sessionRepository.findActiveByPlate(command.plate())
                .orElseThrow(() -> new IllegalArgumentException("No active session for plate: " + command.plate()));

        LocalDateTime exitTime = LocalDateTime.now();
        session.closeSession(exitTime, BigDecimal.ZERO); // temporary to set exit time

        Tariff tariff = tariffRepository.findByVehicleType(session.getVehicle().getType())
                .orElseThrow(() -> new IllegalArgumentException("No tariff configured for vehicle type: " + session.getVehicle().getType()));

        BigDecimal amount = tariffCalculator.calculate(session, tariff);
        session.closeSession(exitTime, amount);

        ParkingSession saved = sessionRepository.save(session);

        return new VehicleExitResponse(
                saved.getId(),
                saved.getVehicle().getPlate(),
                saved.getEntryTime(),
                saved.getExitTime(),
                saved.getTotalAmount()
        );
    }
}
