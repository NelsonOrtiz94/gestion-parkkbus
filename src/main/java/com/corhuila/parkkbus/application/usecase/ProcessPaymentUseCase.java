package com.corhuila.parkkbus.application.usecase;

import com.corhuila.parkkbus.application.command.ProcessPaymentCommand;
import com.corhuila.parkkbus.domain.model.Payment;
import com.corhuila.parkkbus.domain.model.ParkingSession;
import com.corhuila.parkkbus.domain.port.ParkingSessionRepositoryPort;
import com.corhuila.parkkbus.domain.port.PaymentRepositoryPort;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Use Case — Command side (CQRS).
 * Processes payment for a closed parking session.
 */
public class ProcessPaymentUseCase {

    private final ParkingSessionRepositoryPort sessionRepository;
    private final PaymentRepositoryPort paymentRepository;

    public ProcessPaymentUseCase(ParkingSessionRepositoryPort sessionRepository,
                                  PaymentRepositoryPort paymentRepository) {
        this.sessionRepository = sessionRepository;
        this.paymentRepository = paymentRepository;
    }

    public Payment execute(ProcessPaymentCommand command) {
        ParkingSession session = sessionRepository.findActiveByPlate(command.sessionId().toString())
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + command.sessionId()));

        if (session.getTotalAmount() == null) {
            throw new IllegalStateException("Session has no calculated amount. Register exit first.");
        }

        Payment payment = new Payment(
                UUID.randomUUID(),
                session.getId(),
                session.getTotalAmount(),
                command.paymentMethod(),
                LocalDateTime.now(),
                "COMPLETED"
        );

        return paymentRepository.save(payment);
    }
}
