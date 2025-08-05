package io.github.monty.api.payment.domain.model.query;

import io.github.monty.api.payment.common.constants.PaymentType;
import lombok.Builder;

@Builder
public record PaymentInfoQuery(String oid, String price, PaymentType paymentType) {
}
