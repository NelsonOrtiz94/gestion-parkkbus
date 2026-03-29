package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.Payment;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
}