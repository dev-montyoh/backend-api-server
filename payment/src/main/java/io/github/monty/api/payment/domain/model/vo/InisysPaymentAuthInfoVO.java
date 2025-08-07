package io.github.monty.api.payment.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InisysPaymentAuthInfoVO extends PaymentAuthInfoVO {
    private String signature;
    private String verification;
    private String mKey;
    private String mid;
}
