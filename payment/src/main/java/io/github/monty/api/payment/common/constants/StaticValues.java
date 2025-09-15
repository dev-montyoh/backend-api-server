package io.github.monty.api.payment.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StaticValues {

    public static final String HEADER_ERROR_CODE = "code";

    public static final String HEADER_ERROR_MESSAGE = "message";

    public static final String DEFAULT_MESSAGE_AUTHENTICATED = "결제 인증";

    public static final String DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR = "[ERROR] 결제 승인에 실패했습니다.";

    public static final String DEFAULT_MESSAGE_PAYMENT_CANCEL_ERROR = "[ERROR] 결제 취소에 실패했습니다.";

    public static final String DEFAULT_MESSAGE_PAYMENT_NETWORK_CANCEL_ERROR = "[ERROR] 결제 취소에 실패했습니다.";

    public static final String DEFAULT_REASON_PAYMENT_NETWORK_CANCEL = "결제 망 취소";
}
