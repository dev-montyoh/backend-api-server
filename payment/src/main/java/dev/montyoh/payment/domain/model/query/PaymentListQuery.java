package dev.montyoh.payment.domain.model.query;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record PaymentListQuery(Pageable pageable) {
}
