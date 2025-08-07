package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.domain.model.query.PaymentAuthInfoQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentAuthInfoVO;
import io.github.monty.api.payment.domain.strategy.PaymentStrategy;
import io.github.monty.api.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentAuthInfoVO getAuthInfo(PaymentAuthInfoQuery paymentAuthInfoQuery) {
        PaymentStrategy paymentService = paymentStrategyFactory.getPaymentService(paymentAuthInfoQuery.getPaymentType());
        return paymentService.getAuthInfo(paymentAuthInfoQuery);
    }
}
