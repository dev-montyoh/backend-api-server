package io.github.monty.api.payment.domain.model.command;

import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentCreateCommand {
    private PaymentServiceProviderType paymentServiceProviderType;
    private String orderNo;
    private long price;
    private String transactionId;
}
