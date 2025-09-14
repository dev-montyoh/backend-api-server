package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class PaymentApprovalResultVO {
    private boolean isApproved;

    private String resultMessage;

    private String tid;

    private long amount;

    private LocalDateTime approvalDateTime;

    private String buyerPhoneNumber;

    private String buyerEmail;
}
