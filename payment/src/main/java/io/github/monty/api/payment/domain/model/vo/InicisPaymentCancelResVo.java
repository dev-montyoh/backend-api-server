package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InicisPaymentCancelResVo extends PaymentCancelResVo {
    private String cashReceiptCancelNo;
}
