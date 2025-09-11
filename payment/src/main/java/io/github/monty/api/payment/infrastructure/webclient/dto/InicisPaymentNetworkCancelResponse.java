package io.github.monty.api.payment.infrastructure.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InicisPaymentNetworkCancelResponse {
    private String resultCode;

    private String resultMsg;

    private long timestamp;

    private String tid;

    private String mid;

    @JsonProperty("MOID")
    private String moid;

    private String selectPayMethod;
}
