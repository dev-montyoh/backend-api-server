package io.github.monty.api.payment.domain.model.query;

import io.github.monty.api.payment.common.constants.PaymentType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentSignatureQuery {
    private PaymentType paymentType;
}
