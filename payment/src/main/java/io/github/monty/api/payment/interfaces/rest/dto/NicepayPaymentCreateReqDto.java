package io.github.monty.api.payment.interfaces.rest.dto;

public record NicepayPaymentCreateReqDto(
        String orderNo,
        String authToken,
        String signature,
        String transactionId,
        String nextApprovalUrl,
        String networkCancelUrl,
        long price
) {
}
