package io.github.monty.api.payment.infrastructure.jpa;

import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends PaymentRepository, JpaRepository<Payment, String> {
}
