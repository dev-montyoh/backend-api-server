package dev.montyoh.payment.infrastructure.jpa;

import dev.montyoh.payment.domain.model.aggregate.Payment;
import dev.montyoh.payment.domain.repository.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends PaymentRepository, JpaRepository<Payment, String> {
}
