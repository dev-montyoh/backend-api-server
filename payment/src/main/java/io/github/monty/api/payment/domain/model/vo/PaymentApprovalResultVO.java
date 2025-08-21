package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentApprovalResultVO {
    private String tid;

    private long amount;

    private String buyerPhoneNumber;

    private String buyerEmail;
}
