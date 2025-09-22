package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.aggregate.Payment;

import java.util.Optional;

public interface PaymentCustomRepository {

    /**
     * 결제 데이터 조회
     * 결제 취소 데이터까지 같이 조회
     *
     * @param paymentNo 결제 번호
     * @return 결제 엔티티
     */
    Optional<Payment> findByPaymentNoWithCancelList(String paymentNo);
}
