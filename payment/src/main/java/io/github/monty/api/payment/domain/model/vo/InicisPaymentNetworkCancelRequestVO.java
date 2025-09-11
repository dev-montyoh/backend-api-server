package io.github.monty.api.payment.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InicisPaymentNetworkCancelRequestVO {

    private String mid;

    private String authToken;

    private long timestamp;

    private String signature;

    private String verification;

    private String networkCancelUrl;
}
