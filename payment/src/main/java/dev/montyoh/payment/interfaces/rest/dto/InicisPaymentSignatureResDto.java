package dev.montyoh.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record InicisPaymentSignatureResDto(String signature, String verification, String mKey, String mid,
                                           long timestamp) {
}
