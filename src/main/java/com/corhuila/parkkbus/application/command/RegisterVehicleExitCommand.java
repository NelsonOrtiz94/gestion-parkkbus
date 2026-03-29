package com.corhuila.parkkbus.application.command;

/**
 * Command — CQRS write side.
 * Triggers the RegisterVehicleExitUseCase.
 */
public record RegisterVehicleExitCommand(
        String plate
) {}

