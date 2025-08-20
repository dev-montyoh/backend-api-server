package io.github.monty.api.payment.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InicisPaymentApproveRequestVO {

    private String mid;

    private String authToken;

    private long timestamp;

    private String signature;

    private String verification;

    private String authUrl;
}
