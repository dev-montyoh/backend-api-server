package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentGatewayType {
    INICIS("INICIS"),
    NICEPAY("NICEPAY")
    ;

    private final String code;

    public static PaymentGatewayType fromCode(String code) {
        for (PaymentGatewayType PaymentGatewayType : PaymentGatewayType.values()) {
            if (PaymentGatewayType.code.equals(code)) {
                return PaymentGatewayType;
            }
        }
        throw new IllegalArgumentException("invalid payment_type");
    }
}
