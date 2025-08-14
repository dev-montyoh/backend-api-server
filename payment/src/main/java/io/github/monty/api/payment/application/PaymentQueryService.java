package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureVO;
import io.github.monty.api.payment.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentService paymentService;

    public PaymentSignatureVO requestPaymentSignature(PaymentSignatureQuery paymentSignatureQuery) {
        return paymentService.getSignature(paymentSignatureQuery);
    }
}
