package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.application.dto.OccupancyReportResponse;
import com.corhuila.parkkbus.application.query.GetOccupancyReportQuery;
import com.corhuila.parkkbus.domain.port.ParkingSessionRepositoryPort;

/**
 * Use Case — Query side (CQRS).
 * Generates occupancy report for a date range without affecting the write model.
 */
public class GetOccupancyReportQueryUseCase {

    private final ParkingSessionRepositoryPort sessionRepository;

    public GetOccupancyReportQueryUseCase(ParkingSessionRepositoryPort sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public OccupancyReportResponse execute(GetOccupancyReportQuery query) {
        long totalEntries = sessionRepository.countEntriesBetween(query.from(), query.to());
        long totalExits   = sessionRepository.countExitsBetween(query.from(), query.to());
        var  totalRevenue = sessionRepository.sumRevenueBetween(query.from(), query.to());
        long totalSpots   = sessionRepository.countTotalSpots();
        double occupancyRate = totalSpots > 0 ? (double) totalEntries / totalSpots * 100 : 0;

        String period = query.from() + " / " + query.to();
        return new OccupancyReportResponse(period, totalEntries, totalExits, totalRevenue, occupancyRate);
    }
}
