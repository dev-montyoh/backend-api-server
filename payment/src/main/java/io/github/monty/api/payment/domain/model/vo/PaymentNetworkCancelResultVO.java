package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentNetworkCancelResultVO {
    private String resultMessage;

    private long timestamp;

    private String tid;

    private String mid;

    private String moid;

    private String selectPayMethod;
}
