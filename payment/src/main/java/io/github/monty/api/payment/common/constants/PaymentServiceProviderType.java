package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentServiceProviderType {
    INICIS("INICIS"),
    NICEPAY("NICEPAY")
    ;

    private final String code;

    public static PaymentServiceProviderType fromCode(String code) {
        for (PaymentServiceProviderType PaymentServiceProviderType : PaymentServiceProviderType.values()) {
            if (PaymentServiceProviderType.code.equals(code)) {
                return PaymentServiceProviderType;
            }
        }
        throw new IllegalArgumentException("invalid payment_type");
    }
}
