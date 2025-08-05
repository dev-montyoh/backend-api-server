package io.github.monty.api.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record PaymentAuthInfoResDto(String signature, String verification, String mKey) {
}
