package com.corhuila.parkkbus.application.query;

/**
 * Query — CQRS read side.
 * Triggers the GetAvailableSpotsQueryUseCase.
 */
public record GetAvailableSpotsQuery(
        String zone
) {}

