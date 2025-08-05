package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.PaymentType;

public interface PaymentStrategy {

    void getAuthInfo();

    PaymentType getPaymentType();
}
