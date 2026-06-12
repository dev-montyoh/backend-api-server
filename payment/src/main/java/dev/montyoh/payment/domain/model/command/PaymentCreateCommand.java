package dev.montyoh.payment.domain.model.command;

import dev.montyoh.payment.common.constants.PgProviderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentCreateCommand {
    private PgProviderType pgProviderType;
    private String orderNo;
    private long price;
    private String transactionId;
}
