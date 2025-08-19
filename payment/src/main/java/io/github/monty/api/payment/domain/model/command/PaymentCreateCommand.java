package io.github.monty.api.payment.domain.model.command;

import io.github.monty.api.payment.common.constants.PaymentType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentCreateCommand {
    private PaymentType paymentType;

    private long amount;

    private String orderNo;
}
