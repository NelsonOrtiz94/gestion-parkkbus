package com.corhuila.parkkbus.application.dto;

import java.math.BigDecimal;

/**
 * DTO — Response for occupancy report (CQRS read model).
 */
public record OccupancyReportResponse(
        String period,
        long totalEntries,
        long totalExits,
        BigDecimal totalRevenue,
        double occupancyRate
) {}

