package com.corhuila.parkkbus.application.command;

/**
 * Command — CQRS write side.
 * Triggers the RegisterVehicleEntryUseCase.
 */
public record RegisterVehicleEntryCommand(
        String plate,
        String vehicleType
) {}

