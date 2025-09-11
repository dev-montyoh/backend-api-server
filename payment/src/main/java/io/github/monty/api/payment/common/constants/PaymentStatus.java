package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    AUTHENTICATED("AUTHENTICATED", "인증 완료"),
    REQUESTED("REQUESTED", "승인 요청"),
    APPROVED("APPROVED", "승인 성공"),
    DECLINED("DECLINED", "승인 실패"),
    CANCELED("CANCELED", "결제 취소"),
    CANCELED_FAIL("CANCELED_FAIL", "결제 취소"),
    NETWORK_CANCELED("NETWORK_CANCELED", "결제 망취소"),
    NETWORK_CANCELED_FAIL("NETWORK_CANCELED_FAIL", "결제 망취소 실패"),
    ;

    private final String code;
    private final String description;

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.code.equals(code)) {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("invalid payment_status");
    }
}
