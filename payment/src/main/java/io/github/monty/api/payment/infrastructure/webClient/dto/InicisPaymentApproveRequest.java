package io.github.monty.api.payment.infrastructure.webClient.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InicisPaymentApproveRequest {
    private String mid;

    private String authToken;

    private long timestamp;

    private String signature;

    private String verification;

    private String charset;

    private String format;
}
