package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureVO;
import io.github.monty.api.payment.domain.strategy.PaymentStrategy;
import io.github.monty.api.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentSignatureVO getSignature(PaymentSignatureQuery paymentSignatureQuery) {
        PaymentStrategy paymentService = paymentStrategyFactory.getPaymentService(paymentSignatureQuery.getPaymentType());
        return paymentService.getSignature(paymentSignatureQuery);
    }
}
