package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.domain.model.ParkingSession;
import com.corhuila.parkkbus.domain.model.Tariff;
import com.corhuila.parkkbus.domain.port.ParkingSessionRepositoryPort;
import com.corhuila.parkkbus.domain.port.TariffRepositoryPort;
import com.corhuila.parkkbus.domain.service.TariffCalculatorService;

import java.math.BigDecimal;

/**
 * Use Case — calculates the tariff for a given active session.
 * Delegates computation to TariffCalculatorService (Strategy pattern).
 */
public class CalculateTariffUseCase {

    private final ParkingSessionRepositoryPort sessionRepository;
    private final TariffRepositoryPort tariffRepository;
    private final TariffCalculatorService tariffCalculator;

    public CalculateTariffUseCase(ParkingSessionRepositoryPort sessionRepository,
                                   TariffRepositoryPort tariffRepository,
                                   TariffCalculatorService tariffCalculator) {
        this.sessionRepository = sessionRepository;
        this.tariffRepository = tariffRepository;
        this.tariffCalculator = tariffCalculator;
    }

    public BigDecimal execute(String plate) {
        ParkingSession session = sessionRepository.findActiveByPlate(plate)
                .orElseThrow(() -> new IllegalArgumentException("No active session for plate: " + plate));

        Tariff tariff = tariffRepository.findByVehicleType(session.getVehicle().getType())
                .orElseThrow(() -> new IllegalArgumentException("No tariff for type: " + session.getVehicle().getType()));

        return tariffCalculator.calculate(session, tariff);
    }
}
