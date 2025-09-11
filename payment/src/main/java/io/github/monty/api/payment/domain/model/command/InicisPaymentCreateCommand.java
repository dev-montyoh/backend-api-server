package io.github.monty.api.payment.domain.model.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InicisPaymentCreateCommand extends PaymentCreateCommand {
    private String mid;
    private String authToken;
    private String idcCode;
    private String approvalUrl;
    private String networkCancelUrl;
}
