package dev.montyoh.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaymentNetworkCancelResVo {
    private String resultMessage;

    private String tid;
}
