package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    INISYS("inisys"),
    NICEPAY("nicepay")
    ;

    private final String code;
}
