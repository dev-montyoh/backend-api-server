package dev.montyoh.payment.domain.model.query;

import dev.montyoh.payment.common.constants.PgProviderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentSignatureQuery {
    private PgProviderType pgProviderType;
}
