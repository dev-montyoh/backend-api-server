package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.aggregate.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findByPaymentNo(String paymentNo);
    Payment save(Payment payment);
}
