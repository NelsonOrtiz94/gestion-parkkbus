package com.corhuila.parkkbus.application.query;

import java.time.LocalDate;

/**
 * Query — CQRS read side.
 * Triggers the GetOccupancyReportQueryUseCase.
 */
public record GetOccupancyReportQuery(
        LocalDate from,
        LocalDate to
) {}

