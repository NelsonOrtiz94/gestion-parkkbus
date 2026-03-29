package com.corhuila.parkkbus.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentProcessedEvent {
    private final UUID paymentId;
    private final UUID sessionId;
    private final BigDecimal amount;
    private final LocalDateTime processedAt;

    public PaymentProcessedEvent(UUID paymentId, UUID sessionId, BigDecimal amount, LocalDateTime processedAt) {
        this.paymentId = paymentId;
        this.sessionId = sessionId;
        this.amount = amount;
        this.processedAt = processedAt;
    }

    public UUID getPaymentId() { return paymentId; }
    public UUID getSessionId() { return sessionId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}

