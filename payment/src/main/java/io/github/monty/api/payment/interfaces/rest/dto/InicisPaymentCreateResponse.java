package io.github.monty.api.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record InicisPaymentCreateResponse(String paymentNo) {
}
