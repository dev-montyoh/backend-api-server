package io.github.monty.api.payment.interfaces.rest.dto;

public record InicisPaymentCreateRequest(
        String mid,
        String orderNo,
        String authorizationToken,
        String idcName,
        String authorizationUrl,
        String netCancelUrl,
        long price
) {
}
