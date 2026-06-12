package dev.montyoh.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PgProviderType {
    INICIS("INICIS"),
    NICEPAY("NICEPAY");

    private final String code;

    public static PgProviderType fromCode(String code) {
        for (PgProviderType PgProviderType : PgProviderType.values()) {
            if (PgProviderType.code.equals(code)) {
                return PgProviderType;
            }
        }
        throw new IllegalArgumentException("invalid payment_type");
    }
}
