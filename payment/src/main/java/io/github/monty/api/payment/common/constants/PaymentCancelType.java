package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentCancelType {
    CANCEL("CANCEL", "전체 취소"),
    PARTIAL_CANCEL("PARTIAL_CANCEL", "부분 취소"),
    NETWORK_CANCEL("NETWORK_CANCEL", "망 취소");

    private final String code;
    private final String description;

    public static PaymentCancelType fromCode(String code) {
        for (PaymentCancelType paymentCancelType : PaymentCancelType.values()) {
            if (paymentCancelType.getCode().equals(code)) {
                return paymentCancelType;
            }
        }
        throw new IllegalArgumentException("invalid PaymentCancelType");
    }
}
