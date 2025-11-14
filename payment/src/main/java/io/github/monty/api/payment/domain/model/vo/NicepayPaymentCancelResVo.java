package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NicepayPaymentCancelResVo extends PaymentCancelResVo {

    private String cancelAmount;

    private String signature;

    private String cancelTransactionId;
}
