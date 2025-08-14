package io.github.monty.api.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record InisysPaymentSignatureResponse(String signature, String verification, String mKey, String mid, long timestamp) {
}
