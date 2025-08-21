package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.constants.PaymentGatewayType;
import io.github.monty.api.payment.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentServiceFactory {
    private final Map<PaymentGatewayType, PaymentService> paymentServiceMap = new EnumMap<>(PaymentGatewayType.class);

    @Autowired
    public PaymentServiceFactory(List<PaymentService> paymentServiceList) {
        for (PaymentService paymentService : paymentServiceList) {
            paymentServiceMap.put(paymentService.getPaymentType(), paymentService);
        }
    }

    public PaymentService getPaymentService(PaymentGatewayType PaymentGatewayType) {
        if (!paymentServiceMap.containsKey(PaymentGatewayType)) {
            throw new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_SERVICE);
        }
        return paymentServiceMap.get(PaymentGatewayType);
    }
}
