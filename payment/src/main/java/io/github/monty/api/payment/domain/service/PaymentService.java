package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.domain.model.query.PaymentInfoQuery;
import io.github.monty.api.payment.domain.strategy.PaymentStrategy;
import io.github.monty.api.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentStrategyFactory paymentStrategyFactory;

    public void getAuthInfo(PaymentInfoQuery paymentInfoQuery) {
        PaymentStrategy paymentService = paymentStrategyFactory.getPaymentService(paymentInfoQuery.paymentType());
        paymentService.getAuthInfo();
    }
}
