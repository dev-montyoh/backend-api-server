package io.github.monty.api.payment.interfaces.rest.dto;

public record InicisPaymentCreateRequest(
        String mid,
        String orderNo,
        String authToken,
        String idcCode,
        String approvalUrl,
        String cancelUrl,
        long price
) {
}
