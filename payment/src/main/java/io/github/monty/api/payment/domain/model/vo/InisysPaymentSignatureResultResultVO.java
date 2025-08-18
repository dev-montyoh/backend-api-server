package io.github.monty.api.payment.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InisysPaymentSignatureResultResultVO extends PaymentSignatureResultVO {
    private String signature;
    private String verification;
    private String mKey;
    private String mid;
    private long timestamp;
}
