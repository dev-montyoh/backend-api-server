package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.aggregate.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findByPaymentNo(String paymentNo);

    Payment save(Payment payment);

    @EntityGraph(attributePaths = {"paymentCancelList"})
    Page<Payment> findAll(Pageable pageable);
}
