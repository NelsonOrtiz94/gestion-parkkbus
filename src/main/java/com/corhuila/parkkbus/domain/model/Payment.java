package com.corhuila.parkkbus.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final UUID sessionId;
    private final BigDecimal amount;
    private final String method;
    private final LocalDateTime paymentDate;
    private final String status;

    public Payment(UUID id, UUID sessionId, BigDecimal amount, String method, LocalDateTime paymentDate, String status) {
        this.id = id;
        this.sessionId = sessionId;
        this.amount = amount;
        this.method = method;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public UUID getId() { return id; }
    public UUID getSessionId() { return sessionId; }
    public BigDecimal getAmount() { return amount; }
    public String getMethod() { return method; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
}