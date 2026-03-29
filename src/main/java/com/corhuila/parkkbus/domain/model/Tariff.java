package com.corhuila.parkkbus.domain.model;

import java.math.BigDecimal;

public class Tariff {
    private final String vehicleType;
    private final BigDecimal hourlyRate;

    public Tariff(String vehicleType, BigDecimal hourlyRate) {
        this.vehicleType = vehicleType;
        this.hourlyRate = hourlyRate;
    }

    public String getVehicleType() { return vehicleType; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
}