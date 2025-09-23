package io.github.monty.api.payment.interfaces.rest.dto;

public record InicisPaymentCreateReqDto(
        String mid,
        String orderNo,
        String authToken,
        String idcCode,
        String approvalUrl,
        String networkCancelUrl,
        long price
) {
}
