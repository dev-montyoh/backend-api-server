package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NicepayPaymentNetworkCancelResVo extends PaymentNetworkCancelResVo {
    private String signature;

    private String cancelAmount;
}
