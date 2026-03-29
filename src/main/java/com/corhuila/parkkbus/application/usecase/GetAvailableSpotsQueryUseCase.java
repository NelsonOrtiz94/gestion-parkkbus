package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.application.dto.SpotResponse;
import com.corhuila.parkkbus.domain.port.SpotRepositoryPort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case — Query side (CQRS).
 * Returns available spots without touching the write model.
 */
public class GetAvailableSpotsQueryUseCase {

    private final SpotRepositoryPort spotRepository;

    public GetAvailableSpotsQueryUseCase(SpotRepositoryPort spotRepository) {
        this.spotRepository = spotRepository;
    }

    public List<SpotResponse> execute() {
        return spotRepository.findAvailableSpots().stream()
                .map(s -> new SpotResponse(s.getId().toString(), s.getCode(), s.isAvailable()))
                .collect(Collectors.toList());
    }
}
