package io.github.monty.api.payment.domain.model.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NicepayPaymentCreateCommand extends PaymentCreateCommand {
    private String authToken;
    private String signature;
    private String nextApprovalUrl;
    private String networkCancelUrl;
}
