package com.corhuila.parkkbus.infrastructure.config;

import com.corhuila.parkkbus.application.usecase.*;
import com.corhuila.parkkbus.domain.port.*;
import com.corhuila.parkkbus.domain.service.TariffCalculatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public TariffCalculatorService tariffCalculatorService() {
        return new TariffCalculatorService();
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepositoryPort userRepositoryPort,
                                                           TokenServicePort tokenServicePort,
                                                           PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCase(userRepositoryPort, tokenServicePort, passwordEncoder);
    }

    @Bean
    public RegisterVehicleEntryUseCase registerVehicleEntryUseCase(ParkingSessionRepositoryPort sessionRepository,
                                                                    SpotRepositoryPort spotRepository) {
        return new RegisterVehicleEntryUseCase(sessionRepository, spotRepository);
    }

    @Bean
    public RegisterVehicleExitUseCase registerVehicleExitUseCase(ParkingSessionRepositoryPort sessionRepository,
                                                                  TariffRepositoryPort tariffRepository,
                                                                  TariffCalculatorService tariffCalculator) {
        return new RegisterVehicleExitUseCase(sessionRepository, tariffRepository, tariffCalculator);
    }

    @Bean
    public CalculateTariffUseCase calculateTariffUseCase(ParkingSessionRepositoryPort sessionRepository,
                                                          TariffRepositoryPort tariffRepository,
                                                          TariffCalculatorService tariffCalculator) {
        return new CalculateTariffUseCase(sessionRepository, tariffRepository, tariffCalculator);
    }

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(ParkingSessionRepositoryPort sessionRepository,
                                                        PaymentRepositoryPort paymentRepository) {
        return new ProcessPaymentUseCase(sessionRepository, paymentRepository);
    }

    @Bean
    public GetAvailableSpotsQueryUseCase getAvailableSpotsQueryUseCase(SpotRepositoryPort spotRepository) {
        return new GetAvailableSpotsQueryUseCase(spotRepository);
    }

    @Bean
    public GetOccupancyReportQueryUseCase getOccupancyReportQueryUseCase(ParkingSessionRepositoryPort sessionRepository) {
        return new GetOccupancyReportQueryUseCase(sessionRepository);
    }
}