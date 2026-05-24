package dev.montyoh.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record InicisPaymentCreateResDto(String paymentNo) {
}
