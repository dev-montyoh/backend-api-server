package dev.montyoh.payment.domain.model.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InicisPaymentCreateCommand extends PaymentCreateCommand {
    private String authToken;
    private String idcCode;
    private String approvalUrl;
    private String networkCancelUrl;
}
