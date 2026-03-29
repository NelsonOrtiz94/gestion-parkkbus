package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.Tariff;
import java.util.Optional;

public interface TariffRepositoryPort {
    Optional<Tariff> findByVehicleType(String vehicleType);
}