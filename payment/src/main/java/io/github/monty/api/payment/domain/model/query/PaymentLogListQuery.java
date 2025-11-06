package io.github.monty.api.payment.domain.model.query;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record PaymentLogListQuery(String paymentNo, Pageable pageable) {
}
