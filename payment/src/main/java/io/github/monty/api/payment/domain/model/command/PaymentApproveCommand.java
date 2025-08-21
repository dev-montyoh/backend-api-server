package io.github.monty.api.payment.domain.model.command;

import io.github.monty.api.payment.common.constants.PaymentGatewayType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentApproveCommand {
    private PaymentGatewayType paymentGatewayType;
    private String paymentNo;
}
