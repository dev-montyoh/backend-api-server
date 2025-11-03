package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.entity.PaymentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentLogCustomRepository {

    /**
     * 해당 결제 번호의 결제 로그 목록을 조회한다.
     *
     * @param paymentNo 결제 번호
     * @param pageable  페이징 정보
     * @return 조회 결과
     */
    Page<PaymentLog> findAll(String paymentNo, Pageable pageable);
}
