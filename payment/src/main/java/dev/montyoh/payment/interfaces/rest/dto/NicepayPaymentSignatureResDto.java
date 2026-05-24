package dev.montyoh.payment.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record NicepayPaymentSignatureResDto(String mid, String ediDate, String signData) {
}
