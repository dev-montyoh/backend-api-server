package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.query.PaymentInfoQuery;
import io.github.monty.api.payment.interfaces.rest.dto.PaymentAuthInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    public PaymentAuthInfoResDto requestPaymentInfo(PaymentInfoQuery paymentInfoQuery) {
        return null;
    }
}
