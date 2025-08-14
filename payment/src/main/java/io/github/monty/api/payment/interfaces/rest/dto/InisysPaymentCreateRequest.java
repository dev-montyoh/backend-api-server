package io.github.monty.api.payment.interfaces.rest.dto;

public record InisysPaymentCreateRequest(
        String returnUrl,
        String cp_yn,
        String charset,
        String orderNumber,
        String authToken,
        String checkAckUrl,
        String netCancelUrl,
        String mid,
        String idc_name,
        String merchantData,
        String authUrl,
        String cardnum,
        String cardUsePoint
) {
}
