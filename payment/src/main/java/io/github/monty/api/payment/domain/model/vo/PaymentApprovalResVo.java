package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class PaymentApprovalResVo {
    private String resultMessage;

    private String tid;

    private String amount;

    private LocalDateTime approvalDateTime;

    private String buyerPhoneNumber;

    private String buyerEmail;

    private String paymentMethod;
}
