package com.corhuila.parkkbus.application.command;

import java.util.UUID;

/**
 * Command — CQRS write side.
 * Triggers the ProcessPaymentUseCase.
 */
public record ProcessPaymentCommand(
        UUID sessionId,
        String paymentMethod
) {}

