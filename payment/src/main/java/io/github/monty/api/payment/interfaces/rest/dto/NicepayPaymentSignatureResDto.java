package io.github.monty.api.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record NicepayPaymentSignatureResDto(String mid, String ediDate, String signData) {
}
