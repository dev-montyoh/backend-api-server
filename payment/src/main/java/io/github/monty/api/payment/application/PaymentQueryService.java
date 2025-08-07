package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.query.PaymentAuthInfoQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentAuthInfoVO;
import io.github.monty.api.payment.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentService paymentService;

    public PaymentAuthInfoVO requestPaymentInfo(PaymentAuthInfoQuery paymentAuthInfoQuery) {
        return paymentService.getAuthInfo(paymentAuthInfoQuery);
    }
}
