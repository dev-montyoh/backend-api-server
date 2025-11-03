package io.github.monty.api.payment.infrastructure.webclient.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InicisPaymentCancelResponse {
    private String resultCode;

    private String resultMsg;

    private String cancelDate;

    private String cancelTime;

    private String cshrCancelNum;

    private String detailResultCode;

    private String receiptInfo;
}
