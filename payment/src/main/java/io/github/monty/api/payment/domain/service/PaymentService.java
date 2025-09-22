package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.aggregate.PaymentCancel;
import io.github.monty.api.payment.domain.model.query.PaymentListQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentListResultVO;
import io.github.monty.api.payment.domain.repository.PaymentCustomRepository;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import io.github.monty.api.payment.infrastructure.jpa.mapper.PaymentListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentCustomRepository paymentCustomRepository;

    private final PaymentListMapper paymentListMapper;

    /**
     * 결제 데이터 목록 조회
     *
     * @param paymentListQuery 결제 목록 조회 요청 쿼리
     * @return 조회 결과
     */
    public PaymentListResultVO requestPaymentList(PaymentListQuery paymentListQuery) {
        Page<Payment> paymentPage = paymentRepository.findAllByOrderByCreatedAtDesc(paymentListQuery.pageable());
        List<Payment> paymentList = paymentPage.get().toList();
        return paymentListMapper.mapToVo(paymentList, (long) paymentPage.getTotalPages(), paymentPage.getTotalElements());
    }

    /**
     * 결제 취소 가능 여부 조회
     *
     * @param paymentNo 결제 번호
     */
    public void isEnableCancel(String paymentNo) {
        Optional<Payment> paymentOptional = paymentCustomRepository.findByPaymentNoWithCancelList(paymentNo);
        Payment payment = paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
        long totalCancelAmount = payment.getPaymentCancelList().stream()
                .mapToLong(PaymentCancel::getCancelAmount)
                .sum();
        if (payment.getAmount() <= totalCancelAmount) {
            throw new ApplicationException(ErrorCode.ERROR_ALREADY_CANCELLED);
        }
    }
}
