package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class PaymentCancelResultVO {
    private String resultMessage;

    private LocalDateTime cancelDateTime;

    private String reason;
}
