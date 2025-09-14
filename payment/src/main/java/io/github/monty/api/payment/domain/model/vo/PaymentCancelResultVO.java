package io.github.monty.api.payment.domain.model.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class PaymentCancelResultVO {
    private boolean isCancelled;

    private String resultMessage;

    private LocalDateTime cancelDateTIme;
}
