package com.corhuila.parkkbus.infrastructure.adapter.in.web;

import com.corhuila.parkkbus.application.dto.OccupancyReportResponse;
import com.corhuila.parkkbus.application.dto.SpotResponse;
import com.corhuila.parkkbus.application.query.GetOccupancyReportQuery;
import com.corhuila.parkkbus.application.usecase.GetAvailableSpotsQueryUseCase;
import com.corhuila.parkkbus.application.usecase.GetOccupancyReportQueryUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final GetAvailableSpotsQueryUseCase availableSpotsUseCase;
    private final GetOccupancyReportQueryUseCase occupancyReportUseCase;

    public ReportController(GetAvailableSpotsQueryUseCase availableSpotsUseCase,
                            GetOccupancyReportQueryUseCase occupancyReportUseCase) {
        this.availableSpotsUseCase = availableSpotsUseCase;
        this.occupancyReportUseCase = occupancyReportUseCase;
    }

    @GetMapping("/spots/available")
    public ResponseEntity<List<SpotResponse>> getAvailableSpots() {
        return ResponseEntity.ok(availableSpotsUseCase.execute());
    }

    @GetMapping("/occupancy")
    public ResponseEntity<OccupancyReportResponse> getOccupancyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(occupancyReportUseCase.execute(new GetOccupancyReportQuery(from, to)));
    }
}
