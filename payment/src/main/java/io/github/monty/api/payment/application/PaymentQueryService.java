package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.query.PaymentListQuery;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentListResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureResultVO;
import io.github.monty.api.payment.domain.service.PaymentService;
import io.github.monty.api.payment.domain.strategy.PaymentStrategy;
import io.github.monty.api.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentService paymentService;

    /**
     * 결제 Signature 생성
     *
     * @param paymentSignatureQuery 결제 Signature 생성 요청 쿼리
     * @return 생성 결과
     */
    public PaymentSignatureResultVO requestPaymentSignature(PaymentSignatureQuery paymentSignatureQuery) {
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentSignatureQuery.getPaymentServiceProviderType());
        return paymentStrategy.getSignature(paymentSignatureQuery);
    }

    /**
     * 결제 목록 조회
     *
     * @param paymentListQuery 결제 목록 조회 쿼리
     * @return 결제 목록 조회 결과
     */
    public PaymentListResultVO requestPaymentList(PaymentListQuery paymentListQuery) {
        return paymentService.requestPaymentList(paymentListQuery);
    }
}
