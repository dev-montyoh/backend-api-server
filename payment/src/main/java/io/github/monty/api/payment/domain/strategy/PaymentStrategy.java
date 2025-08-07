package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.PaymentAuthInfoQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentAuthInfoVO;

public interface PaymentStrategy {
    PaymentType getPaymentType();

    PaymentAuthInfoVO getAuthInfo(PaymentAuthInfoQuery paymentAuthInfoQuery);
}
