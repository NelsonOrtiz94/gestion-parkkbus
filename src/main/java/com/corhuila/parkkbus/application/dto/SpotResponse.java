package com.corhuila.parkkbus.application.dto;

/**
 * DTO — Response for a single available spot.
 */
public record SpotResponse(
        String id,
        String code,
        boolean available
) {}

