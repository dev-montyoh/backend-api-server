package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.query.PaymentListQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentListResultVO;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import io.github.monty.api.payment.infrastructure.jpa.mapper.PaymentListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private final PaymentListMapper paymentListMapper;

    /**
     * 결제 데이터 목록 조회
     *
     * @param paymentListQuery 결제 목록 조회 요청 쿼리
     * @return 조회 결과
     */
    public PaymentListResultVO requestPaymentList(PaymentListQuery paymentListQuery) {
        Page<Payment> paymentPage = paymentRepository.findAll(paymentListQuery.pageable());
        List<Payment> paymentList = paymentPage.get().toList();
        return paymentListMapper.mapToVo(paymentList, (long) paymentPage.getTotalPages(), paymentPage.getTotalElements());
    }
}
