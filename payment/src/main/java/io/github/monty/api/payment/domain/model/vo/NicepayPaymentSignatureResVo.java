package io.github.monty.api.payment.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NicepayPaymentSignatureResVo extends PaymentSignatureResVo {
    private String mid;
    private String ediDate;
    private String signData;
}
