package dev.montyoh.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NicepayPaymentApprovalResVo extends PaymentApprovalResVo {

    private String signature;
}
