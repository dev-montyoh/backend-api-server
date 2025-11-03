package io.github.monty.api.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record InicisPaymentCreateResDto(String paymentNo) {
}
