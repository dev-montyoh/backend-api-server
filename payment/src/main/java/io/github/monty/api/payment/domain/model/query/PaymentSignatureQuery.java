package io.github.monty.api.payment.domain.model.query;

import io.github.monty.api.payment.common.constants.PaymentGatewayType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentSignatureQuery {
    private PaymentGatewayType PaymentGatewayType;
}
