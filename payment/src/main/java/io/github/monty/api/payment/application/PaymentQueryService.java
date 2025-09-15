package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureResultVO;
import io.github.monty.api.payment.domain.service.PaymentService;
import io.github.monty.api.payment.domain.service.PaymentServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentServiceFactory paymentServiceFactory;

    /**
     * 결제 Signature 생성
     *
     * @param paymentSignatureQuery 결제 Signature 생성 요청 쿼리
     * @return 생성 결과
     */
    public PaymentSignatureResultVO requestPaymentSignature(PaymentSignatureQuery paymentSignatureQuery) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentSignatureQuery.getPaymentServiceProviderType());
        return paymentService.getSignature(paymentSignatureQuery);
    }
}
