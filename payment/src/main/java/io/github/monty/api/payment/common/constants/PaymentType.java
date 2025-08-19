package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    INICIS("INICIS"),
    NICEPAY("NICEPAY")
    ;

    private final String code;

    public static PaymentType fromCode(String code) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.code.equals(code)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("invalid payment_type");
    }
}
