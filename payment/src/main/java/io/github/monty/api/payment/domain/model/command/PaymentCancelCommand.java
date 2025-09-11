package io.github.monty.api.payment.domain.model.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentCancelCommand {

    private String paymentNo;
}
