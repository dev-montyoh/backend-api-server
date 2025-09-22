package io.github.monty.api.payment.infrastructure.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InicisPaymentApprovalResponse {

    private String resultCode;

    private String resultMsg;

    private String tid;

    private String mid;

    @JsonProperty("MOID")
    private String moid;

    @JsonProperty("TotPrice")
    private long totPrice;

    private String goodName;

    private String payMethod;

    private String applDate;

    private String applTime;

    @JsonProperty("EventCode")
    private String eventCode;

    private String buyerTel;

    private String buyerEmail;

    private String custEmail;
}
