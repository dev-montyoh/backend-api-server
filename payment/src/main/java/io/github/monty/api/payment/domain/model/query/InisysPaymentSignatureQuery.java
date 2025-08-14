package io.github.monty.api.payment.domain.model.query;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InisysPaymentSignatureQuery extends PaymentSignatureQuery {
    private String oid;
    private String price;
}
