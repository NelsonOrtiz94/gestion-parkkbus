package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.Tariff;

import java.util.List;
import java.util.Optional;

public interface TariffRepositoryPort {
    List<Tariff> findAll();
    Optional<Tariff> findByVehicleType(String vehicleType);
}