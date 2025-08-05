package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InisysPaymentStrategy implements PaymentStrategy {
    @Override
    public void getAuthInfo() {

    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.INISYS;
    }
}
