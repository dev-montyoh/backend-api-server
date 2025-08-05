package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {
    private final Map<PaymentType, PaymentStrategy> paymentServiceMap = new HashMap<>();

    @Autowired
    public PaymentStrategyFactory(List<PaymentStrategy> paymentStrategies) {
        for (PaymentStrategy paymentStrategy : paymentStrategies) {
            paymentServiceMap.put(paymentStrategy.getPaymentType(), paymentStrategy);
        }
    }

    public PaymentStrategy getPaymentService(PaymentType paymentType) {
        if (!paymentServiceMap.containsKey(paymentType)) {
            throw new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_SERVICE);
        }
        return paymentServiceMap.get(paymentType);
    }
}
